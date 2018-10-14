package com.veggroot.android.model;


public class Vegetable {

    private String costPerKg;
    private int itemId;
    private String itemImage;
    private String itemName;

    public Vegetable() {
    }

    public Vegetable(String costPerKg, int itemId, String itemImage, String itemName) {
        this.costPerKg = costPerKg;
        this.itemId = itemId;
        this.itemImage = itemImage;
        this.itemName = itemName;
    }


    public String getCostPerKg() {
        return costPerKg;
    }

    public void setCostPerKg(String costPerKg) {
        this.costPerKg = costPerKg;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

}