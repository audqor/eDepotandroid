package com.nocson.eDepot;

public class EzServerInfo {
    private int id;
    private String address;
    private String account;
    private String password;
    private  String domain;
    public EzServerInfo(){}
    public EzServerInfo(int id,String domain, String address, String account, String password) {
        this.id = id;
        this.domain = domain;
        this.address = address;
        this.account = account;
        this.password = password;

    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return this.account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
    public String getAddress() {
        return this.address;
    }

    public String getDomain() {
        return this.domain;
    }
}
