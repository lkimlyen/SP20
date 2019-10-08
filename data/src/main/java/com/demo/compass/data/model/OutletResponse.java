package com.demo.compass.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutletResponse {
    @SerializedName("SimpleOutlet")
    @Expose
    private OutletEntiy outlet;

    @SerializedName("Status")
    @Expose
    private int status;

    @SerializedName("Description")
    @Expose
    private String description;

    public OutletEntiy getOutlet() {
        return outlet;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
