package com.demo.compass.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OutletEntiy {

    @SerializedName("OutletID")
    @Expose
    private int outletId;

    @SerializedName("OutletName")
    @Expose
    private String outletName;

    @SerializedName("OutletAddress")
    @Expose
    private String address;

    @SerializedName("City")
    @Expose
    private String city;

    @SerializedName("District")
    @Expose
    private String district;

    @SerializedName("OutletTypeID")
    @Expose
    private int outletType;

    @SerializedName("Promotion")
    @Expose
    private boolean promotion;

    @SerializedName("ChanelID")
    @Expose
    private int chanelId;

    @SerializedName("IsLuckyMega")
    @Expose
    private boolean isLuckyMega;

    public int getOutletId() {
        return outletId;
    }

    public String getOutletName() {
        return outletName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public int getOutletType() {
        return outletType;
    }

    public boolean isPromotion() {
        return promotion;
    }

    public int getChanelId() {
        return chanelId;
    }

    public boolean isLuckyMega() {
        return isLuckyMega;
    }
}
