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
}
