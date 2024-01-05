package com.ppk.mexpense_native;

public class Details {
    private String name;
    private String expenseType;
    private  String expenseAmount;
    private String expenseTime;
    private String comment;

    public Details(String name) {
        this.name = name;
    }

    public Details(String name, String expenseType, String expenseAmount, String expenseTime, String comment) {
        this.name = name;
        this.expenseType = expenseType;
        this.expenseAmount = expenseAmount;
        this.expenseTime = expenseTime;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getExpenseType() {
        return expenseType;
    }
    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }
    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseTime() {
        return expenseTime;
    }
    public void setExpenseTime(String expenseTime) {
        this.expenseTime = expenseTime;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
