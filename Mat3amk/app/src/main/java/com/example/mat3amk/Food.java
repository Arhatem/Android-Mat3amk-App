package com.example.mat3amk;

public class Food {

    private String description;
    private String dish_name;
    private String image_URL;
    private String price;
    private String key;
    private String address;
    private String res;
    private  String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public String getDish_name() {
        return dish_name;
    }

    public String getImage_URL() {
        return image_URL;
    }

    public String getPrice() {
        return price;
    }
}
