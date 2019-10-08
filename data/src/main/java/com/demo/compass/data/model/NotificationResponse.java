package com.demo.compass.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationResponse {
    @SerializedName("ListSimpleNotification")
    @Expose
    private List<NotificationEntity> list;

    @SerializedName("Status")
    @Expose
    private int status;

    @SerializedName("Description")
    @Expose
    private String description;

    public List<NotificationEntity> getList() {
        return list;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
