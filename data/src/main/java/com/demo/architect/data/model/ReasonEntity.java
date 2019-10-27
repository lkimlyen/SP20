package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReasonEntity {
    @SerializedName("ID")
    @Expose
    private int id;

    @SerializedName("EmergencyCode")
    @Expose
    private String emergencyCode;

    @SerializedName("EmergencyName")
    @Expose
    private String emergencyName;

    @SerializedName("EmergencyDescription")
    @Expose
    private String emergencyDescription;

    public int getId() {
        return id;
    }

    public String getEmergencyCode() {
        return emergencyCode;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public String getEmergencyDescription() {
        return emergencyDescription;
    }
}
