package com.ppk.mexpense_native;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class json_upload extends AppCompatActivity {

    TextView txtResultView;
    Button btnExpUpload;
    String jsonString ="";
    m_expenseDB mExpenseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_upload);

        txtResultView = findViewById(R.id.txtResultView);
        btnExpUpload = findViewById(R.id.btnExpUpload);

        String userId="001273806";

        mExpenseDB= new m_expenseDB(this);
        ArrayList<Details> detailList = mExpenseDB.Upload();

        Upload upload = new Upload(userId, detailList);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        jsonString = gson.toJson(upload);

        txtResultView.setText(jsonString);

        btnExpUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadHandler();
            }
        });
    }

    private void uploadHandler() {
        try {
            URL pageURL = new URL(getString(R.string.expenseUrl));
            //trustAllHosts();
            HttpsURLConnection con = (HttpsURLConnection)pageURL.openConnection();
            con.setHostnameVerifier(DUMMY_VERIFIER);

            json_upload.JsonThread myTask = new json_upload.JsonThread(this, con, jsonString);
            Thread t1 = new Thread(myTask, "JSON Thread");
            t1.start();

        } catch (IOException e) { e.printStackTrace(); }
    }

    // Verifier that verifies all hosts
    private static final HostnameVerifier DUMMY_VERIFIER = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * This is based on the code provided at https://stackoverflow.com/questions/995514/https-connection-android/1000205#1000205
     * Trust every server - dont check for any certificate
     */
    private void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, null);
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class JsonThread implements Runnable
    {
        private AppCompatActivity activity;
        private HttpURLConnection con;
        private String jsonPayLoad;

        public JsonThread(AppCompatActivity activity, HttpURLConnection con, String jsonPayload)
        {
            this.activity = activity;
            this.con = con;
            this.jsonPayLoad = jsonPayload;
        }

        @Override
        public void run()
        {
            String response = "";
            if (prepareConnection()) {
                response = postJson();
            } else {
                response = "Error preparing the connection";
            }
            showResult(response);
        }


        private void showResult(String response) {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {

                    StringBuilder stringBuilder=new StringBuilder();
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        stringBuilder.append("Response:\n");
                        stringBuilder.append(jsonObject.getString("uploadResponseCode"));

                        String st = jsonObject.getString("uploadResponseCode");
                        Log.i("***Response Code***",st);

                        if(st.equals("SUCCESS")) {
                            stringBuilder.append("\nUser ID: ");
                            stringBuilder.append(jsonObject.getString("userId"));

                            stringBuilder.append("\nUpload Data: ");
                            stringBuilder.append(jsonObject.getString("number"));

                            stringBuilder.append("\nTrip Names: ");
                            stringBuilder.append(jsonObject.getString("names"));
                        }

                        stringBuilder.append("\nMessage: ");
                        stringBuilder.append(jsonObject.getString("message"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String resultStr = stringBuilder.toString();

                    txtResultView.setText(resultStr);
                }
            });
        }

        private String postJson() {
            String response = "";
            try {
                con.setFixedLengthStreamingMode(jsonPayLoad.getBytes().length);
                PrintWriter out = new PrintWriter(con.getOutputStream());
                out.print(jsonPayLoad);
                out.close();
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    response = readStream(con.getInputStream());
                } else {
                    response = "Error contacting server: " + responseCode;
                }
            } catch (Exception e) {
                response = e.toString();
            }
            return response;
        }

        private String readStream(InputStream in) {
            StringBuilder sb = new StringBuilder();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String nextLine = "";
                while ((nextLine = reader.readLine()) != null) {
                    sb.append(nextLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        private String generatePage(String content) {
            return "<html><body><p>" + content + "</p></body></html>";
        }

        private boolean prepareConnection() {
            try {
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                return true;

            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}