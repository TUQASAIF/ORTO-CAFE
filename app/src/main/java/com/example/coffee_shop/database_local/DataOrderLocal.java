package com.example.coffee_shop.database_local;

public class DataOrderLocal {

   String id, prodectID ,imageurl , prodectname , prodectCount , prodectprice, prodectNotes,prodectSize ,milk ,sugaure , style ,phoneFrind ;

    boolean switchPrice;
    boolean switchSugar;
    boolean switchMilk;
    boolean switchStyle;

    public DataOrderLocal() {
    }

    public DataOrderLocal(String id, String prodectID, String imageurl, String prodectname, String prodectCount, String prodectprice, String prodectNotes, String prodectSize, String milk, String sugaure, String style, String phoneFrind, boolean switchPrice, boolean switchSugar, boolean switchMilk, boolean switchStyle) {
        this.id = id;
        this.prodectID = prodectID;
        this.imageurl = imageurl;
        this.prodectname = prodectname;
        this.prodectCount = prodectCount;
        this.prodectprice = prodectprice;
        this.prodectNotes = prodectNotes;
        this.prodectSize = prodectSize;
        this.milk = milk;
        this.sugaure = sugaure;
        this.style = style;
        this.phoneFrind = phoneFrind;
        this.switchPrice = switchPrice;
        this.switchSugar = switchSugar;
        this.switchMilk = switchMilk;
        this.switchStyle = switchStyle;
    }

    public boolean isSwitchPrice() {
        return switchPrice;
    }

    public void setSwitchPrice(boolean switchPrice) {
        this.switchPrice = switchPrice;
    }

    public boolean isSwitchSugar() {
        return switchSugar;
    }

    public void setSwitchSugar(boolean switchSugar) {
        this.switchSugar = switchSugar;
    }

    public boolean isSwitchMilk() {
        return switchMilk;
    }

    public void setSwitchMilk(boolean switchMilk) {
        this.switchMilk = switchMilk;
    }

    public boolean isSwitchStyle() {
        return switchStyle;
    }

    public void setSwitchStyle(boolean switchStyle) {
        this.switchStyle = switchStyle;
    }

    public String getProdectID() {
        return prodectID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProdectID(String prodectID) {
        this.prodectID = prodectID;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getProdectname() {
        return prodectname;
    }

    public void setProdectname(String prodectname) {
        this.prodectname = prodectname;
    }

    public String getProdectCount() {
        return prodectCount;
    }

    public void setProdectCount(String prodectCount) {
        this.prodectCount = prodectCount;
    }

    public String getProdectprice() {
        return prodectprice;
    }

    public void setProdectprice(String prodectprice) {
        this.prodectprice = prodectprice;
    }

    public String getProdectNotes() {
        return prodectNotes;
    }

    public void setProdectNotes(String prodectNotes) {
        this.prodectNotes = prodectNotes;
    }

    public String getProdectSize() {
        return prodectSize;
    }

    public void setProdectSize(String prodectSize) {
        this.prodectSize = prodectSize;
    }

    public String getMilk() {
        return milk;
    }

    public void setMilk(String milk) {
        this.milk = milk;
    }

    public String getSugaure() {
        return sugaure;
    }

    public void setSugaure(String sugaure) {
        this.sugaure = sugaure;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getPhoneFrind() {
        return phoneFrind;
    }

    public void setPhoneFrind(String phoneFrind) {
        this.phoneFrind = phoneFrind;
    }
}

