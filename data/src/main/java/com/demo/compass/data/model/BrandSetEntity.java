package com.demo.compass.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrandSetEntity {
    @SerializedName("BrandSetID")
    @Expose
    private int brandSetId;

    @SerializedName("OutletTypeID")
    @Expose
    private int outletTypeId;

    @SerializedName("BrandID")
    @Expose
    private int brandId;

    @SerializedName("SetName")
    @Expose
    private String setName;


    @SerializedName("NumberTotal")
    @Expose
    private int totalGift;

    public int getOutletTypeId() {
        return outletTypeId;
    }

    public int getBrandId() {
        return brandId;
    }

    public String getSetName() {
        return setName;
    }

    public int getBrandSetId() {
        return brandSetId;
    }

    public int getTotalGift() {
        return totalGift;
    }
}
