package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

public class CurrentBrandModel extends RealmObject {
    @PrimaryKey
    private int Id;
    private int BrandID;

    @SerializedName("pOutletID")
    @Expose
    private int OutletID;
    @SerializedName("pBrandSetID")
    @Expose
    private int BrandSetID;
    @SerializedName("pNumber")
    @Expose
    private int NumberUsed;
    private BrandModel brandModel;
    private BrandSetModel brandSetModel;
    private boolean IsUsed;
    @SerializedName("pTeamOutletID")
    @Expose
    private int CreatedBy;

    @SerializedName("pDeviceDateTime")
    @Expose
    private String DateTimeCreate;

    private int Status;


    public CurrentBrandModel() {
    }

    public CurrentBrandModel(int id, int brandID, int oultetID, int brandSetID, int numberUsed, boolean isUsed, int status) {
        Id = id;
        BrandID = brandID;
        OutletID = oultetID;
        BrandSetID = brandSetID;
        NumberUsed = numberUsed;
        IsUsed = isUsed;
        Status = status;
    }


    public int getId() {
        return Id;
    }

    public int getBrandID() {
        return BrandID;
    }

    public int getBrandSetID() {
        return BrandSetID;
    }

    public boolean isUsed() {
        return IsUsed;
    }

    public int getOutletID() {
        return OutletID;
    }

    public BrandModel getBrandModel() {
        return brandModel;
    }

    public BrandSetModel getBrandSetModel() {
        return brandSetModel;
    }

    public void setBrandModel(BrandModel brandModel) {
        this.brandModel = brandModel;
    }

    public void setBrandSetModel(BrandSetModel brandSetModel) {
        this.brandSetModel = brandSetModel;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setBrandID(int brandID) {
        BrandID = brandID;
    }

    public void setOutletID(int outletID) {
        OutletID = outletID;
    }

    public void setBrandSetID(int brandSetID) {
        BrandSetID = brandSetID;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public String getDateTimeCreate() {
        return DateTimeCreate;
    }

    public void setDateTimeCreate(String dateTimeCreate) {
        DateTimeCreate = dateTimeCreate;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(CurrentBrandModel.class).max("Id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public void setUsed(boolean used) {
        IsUsed = used;
    }

    public static LinkedHashMap<Object, List<BrandSetDetailModel>> getListBrandSetDetailCurrent(Realm realm, int outletId, int outletTypeID) {
        LinkedHashMap<Object, List<BrandSetDetailModel>> list = new LinkedHashMap<>();
        RealmResults<CurrentBrandModel> results = realm.where(CurrentBrandModel.class)
                .equalTo("OutletID", outletId)
                .equalTo("IsUsed", true).findAll().sort("BrandID", Sort.ASCENDING);
        for (CurrentBrandModel currentBrandModel : results) {
            RealmResults<BrandSetDetailModel> brandSetDetailModels = realm.where(BrandSetDetailModel.class)
                    .equalTo("BrandSetID", currentBrandModel.getBrandSetID()).findAll();
            list.put(currentBrandModel, realm.copyFromRealm(brandSetDetailModels));
        }

        if (outletTypeID == 3) {
            RealmResults<BrandModel> brandModels = realm.where(BrandModel.class).equalTo("IsDialLucky", false).equalTo("IsRequest", true).findAll();
            for (BrandModel brandModel : brandModels) {
                RealmResults<BrandSetModel> brandSetModels = realm.where(BrandSetModel.class).equalTo("BrandID", brandModel.getId()).findAll();
                for (BrandSetModel brandSetModel : brandSetModels) {
                    RealmResults<BrandSetDetailModel> brandSetDetailModels = realm.where(BrandSetDetailModel.class)
                            .equalTo("BrandSetID", brandSetModel.getId()).findAll();
                    list.put(brandSetModel, realm.copyFromRealm(brandSetDetailModels));
                }

            }
        }
        return list;
    }

    public static List<CurrentBrandModel> getListBrandSetCurrent(Realm realm, int outletId) {
        RealmResults<CurrentBrandModel> results = realm.where(CurrentBrandModel.class)
                .equalTo("OutletID", outletId)
                .equalTo("IsUsed", true)
                .findAll().sort("BrandID", Sort.ASCENDING);
        return realm.copyFromRealm(results);
    }

    public static void updateBrandSetCurrent(Realm realm, int outletId, int brandsetId) {
        CurrentBrandModel currentBrandModel = realm.where(CurrentBrandModel.class)
                .equalTo("OutletID", outletId)
                .equalTo("BrandSetID", brandsetId)
                .findFirst();
        BrandSetModel brandSetModel = realm.where(BrandSetModel.class).equalTo("Id", brandsetId).findFirst();
        BrandModel brandModel = realm.where(BrandModel.class).equalTo("id", brandSetModel.getBrandID()).findFirst();
        CurrentBrandModel currentBrandModel1 = realm.where(CurrentBrandModel.class).equalTo("OutletID", outletId)
                .notEqualTo("BrandSetID", brandsetId)
                .equalTo("BrandID", brandModel.getId())
                .equalTo("IsUsed", true)
                .findFirst();
        if (currentBrandModel1 != null) {
            currentBrandModel1.setUsed(false);
        }


        if (currentBrandModel != null) {
            currentBrandModel.setUsed(true);
        } else {
            currentBrandModel = new CurrentBrandModel(id(realm) + 1, brandModel.getId(), outletId, brandsetId, 0, true, Constants.WAITING_UPLOAD);
            currentBrandModel = realm.copyToRealmOrUpdate(currentBrandModel);
            currentBrandModel.setBrandSetModel(brandSetModel);
            currentBrandModel.setBrandModel(brandModel);
        }
        RealmResults<BrandSetDetailModel> brandSetDetailModelRealmResults = realm.where(BrandSetDetailModel.class)
                .equalTo("BrandSetID", brandsetId)
                .findAll();
        for (BrandSetDetailModel brandSetDetailModel : brandSetDetailModelRealmResults) {
            CurrentGiftModel currentGiftModel = realm.where(CurrentGiftModel.class).equalTo("OutletID", outletId)
                    .equalTo("GiftID", brandSetDetailModel.getGiftID())
                    .findFirst();
            if (currentGiftModel != null) {
                currentGiftModel.setNumberTotal(brandSetDetailModel.getNumber());
                currentGiftModel.setNumberRest(currentGiftModel.getNumberTotal() - currentGiftModel.getNumberUsed());
            }

        }
    }

    public int getNumberUsed() {
        return NumberUsed;
    }

    public void setNumberUsed(int numberUsed) {
        NumberUsed = numberUsed;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public static CurrentBrandModel getListCurrentBrandSet(Realm realm, int outletId, int brandId) {
        CurrentBrandModel currentBrandModel = realm.where(CurrentBrandModel.class).equalTo("OutletID", outletId)
                .equalTo("BrandID", brandId)
                .equalTo("IsUsed", true).findFirst();
        return realm.copyFromRealm(currentBrandModel);
    }

    public static void updateNumberCurrentBrand(Realm realm, CurrentBrandModel currentBrandModel) {
        CurrentBrandModel currentBrandCurrent = realm.where(CurrentBrandModel.class).equalTo("Id", currentBrandModel.getId())
                .findFirst();
        if (currentBrandCurrent.getNumberUsed() != currentBrandModel.getNumberUsed()) {
            currentBrandCurrent = realm.copyToRealmOrUpdate(currentBrandModel);
            currentBrandCurrent.setStatus(Constants.WAITING_UPLOAD);
        }

    }

    public static List<CurrentBrandModel> getListCurrentBrandWaitingUpload(Realm realm, int outletId) {
        RealmResults<CurrentBrandModel> results = realm.where(CurrentBrandModel.class).equalTo("OutletID", outletId)
                .notEqualTo("CreatedBy", 0).isNotNull("DateTimeCreate").equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        return realm.copyFromRealm(results);
    }

    public static int updateStatusCurrentBrand(Realm realm, int outletId) {
        RealmResults<CurrentBrandModel> results = realm.where(CurrentBrandModel.class).equalTo("OutletID", outletId)
                .notEqualTo("CreatedBy", 0).isNotNull("DateTimeCreate").equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        for (CurrentBrandModel currentBrandModel : results) {
            currentBrandModel.setStatus(Constants.UPLOADED);
            currentBrandModel.setNumberUsed(0);
        }
        return results.size();
    }
}
