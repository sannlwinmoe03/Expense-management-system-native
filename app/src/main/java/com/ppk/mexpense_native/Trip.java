package com.ppk.mexpense_native;

import java.io.Serializable;

public class Trip implements Serializable
{
    private int  tID;
    private String  tName;
    private String  destination;
    private String  description;
    private String  tDate;
    private String  tTime;
    private String  eNum;
    private String  risk;

    public Trip(int tID, String tName, String destination, String description, String tDate, String tTime, String eNum, String risk) {
        this.tID = tID;
        this.tName = tName;
        this.destination = destination;
        this.description = description;
        this.tDate = tDate;
        this.tTime = tTime;
        this.eNum = eNum;
        this.risk = risk;
    }

    public Trip(String tName, String destination, String description, String tDate, String tTime, String eNum, String risk) {
        this.tName = tName;
        this.destination = destination;
        this.description = description;
        this.tDate = tDate;
        this.tTime = tTime;
        this.eNum = eNum;
        this.risk = risk;
    }

    public Trip(int tID, String tName, String destination, String description, String tDate, String tTime, String eNum) {
        this.tID = tID;
        this.tName = tName;
        this.destination = destination;
        this.description = description;
        this.tDate = tDate;
        this.tTime = tTime;
        this.eNum = eNum;
    }

    int gettID() {
        return tID;
    }

    public void settID(int tID) {
        this.tID = tID;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String gettDate() {
        return tDate;
    }

    public void settDate(String tDate) {
        this.tDate = tDate;
    }

    public String gettTime() {
        return tTime;
    }

    public void settTime(String tTime) {
        this.tTime = tTime;
    }

    public String geteNum() {
        return eNum;
    }

    public void seteNum(String eNum) {
        this.eNum = eNum;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }
}
