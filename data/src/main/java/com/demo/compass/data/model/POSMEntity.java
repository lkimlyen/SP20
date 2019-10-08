package com.demo.compass.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class POSMEntity {
    @SerializedName("ID")
    @Expose
    private int id;

    @SerializedName("BrandID")
    @Expose
    private int brandId;

    @SerializedName("OutletTypeID")
    @Expose
    private int outletType;

    @SerializedName("POSMName")
    @Expose
    private String posmName;

    @SerializedName("POSMDescription")
    @Expose
    private String Description;

    @SerializedName("POSMAvatar")
    @Expose
    private String posmAvatar;

    public int getId() {
        return id;
    }

    public int getBrandId() {
        return brandId;
    }

    public int getOutletType() {
        return outletType;
    }

    public String getPosmName() {
        return posmName;
    }

    public String getDescription() {
        return Description;
    }

    public String getPosmAvatar() {
        return posmAvatar;
    }
}
