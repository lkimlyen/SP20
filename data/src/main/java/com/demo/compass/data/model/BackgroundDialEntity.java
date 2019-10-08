package com.demo.compass.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BackgroundDialEntity {
    @SerializedName("ID")
    @Expose
    private int id;

    @SerializedName("ProjectID")
    @Expose
    private int projectId;

    @SerializedName("BrandID")
    @Expose
    private int brandId;

    @SerializedName("BGButton")
    @Expose
    private String bgButton;

    @SerializedName("BGRing")
    @Expose
    private String bgCircle;

    @SerializedName("BGArrow")
    @Expose
    private String bgArrow;

    @SerializedName("BGLayout")
    @Expose
    private String bgLayout;

    @SerializedName("ColorBorder")
    @Expose
    private String colorBorder;

    public int getId() {
        return id;
    }

    public int getProjectId() {
        return projectId;
    }

    public int getBrandId() {
        return brandId;
    }

    public String getBgButton() {
        return bgButton;
    }

    public String getBgCircle() {
        return bgCircle;
    }

    public String getBgArrow() {
        return bgArrow;
    }

    public String getColorBorder() {
        return colorBorder;
    }

    public String getBgLayout() {
        return bgLayout;
    }
}
