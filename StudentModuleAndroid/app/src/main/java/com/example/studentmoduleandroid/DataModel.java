package com.example.studentmoduleandroid;

public class DataModel {
    String date,startTime,endTime,subject,type,lim,id;

    public DataModel(String date,String startTime,String endTime,String subject,String type,
                     String id,String lim) {
        this.date=date;
        this.type=type;
        this.startTime=startTime;
        this.endTime=endTime;
        this.subject=subject;
        this.lim=lim;
        this.id=id;
    }
    public String getdate() {
        return date;
    }

    public String gettype() {
        return type;
    }

    public String getstarttime() {
        return startTime;
    }

    public String getendtime() {
        return endTime;
    }

    public String getsubject(){
        return subject;
    }

    public String getlim(){
        return lim;
    }

    public String getsId(){ return id; }

}