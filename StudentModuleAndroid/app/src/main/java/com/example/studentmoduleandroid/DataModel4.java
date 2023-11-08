package com.example.studentmoduleandroid;

public class DataModel4 {
    String sid,schid,obtmark,dte,sname,cname,seatno,stuname;

    public DataModel4(String sid,String schid,String obtmark,String dte,String sname,String cname,
                      String seatno,String stuname) {
        this.sid = sid;
        this.schid = schid;
        this.obtmark = obtmark;
        this.dte = dte;
        this.sname = sname;
        this.cname = cname;
        this.seatno = seatno;
        this.stuname=stuname;
    }

    public String getdmsid() {
        return sid;
    }

    public String getdmschid() {
        return schid;
    }

    public String getdmobtmark() {
        return obtmark;
    }

    public String getdmdte() {
        return dte;
    }

    public String getdmsname() {
        return sname;
    }

    public String getdmcname() {
        return cname;
    }

    public String getdmseat() {
        return seatno;
    }

    public String getdmstuname() {
        return stuname;
    }
}