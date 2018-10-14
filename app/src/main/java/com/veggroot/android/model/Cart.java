package com.veggroot.android.model;

public class Cart {

    private String costPerKg;
    private Integer itemId;
    private String itemImage;
    private String itemName;
    private Integer totalNumber;

    public Cart() {
    }

    public Cart(String costPerKg, Integer itemId, String itemImage, String itemName, Integer totalNumber) {

        this.costPerKg = costPerKg;
        this.itemId = itemId;
        this.itemImage = itemImage;
        this.itemName = itemName;
        this.totalNumber = totalNumber;
    }

    public void setCostPerKg(String costPerKg) {
        this.costPerKg = costPerKg;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public String getCostPerKg() {
        return costPerKg;
    }

    public Integer getItemId() {
        return itemId;
    }

    public String getItemImage() {
        return itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }


}