package com.demo.architect.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChooseSetEntitiy {
    @SerializedName("RequimentChangeSetID")
    @Expose
    private int requimentChangeSetID;

    @SerializedName("BrandID")
    @Expose
    private int brandId;

    @SerializedName("BrandSetID")
    @Expose
    private int brandSetId;

    @SerializedName("NumberUsed")
    @Expose
    private int numberUsed;

    public int getRequimentChangeSetID() {
        return requimentChangeSetID;
    }

    public int getBrandId() {
        return brandId;
    }

    public int getBrandSetId() {
        return brandSetId;
    }

    public int getNumberUsed() {
        return numberUsed;
    }
}
