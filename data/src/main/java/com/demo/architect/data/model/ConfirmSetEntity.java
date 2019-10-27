package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfirmSetEntity {
    @SerializedName("WarehouseRequirementSetID")
    @Expose
    private int id;

    @SerializedName("RequirementCode")
    @Expose
    private String code;


    @SerializedName("RequirementDateTime")
    @Expose
    private String requirementDateTime;

    @SerializedName("Status")
    @Expose
    private int status;

    @SerializedName("ListDetail")
    @Expose
    private List<DetailSet> detailSetList;

    public class DetailSet{
        @SerializedName("BrandSetID")
        @Expose
        private int brandSetId;

        @SerializedName("Number")
        @Expose
        private int number;

        public int getBrandSetId() {
            return brandSetId;
        }

        public int getNumber() {
            return number;
        }
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getRequirementDateTime() {
        return requirementDateTime;
    }

    public int getStatus() {
        return status;
    }

    public List<DetailSet> getDetailSetList() {
        return detailSetList;
    }
}
