package com.demo.architect.data.model.offline;

import com.demo.architect.utils.view.ConvertUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DetailRequestGiftModel extends RealmObject {
    @PrimaryKey
    private int Id;
    @SerializedName("pOutletID")
    @Expose
    private int OutletID;
    @SerializedName("pRequirementCode")
    @Expose
    private String Code;

    @SerializedName("pBrandSetID")
    @Expose
    private int BrandSetID;

    @SerializedName("pNumber")
    @Expose
    private int Number;

    @SerializedName("pRequirementDateTime")
    @Expose
    private String CreatedDateTime;

    @SerializedName("pUserID")
    @Expose
    private int CreatedBy;

    @SerializedName("pDeviceToken")
    @Expose
    private String DeviceToken;

    private BrandSetModel brandSetModel;

    public DetailRequestGiftModel() {
    }

    public DetailRequestGiftModel(int outletID, String code, int brandSetID, int number, String createdDateTime, int createdBy, String deviceToken) {
        OutletID = outletID;
        Code = code;
        BrandSetID = brandSetID;
        Number = number;
        CreatedDateTime = createdDateTime;
        CreatedBy = createdBy;
        DeviceToken = deviceToken;
    }

    public DetailRequestGiftModel(int id, int outletID, String code, int brandSetID, int number, String createdDateTime, int createdBy, String deviceToken) {
        Id = id;
        OutletID = outletID;
        Code = code;
        BrandSetID = brandSetID;
        Number = number;
        CreatedDateTime = createdDateTime;
        CreatedBy = createdBy;
        DeviceToken = deviceToken;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(DetailRequestGiftModel.class).max("Id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static DetailRequestGiftModel addRequestCode(Realm realm, DetailRequestGiftModel detailRequestGiftModel) {

        DetailRequestGiftModel requestGift = new DetailRequestGiftModel(id(realm) + 1, detailRequestGiftModel.OutletID,
                ConvertUtils.getCodeGenerationByTime(), detailRequestGiftModel.BrandSetID,
                detailRequestGiftModel.Number, ConvertUtils.getDateTimeCurrent(), detailRequestGiftModel.CreatedBy, detailRequestGiftModel.DeviceToken);
        requestGift = realm.copyToRealm(requestGift);
        BrandSetModel brandSetModel = realm.where(BrandSetModel.class).equalTo("Id", requestGift.BrandSetID).findFirst();
        requestGift.setBrandSetModel(brandSetModel);
        return requestGift;

    }

    public int getId() {
        return Id;
    }

    public int getOutletID() {
        return OutletID;
    }

    public String getCode() {
        return Code;
    }

    public int getBrandSetID() {
        return BrandSetID;
    }

    public int getNumber() {
        return Number;
    }

    public String getCreatedDateTime() {
        return CreatedDateTime;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public String getDeviceToken() {
        return DeviceToken;
    }

    public BrandSetModel getBrandSetModel() {
        return brandSetModel;
    }

    public void setBrandSetModel(BrandSetModel brandSetModel) {
        this.brandSetModel = brandSetModel;
    }
}
