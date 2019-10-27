package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.CurrentBrandSetEntity;
import com.demo.architect.data.model.CurrentGift;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class CurrentGiftModel extends RealmObject {
    @PrimaryKey
    private int Id;
    @SerializedName("pOutletID")
    @Expose
    private int OutletID;

    @SerializedName("pGiftID")
    @Expose
    private int GiftID;
    private int NumberTotal;
    @SerializedName("pNumber")
    @Expose
    private int NumberRest;
    private int NumberUsed;
    @SerializedName("pTeamOutletID")
    @Expose
    private int CreatedBy;

    private int Status;

    public CurrentGiftModel() {
    }

    public CurrentGiftModel(int id, int outletID, int giftID, int numberTotal, int numberRest, int numberUsed, int createdBy, int status) {
        Id = id;
        OutletID = outletID;
        GiftID = giftID;
        NumberTotal = numberTotal;
        NumberRest = numberRest;
        NumberUsed = numberUsed;
        CreatedBy = createdBy;
        Status = status;
    }

    public CurrentGiftModel(int outletID, int giftID, int numberUsed, int createdBy) {
        OutletID = outletID;
        GiftID = giftID;
        NumberUsed = numberUsed;
        CreatedBy = createdBy;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(CurrentGiftModel.class).max("Id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static void addOrUpdateCurrentGift(Realm realm, List<CurrentGift> list, int createdBy) {
        for (CurrentGift model : list) {
            CurrentGiftModel currentGiftModel = realm.where(CurrentGiftModel.class)
                    .equalTo("GiftID", model.getGiftID()).findFirst();
            if (currentGiftModel.getNumberUsed() != model.getNumberUsed()){
                currentGiftModel.setCreatedBy(createdBy);
                currentGiftModel.setNumberUsed(model.getNumberUsed());
                currentGiftModel.setNumberRest(currentGiftModel.getNumberTotal() - currentGiftModel.getNumberUsed());
                currentGiftModel.setStatus(Constants.WAITING_UPLOAD);
            }

        }

    }

    public static void addListCurrentGift(Realm realm, List<CurrentBrandSetEntity> list, int outletId, int userId) {
        RealmResults<CurrentBrandModel> currentBrandModelRealmResults = realm.where(CurrentBrandModel.class).findAll();
        currentBrandModelRealmResults.deleteAllFromRealm();
        RealmResults<CurrentGiftModel> currentGiftModelRealmResults = realm.where(CurrentGiftModel.class).findAll();
        currentGiftModelRealmResults.deleteAllFromRealm();
        for (CurrentBrandSetEntity currentBrandSetEntity : list) {

            CurrentBrandModel currentBrandModel = new CurrentBrandModel(CurrentBrandModel.id(realm) + 1, currentBrandSetEntity.getBrandId(), outletId,
                    currentBrandSetEntity.getBrandSetId(), 0, true, Constants.UPLOADED);
            currentBrandModel = realm.copyToRealmOrUpdate(currentBrandModel);

            BrandModel brandModel = realm.where(BrandModel.class).equalTo("id", currentBrandModel.getBrandID()).findFirst();
            currentBrandModel.setBrandModel(brandModel);
            BrandSetModel brandSetModel = realm.where(BrandSetModel.class).equalTo("Id", currentBrandModel.getBrandSetID()).findFirst();
            currentBrandModel.setBrandSetModel(brandSetModel);
            for (CurrentBrandSetEntity.CurrentGift currentGift : currentBrandSetEntity.getList()) {
                BrandSetDetailModel brandSetDetailModel = realm.where(BrandSetDetailModel.class)
                        .equalTo("BrandSetID", currentBrandSetEntity.getBrandSetId())
                        .equalTo("GiftID", currentGift.getGiftId()).findFirst();
                CurrentGiftModel currentGiftModel = new CurrentGiftModel(id(realm) + 1, outletId,
                        currentGift.getGiftId(), brandSetDetailModel.getNumber(), currentGift.getNumber(),
                        brandSetDetailModel.getNumber() - currentGift.getNumber(), userId, Constants.UPLOADED);
                realm.copyToRealmOrUpdate(currentGiftModel);
            }
        }


    }

    public static List<CurrentGiftModel> getListCurrentGift(Realm realm, int outletID, int brandId) {
        CurrentBrandModel currentBrandModel = realm.where(CurrentBrandModel.class)
                .equalTo("BrandID", brandId)
                .equalTo("IsUsed", true)
                .equalTo("OutletID", outletID).findFirst();
        RealmResults<BrandSetDetailModel> results = realm.where(BrandSetDetailModel.class)
                .equalTo("BrandSetID", currentBrandModel.getBrandSetID()).findAll();
        RealmList<CurrentGiftModel> realmList = new RealmList<>();
        for (BrandSetDetailModel item : results) {
            CurrentGiftModel currentGift = realm.where(CurrentGiftModel.class).equalTo("OutletID", outletID)
                    .equalTo("GiftID", item.getGiftID()).findAll().last();
            if (currentGift != null) {
                realmList.add(currentGift);
            }
        }

        return realm.copyFromRealm(realmList);
    }


    public int getId() {
        return Id;
    }

    public int getOutletID() {
        return OutletID;
    }

    public int getGiftID() {
        return GiftID;
    }

    public int getNumberRest() {
        return NumberRest;
    }

    public int getNumberUsed() {
        return NumberUsed;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setId(int id) {
        Id = id;
    }


    public void setOutletID(int outletID) {
        OutletID = outletID;
    }

    public void setGiftID(int giftID) {
        GiftID = giftID;
    }

    public void setNumberTotal(int numberTotal) {
        NumberTotal = numberTotal;
    }

    public void setNumberRest(int numberRest) {
        NumberRest = numberRest;
    }

    public void setNumberUsed(int numberUsed) {
        NumberUsed = numberUsed;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public int getNumberTotal() {
        return NumberTotal;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public static List<CurrentGiftModel> getListRequestWaitingUpload(Realm realm, int outletId) {
        RealmResults<CurrentGiftModel> results = realm.where(CurrentGiftModel.class)
                .equalTo("OutletID", outletId).equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        return realm.copyFromRealm(results);
    }

    public static boolean checkConditionChangeBrand(Realm realm, int brandId, int brandSetIdSelect) {
        CurrentBrandModel currentBrandModel = realm.where(CurrentBrandModel.class).equalTo("BrandID", brandId)
                .equalTo("IsUsed", true).findFirst();
        BrandSetModel brandSetModelCurrent = realm.where(BrandSetModel.class).equalTo("Id", currentBrandModel.getBrandSetID()).findFirst();

        BrandSetModel brandSetModelSelect = realm.where(BrandSetModel.class).equalTo("Id",brandSetIdSelect).findFirst();

        return brandSetModelSelect.getTotalGift() > brandSetModelCurrent.getTotalGift();
    }

    public static int updateStatusCurrentGift(Realm realm, int outletId) {
        RealmResults<CurrentGiftModel> results = realm.where(CurrentGiftModel.class)
                .equalTo("OutletID", outletId).equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        for (CurrentGiftModel currentGiftModel : results) {
            currentGiftModel.setStatus(Constants.UPLOADED);
        }

        return results.size();
    }
}
