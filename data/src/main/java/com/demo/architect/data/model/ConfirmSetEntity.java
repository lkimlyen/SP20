package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfirmSetEntity {
    @SerializedName("ID")
    @Expose
    private int ID;

    @SerializedName("SPID")
    @Expose
    private int SPID;


    @SerializedName("SPName")
    @Expose
    private String SPName;

    @SerializedName("WarehouseID")
    @Expose
    private int WarehouseID;
    @SerializedName("Status")
    @Expose
    private int Status;

    @SerializedName("CreatedAt")
    @Expose
    private long CreatedAt;


    @SerializedName("Gifts")
    @Expose
    private List<GiftEntity> Gifts;
    @SerializedName("BrandSets")
    @Expose
    private List<DetailSet> detailSetList;

    public class DetailSet{
        @SerializedName("BrandSetID")
        @Expose
        private int brandSetId;

        @SerializedName("Number")
        @Expose
        private int number;

        public int getBrandSetId() {
            return brandSetId;
        }

        public int getNumber() {
            return number;
        }
    }

    public int getID() {
        return ID;
    }

    public int getSPID() {
        return SPID;
    }

    public String getSPName() {
        return SPName;
    }

    public int getWarehouseID() {
        return WarehouseID;
    }

    public int getStatus() {
        return Status;
    }

    public long getCreatedAt() {
        return CreatedAt;
    }

    public List<GiftEntity> getGifts() {
        return Gifts;
    }

    public List<DetailSet> getDetailSetList() {
        return detailSetList;
    }
}
