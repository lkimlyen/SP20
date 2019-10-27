package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrizeEntity {
    @SerializedName("GiftID")
    @Expose
    private int giftId;

    @SerializedName("OutletID")
    @Expose
    private int outletId;

    @SerializedName("DateTrue")
    @Expose
    private String dateTrue;

    public int getGiftId() {
        return giftId;
    }

    public int getOutletId() {
        return outletId;
    }

    public String getDateTrue() {
        return dateTrue;
    }
}
