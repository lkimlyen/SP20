package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadEntity {
    @SerializedName("ImageID")
    private int id;

    public int getId() {
        return id;
    }

    @SerializedName("Status")
    @Expose
    private int status;

    @SerializedName("Description")
    @Expose
    private String description;

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
