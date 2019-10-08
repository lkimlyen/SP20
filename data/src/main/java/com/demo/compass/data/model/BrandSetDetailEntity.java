package com.demo.compass.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrandSetDetailEntity {
    @SerializedName("BrandSetDetailID")
    @Expose
    private  int id;
    @SerializedName("BrandSetID")
    @Expose
    private int brandSetId;

    @SerializedName("GiftID")
    @Expose
    private int giftId;

    @SerializedName("Number")
    @Expose
    private int number;

    @SerializedName("Percentage")
    @Expose
    private int percentage;

    public int getBrandSetId() {
        return brandSetId;
    }

    public int getGiftId() {
        return giftId;
    }

    public int getNumber() {
        return number;
    }

    public int getPercentage() {
        return percentage;
    }

    public int getId() {
        return id;
    }


}
