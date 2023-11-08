package com.example.studentmoduleandroid;

public class DataModel2 {

    String que,coropt,youropt;

    public DataModel2(String que,String coropt,String youropt) {
        this.que = que;
        this.coropt = coropt;
        this.youropt = youropt;
    }
    public String getquestion() {
        return que;
    }

    public String getcoropt() {
        return coropt;
    }

    public String getyouropt() {
        return youropt;
    }

}