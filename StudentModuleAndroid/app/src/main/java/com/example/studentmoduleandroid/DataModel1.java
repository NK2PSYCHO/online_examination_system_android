package com.example.studentmoduleandroid;

public class DataModel1 {
    String que,opt1,opt2,opt3,opt4,qid;

    public DataModel1(String que,String opt1,String opt2,String opt3,String opt4,String qid) {
        this.que = que;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        this.opt4 = opt4;
        this.qid = qid;
    }
    public String getquestion() {
        return que;
    }

    public String getoption1() {
        return opt1;
    }

    public String getoption2() {
        return opt2;
    }

    public String getoption3() {
        return opt3;
    }

    public String getoption4(){
        return opt4;
    }

    public String getqid(){
        return qid;
    }

}