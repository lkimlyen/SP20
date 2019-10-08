package com.demo.compass.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrentBrandSetEntity {
    @SerializedName("BrandID")
    @Expose
    private int brandId;
    @SerializedName("BrandSetID")
    @Expose
    private int brandSetId;

    @SerializedName("NumberUsed")
    @Expose
    private int numberUsed;
    @SerializedName("ListCurrentGift")
    @Expose
    private List<CurrentGift> list;

    public int getBrandSetId() {
        return brandSetId;
    }

    public List<CurrentGift> getList() {
        return list;
    }

    public int getBrandId() {
        return brandId;
    }

    public int getNumberUsed() {
        return numberUsed;
    }

    public class CurrentGift {
        @SerializedName("GiftID")
        @Expose
        private int giftId;

        @SerializedName("Number")
        @Expose
        private int number;

        public int getGiftId() {
            return giftId;
        }

        public int getNumber() {
            return number;
        }
    }


}
