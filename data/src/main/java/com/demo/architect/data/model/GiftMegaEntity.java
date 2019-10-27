package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GiftMegaEntity {

    @SerializedName("TurnID")
    @Expose
    private int id;

    @SerializedName("DateStart")
    @Expose
    private String dateStart;

    @SerializedName("DateEnd")
    @Expose
    private String dateEnd;

    @SerializedName("ListGift")
    @Expose
    private List<GiftMegaInTimeEntity> listGift;


    public int getId() {
        return id;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public List<GiftMegaInTimeEntity> getListGift() {
        return listGift;
    }
}
