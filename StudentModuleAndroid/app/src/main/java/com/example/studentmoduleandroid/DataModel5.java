package com.example.studentmoduleandroid;

public class DataModel5 {

    String sid,schid,qid,que,mark,ans;

    public DataModel5(String qid,String schid,String sid,String que,String mark,String ans){
        this.qid=qid;
        this.schid=schid;
        this.sid=sid;
        this.que=que;
        this.mark=mark;
        this.ans=ans;
    }

    public String getfsid() {
        return sid;
    }

    public String getfqid() {
        return qid;
    }

    public String getfschid() {
        return schid;
    }

    public String getfque() {
        return que;
    }

    public String getfmark() {
        return mark;
    }

    public String getfans() {
        return ans;
    }

}