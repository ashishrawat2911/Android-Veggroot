package com.veggroot.android.model;

public class Item {


    public int itemId;
    public String itemName;
    public String itemImage;
    public String costPerKg;
    public int totalNumber;

    public Item(int itemId, String itemName, String itemImage, String costPerKg, int totalNumber) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemImage = itemImage;
        this.costPerKg = costPerKg;
        this.totalNumber = totalNumber;
    }
    public Item() {

    }
    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public String getCostPerKg() {
        return costPerKg;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public void setCostPerKg(String costPerKg) {
        this.costPerKg = costPerKg;
    }
}
