package com.example.studentmoduleandroid;

public class DataModel3 {

    String que,yourans,remark,mark;

    public DataModel3(String que,String yourans,String remark,String mark) {
        this.que = que;
        this.yourans = yourans;
        this.remark = remark;
        this.mark = mark;
    }
    public String getquestion() {
        return que;
    }

    public String getyourans() {
        return yourans;
    }

    public String getremark() {
        return remark;
    }

    public String getmark() {
        return mark;
    }

}