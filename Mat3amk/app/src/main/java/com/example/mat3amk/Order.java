package com.example.mat3amk;

public class Order {
    private String ProductName;
    private String Quantity;
    private String Price;
    private  int ID;

    public Order() {
    }

    public Order(String productName, String quantity, String price) {
        ProductName = productName;
        Quantity = quantity;
        Price = price;
    }

    public int getID() {
        return ID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
