package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiftEntity {
    @SerializedName("ID")
    @Expose
    private int id;

    @SerializedName("GiftCode")
    @Expose
    private String giftCode;

    @SerializedName("GiftName")
    @Expose
    private String giftName;
    @SerializedName("FilePath")
    @Expose
    private String filePath;

    @SerializedName("IsTopupCard")
    @Expose
    private boolean isTopupCard;


    @SerializedName("CategoryType")
    @Expose
    private String CategoryType;


    @SerializedName("Quantity")
    @Expose
    private int Quantity;

    @SerializedName("QuantitySuccess")
    @Expose
    private int QuantitySuccess;

    @SerializedName("QuantityFail")
    @Expose
    private int QuantityFail;

    @SerializedName("QuantityWaitting")
    @Expose
    private int QuantityWaitting;
    public int getId() {
        return id;
    }


    public String getGiftCode() {
        return giftCode;
    }

    public String getGiftName() {
        return giftName;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isTopupCard() {
        return isTopupCard;
    }

    public String getCategoryType() {
        return CategoryType;
    }

    public int getQuantity() {
        return Quantity;
    }

    public int getQuantitySuccess() {
        return QuantitySuccess;
    }

    public int getQuantityFail() {
        return QuantityFail;
    }

    public int getQuantityWaitting() {
        return QuantityWaitting;
    }
}
