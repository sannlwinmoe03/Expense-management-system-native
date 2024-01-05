package com.ppk.mexpense_native;

import java.io.Serializable;
import java.util.ArrayList;

public class Upload implements Serializable {
    private ArrayList<Details> detailList;
    private String userId;

    public Upload(String userId) {
        this.userId = userId;
    }

    public Upload(ArrayList<Details> detailList) {
        this.detailList = detailList;
    }

    public Upload(String userId, ArrayList<Details> detailList) {
        this.userId = userId;
        this.detailList = detailList;
    }

    public ArrayList<Details> getExpenseList() {
        return detailList;
    }
    public void setExpenseList(ArrayList<Details> detailList) {
        this.detailList = detailList;
    }

    public String userId() {
        return userId;
    }
    public void userId(String userId) {
        this.userId = userId;
    }
}
