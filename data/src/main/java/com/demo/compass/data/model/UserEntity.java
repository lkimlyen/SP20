package com.demo.compass.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserEntity implements Serializable {
    @SerializedName("TeamOutletID")
    @Expose
    private int teamOutletId;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("ProjectID")
    @Expose
    private int projectId;

    @SerializedName("UserID")
    @Expose
    private int userId;

    @SerializedName("ProjectName")
    @Expose
    private String projectName;

    @SerializedName("EntityOutlet")
    @Expose
    private OutletEntiy outlet;

    public int getTeamOutletId() {
        return teamOutletId;
    }

    public String getName() {
        return name;
    }

    public OutletEntiy getOutlet() {
        return outlet;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public int getUserId() {
        return userId;
    }
}