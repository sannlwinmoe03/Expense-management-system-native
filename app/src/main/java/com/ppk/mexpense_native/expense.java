package com.ppk.mexpense_native;

import java.io.Serializable;

public class expense implements Serializable
{
    private  int ID;
    private  String eName;
    private  String eAmount;
    private  String eDate;
    private  String eCom;
    private int eID;

    public expense(int ID, String eName, String eAmount, String eDate, String eCom, int eID)
    {
        this.ID = ID;
        this.eName = eName;
        this.eAmount = eAmount;
        this.eDate = eDate;
        this.eCom = eCom;
        this.eID = eID;
    }
    public expense(String eName, String eAmount, String eDate, String eCom, int eID)
    {
        this.eName = eName;
        this.eAmount = eAmount;
        this.eDate = eDate;
        this.eCom = eCom;
        this.eID = eID;
    }
    public expense(int ID, String eName, String eAmount, String eDate, String eCom)
    {
        this.ID = ID;
        this.eName = eName;
        this.eAmount = eAmount;
        this.eDate = eDate;
        this.eCom = eCom;
    }
    public expense(String eName, String eAmount, String eDate, String eCom)
    {
        this.eName = eName;
        this.eAmount = eAmount;
        this.eDate = eDate;
        this.eCom = eCom;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String geteAmount() {
        return eAmount;
    }

    public void seteAmount(String eAmount) {
        this.eAmount = eAmount;
    }

    public String geteDate() {
        return eDate;
    }

    public void seteDate(String eDate) {
        this.eDate = eDate;
    }

    public String geteCom() {
        return eCom;
    }

    public void seteCom(String eCom) {
        this.eCom = eCom;
    }

    public int geteID() {
        return eID;
    }

    public void seteID(int eID) {
        this.eID = eID;
    }
}
