package com.veggroot.android.model;

/**
 * created by Ashish Rawat
 */

public class OrderItem {
    private String itemName;
    private String itemImage;
    private Double itemPrice;
    private String orderTime;
    private int totalNumber;
    private String unit;

    public OrderItem() {
    }

    public OrderItem(String itemName, String itemImage, Double itemPrice, String orderTime, int totalNumber, String unit) {
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.itemPrice = itemPrice;
        this.orderTime = orderTime;
        this.totalNumber = totalNumber;
        this.unit = unit;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}