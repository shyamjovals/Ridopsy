package com.example.prasanna.prot1;

import java.io.Serializable;

/**
 * Created by arvind on 18/01/16.
 */
public class Schedule implements Serializable {
    public String username;
    public String password;
    public String ride;
    public String busno;
    public String busdep;
    public String busdest;
    public String fname;
    public String fnum;
    public String ambname;
    public String ambnum;
    public String bankname;
    public String haltven;
    public String haltdur;
    public String cname;
    public String cnum;
    public String cmodel;
    public String ccap;
    public String transtop;
    public Double latit;
    public Double longit;
    public String _id;
    public String _rev = null;

    public Schedule(String username, String password, String ride, String busno, String busdep, String busdest, String fname, String fnum, String ambname, String ambnum, String bankname, String haltven, String haltdur, String cname, String cnum, String cmodel, String ccap, String transtop,Double latit, Double longit){
        this._id=username;
        this.username=username;
        this.password=password;
        this.ride=ride;
        this.busno=busno;
        this.busdep=busdep;
        this.busdest=busdest;
        this.fname=fname;
        this.fnum=fnum;
        this.ambname=ambname;
        this.ambnum=ambnum;
        this.bankname=bankname;
        this.haltven=haltven;
        this.haltdur=haltdur;
        this.cname=cname;
        this.cnum=cnum;
        this.cmodel=cmodel;
        this.ccap=ccap;
        this.transtop=transtop;
        this.latit=latit;
        this.longit=longit;

    }

}