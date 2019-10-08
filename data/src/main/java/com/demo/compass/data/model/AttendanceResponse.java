package com.demo.compass.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceResponse {
    @SerializedName("AttendanceTrackingID")
    @Expose
    private int attendanceId;

    @SerializedName("Attendance_ImageID")
    @Expose
    private int attendanceImageId;

    @SerializedName("Status")
    @Expose
    private int status;

    @SerializedName("Description")
    @Expose
    private String description;

    public int getAttendanceId() {
        return attendanceId;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public int getAttendanceImageId() {
        return attendanceImageId;
    }
}
