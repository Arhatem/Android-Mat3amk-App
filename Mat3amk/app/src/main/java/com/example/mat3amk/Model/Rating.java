package com.example.mat3amk.Model;

public class Rating {

    private String userName;
    private String foodName;
    private String rateValue;
    private String comment;
    private String email;
    public Rating() {
    }

    public Rating(String userName, String foodName, String rateValue, String comment,String email) {
        this.userName = userName;
        this.foodName = foodName;
        this.rateValue = rateValue;
        this.comment = comment;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
