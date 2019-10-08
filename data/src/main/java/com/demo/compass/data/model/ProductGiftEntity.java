package com.demo.compass.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductGiftEntity {
    @SerializedName("ProductGiftID")
    @Expose
    private int productGiftID;

    @SerializedName("ProductID")
    @Expose
    private int productID;

    @SerializedName("GiftID")
    @Expose
    private int giftID;

    @SerializedName("Number")
    @Expose
    private int number;

    public int getProductGiftID() {
        return productGiftID;
    }

    public int getProductID() {
        return productID;
    }

    public int getGiftID() {
        return giftID;
    }

    public int getNumber() {
        return number;
    }
}
