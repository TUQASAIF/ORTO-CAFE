package com.example.coffee_shop.ui.model;

public class Data_SupCategrory {


    String title;
    String priceD;
    String priceS;
    String pricem;
    String priceL;
    String image;

    String idCat;

    boolean switchPrice;
    boolean switchSugar;
    boolean switchMilk;
    boolean switchStyle;


    public Data_SupCategrory() {
    }


    public Data_SupCategrory(String title, String priceD, String priceS, String pricem, String priceL,
                             String image, String idCat, boolean switchPrice, boolean switchSugar, boolean switchMilk, boolean switchStyle) {
        this.title = title;
        this.priceD = priceD;
        this.priceS = priceS;
        this.pricem = pricem;
        this.priceL = priceL;
        this.image = image;
        this.idCat = idCat;
        this.switchPrice = switchPrice;
        this.switchSugar = switchSugar;
        this.switchMilk = switchMilk;
        this.switchStyle = switchStyle;
    }

    public String getIdCat() {
        return idCat;
    }

    public void setIdCat(String idCat) {
        this.idCat = idCat;
    }

    public boolean getSwitchPrice() {
        return switchPrice;
    }

    public void setSwitchPrice(boolean switchPrice) {
        this.switchPrice = switchPrice;
    }

    public boolean getSwitchSugar() {
        return switchSugar;
    }

    public void setSwitchSugar(boolean switchSugar) {
        this.switchSugar = switchSugar;
    }

    public boolean getSwitchMilk() {
        return switchMilk;
    }

    public void setSwitchMilk(boolean switchMilk) {
        this.switchMilk = switchMilk;
    }

    public boolean getSwitchStyle() {
        return switchStyle;
    }

    public void setSwitchStyle(boolean switchStyle) {
        this.switchStyle = switchStyle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriceD() {
        return priceD;
    }

    public void setPriceD(String priceD) {
        this.priceD = priceD;
    }

    public String getPriceS() {
        return priceS;
    }

    public void setPriceS(String priceS) {
        this.priceS = priceS;
    }

    public String getPricem() {
        return pricem;
    }

    public void setPricem(String pricem) {
        this.pricem = pricem;
    }

    public String getPriceL() {
        return priceL;
    }

    public void setPriceL(String priceL) {
        this.priceL = priceL;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId_food() {
        return idCat;
    }

    public void setId_food(String id_food) {
        this.idCat = id_food;
    }
}
