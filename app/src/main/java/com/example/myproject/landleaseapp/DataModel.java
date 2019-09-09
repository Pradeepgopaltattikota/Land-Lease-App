package com.example.myproject.landleaseapp;


public class DataModel {

    String name,place,add,rate,link,img;

    public DataModel() {
    }

    public DataModel(String name, String place, String add, String rate, String link, String img) {
        this.name = name;
        this.place = place;
        this.add = add;
        this.rate = rate;
        this.link = link;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


}
