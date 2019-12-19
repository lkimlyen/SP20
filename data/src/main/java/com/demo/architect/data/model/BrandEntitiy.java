package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrandEntitiy {
    @SerializedName("ID")
    @Expose
    private int id;

    @SerializedName("BrandCode")
    @Expose
    private String brandCode;

    @SerializedName("BrandName")
    @Expose
    private String brandName;

    @SerializedName("IsDialLucky")
    @Expose
    private boolean isDialLucky;
    @SerializedName("IsTopUpCard")
    @Expose
    private boolean isTopupCard;

    @SerializedName("MaximumChangeGift")
    @Expose
    private int maximumChangeGift;

    @SerializedName("NumberOfEnough")
    @Expose
    private int numberOfEnough;

    @SerializedName("NumberGiftOfDay")
    @Expose
    private int numberGiftOfDay;

    @SerializedName("IsRequest")
    @Expose
    private boolean isRequest;

    public int getId() {
        return id;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public String getBrandName() {
        return brandName;
    }

    public boolean isDialLucky() {
        return isDialLucky;
    }

    public int getMaximumChangeGift() {
        return maximumChangeGift;
    }

    public int getNumberOfEnough() {
        return numberOfEnough;
    }

    public int getNumberGiftOfDay() {
        return numberGiftOfDay;
    }

    public boolean isTopupCard() {
        return isTopupCard;
    }

    public boolean isRequest() {
        return isRequest;
    }
}
