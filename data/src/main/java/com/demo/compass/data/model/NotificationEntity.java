package com.demo.compass.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationEntity {
    @SerializedName("ID")
    @Expose
    private int id;

    @SerializedName("NotificationCode")
    @Expose
    private String code;

    @SerializedName("NotificationContent")
    @Expose
    private String description;

    @SerializedName("NotificationTitle")
    @Expose
    private String title;

    @SerializedName("CreatedDateTime")
    @Expose
    private String date;

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getCode() {
        return code;
    }
}
