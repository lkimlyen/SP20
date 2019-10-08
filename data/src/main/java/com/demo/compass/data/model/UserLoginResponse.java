package com.demo.compass.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginResponse {

    @SerializedName("UserInfo")
    @Expose
    private UserInfo userInfo;

    @SerializedName("Status")
    @Expose
    private int status;

    @SerializedName("Description")
    @Expose
    private String description;

    public class UserInfo {
        @SerializedName("UserTeam_ID")
        @Expose
        private int userTeamId;

        @SerializedName("DisplayName")
        @Expose
        private String displayName;


    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
    public int getUserTeamId() {
        return userInfo.userTeamId;
    }

    public String getDisplayName() {
        return userInfo.displayName;
    }

}
