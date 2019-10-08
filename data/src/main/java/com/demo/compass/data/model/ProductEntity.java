package com.demo.compass.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductEntity {
    @SerializedName("ID")
    @Expose
    private int id;

    @SerializedName("ProductCode")
    @Expose
    private String productCode;

    @SerializedName("ProductName")
    @Expose
    private String productName;

    @SerializedName("FilePath")
    @Expose
    private String filePath;

    @SerializedName("BrandID")
    @Expose
    private int brandID;

    @SerializedName("IsChangeGift")
    @Expose
    private boolean isChangeGift;

    @SerializedName("IsPrice")
    @Expose
    private boolean isPrice;

    @SerializedName("IsStock")
    @Expose
    private boolean isStock;

    @SerializedName("IsTakeOffVolume")
    @Expose
    private boolean isTakeOffVolume;

    @SerializedName("IsGetInfo")
    @Expose
    private boolean isGetInfo;

    @SerializedName("NumberOfEnough")
    @Expose
    private int numberOfEnough;

    public int getId() {
        return id;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getBrandID() {
        return brandID;
    }

    public boolean isChangeGift() {
        return isChangeGift;
    }

    public boolean isPrice() {
        return isPrice;
    }

    public boolean isStock() {
        return isStock;
    }

    public boolean isTakeOffVolume() {
        return isTakeOffVolume;
    }

    public int getNumberOfEnough() {
        return numberOfEnough;
    }

    public boolean isGetInfo() {
        return isGetInfo;
    }
}
