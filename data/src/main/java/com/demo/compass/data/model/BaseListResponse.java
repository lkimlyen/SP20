package com.demo.compass.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by uyminhduc on 10/23/16.
 */

public class BaseListResponse<T> {
    @SerializedName("Status")
    @Expose
    private int status;


    @SerializedName("Description")
    @Expose
    private String description;


    @SerializedName("Data")
    @Expose
    private List<T> data;

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public List<T> getData() {
        return data;
    }
}
