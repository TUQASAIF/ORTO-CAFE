package com.example.coffee_shop.ui.model;

import com.example.coffee_shop.database_local.DataOrderLocal;

import java.util.List;

public class DataReqeste {

    String iduser,address,total,status,total_decount ,total_due,location_driver  , note,phoneFrind,date,current_date;
    List<DataOrderLocal> food;

    public DataReqeste() {
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public DataReqeste(String total_due, String total_decount, String iduser,
                       String address, String total, List<DataOrderLocal> food,String note, String phoneFrind,String date,String current_date) {
        this.iduser = iduser;
        this.address = address;
        this.total = total;
        this.food = food;
        this.status="0";
        this.total_decount=total_decount;
        this.total_due=total_due;
        this.location_driver="";
        this.note=note;
        this.phoneFrind=phoneFrind;
        this.date=date;
        this.current_date=current_date;
    }

    public String getLocation_driver() {
        return location_driver;
    }

    public void setLocation_driver(String location_driver) {
        this.location_driver = location_driver;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhoneFrind() {
        return phoneFrind;
    }

    public void setPhoneFrind(String phoneFrind) {
        this.phoneFrind = phoneFrind;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal_decount() {
        return total_decount;
    }

    public String getTotal_due() {
        return total_due;
    }

    public void setTotal_due(String total_due) {
        this.total_due = total_due;
    }

    public void setTotal_decount(String total_decount) {
        this.total_decount = total_decount;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<DataOrderLocal> getFood() {
        return food;
    }

    public void setFood(List<DataOrderLocal> food) {
        this.food = food;
    }
}
