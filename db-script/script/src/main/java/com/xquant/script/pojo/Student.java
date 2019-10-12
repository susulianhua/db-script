package com.xquant.script.pojo;



public class Student {
    private String sno;
    private String spwd;
    private int sage;
    private String sname;
    private String ssex;
    private String stel;


    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSpwd() {
        return spwd;
    }

    public void setSpwd(String spwd) {
        this.spwd = spwd;
    }

    public String getSsex() {
        return ssex;
    }

    public void setSsex(String ssex) {
        this.ssex = ssex;
    }

    public int getSage() {
        return sage;
    }

    public void setSage(int sage) {
        this.sage = sage;
    }

    public String getStel() {
        return stel;
    }

    public void setStel(String stel) {
        this.stel = stel;
    }

    @Override
    public String toString(){
        return "Student{" +
                "sno= " + sno +
                ", sname=" + sname +
                ", spwd=" + spwd +
                ", ssex=" + ssex +
                ", sage=" + sage +
                ", stel=" + stel + "}\n";

    }
}
