package com.demo.compass.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiftMegaInTimeEntity {
    @SerializedName("GiftID")
    @Expose
    private int id;


    @SerializedName("Name")
    @Expose
    private String giftName;


    @SerializedName("FilePath")
    @Expose
    private String filePath;

    @SerializedName("IsGift")
    @Expose
    private boolean isGift;

    public int getId() {
        return id;
    }

    public boolean isGift() {
        return isGift;
    }

    public String getGiftName() {
        return giftName;
    }

    public String getFilePath() {
        return filePath;
    }
}
