package com.demo.architect.data.model.offline;


import com.demo.architect.data.model.BrandSetDetailEntity;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class BrandSetDetailModel extends RealmObject {

    @PrimaryKey
    private int Id;
    private int BrandSetID;
    private int GiftID;
    private int Number;
    private int Percentage;
    private GiftModel giftModel;

    public BrandSetDetailModel() {
    }

    public BrandSetDetailModel(int id, int brandSetID, int giftID, int number, int percentage) {
        Id = id;
        BrandSetID = brandSetID;
        GiftID = giftID;
        Number = number;
        Percentage = percentage;
    }

    public static void addBrandSetDetail(Realm realm, List<BrandSetDetailEntity> list) {
        RealmResults<BrandSetDetailModel> results = realm.where(BrandSetDetailModel.class).findAll();
        results.deleteAllFromRealm();
        for (BrandSetDetailEntity item : list) {
            BrandSetDetailModel brandSetDetailModel = new BrandSetDetailModel(item.getId(), item.getBrandSetId(), item.getGiftId(),
                    item.getNumber(), item.getPercentage());

            brandSetDetailModel = realm.copyToRealm(brandSetDetailModel);
            GiftModel giftModel = realm.where(GiftModel.class).equalTo("Id", brandSetDetailModel.getGiftID()).findFirst();
            brandSetDetailModel.setGiftModel(giftModel);
        }
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getBrandSetID() {
        return BrandSetID;
    }

    public void setBrandSetID(int brandSetID) {
        BrandSetID = brandSetID;
    }

    public int getGiftID() {
        return GiftID;
    }

    public void setGiftID(int giftID) {
        GiftID = giftID;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public int getPercentage() {
        return Percentage;
    }

    public void setPercentage(int percentage) {
        Percentage = percentage;
    }

    public GiftModel getGiftModel() {
        return giftModel;
    }

    public void setGiftModel(GiftModel giftModel) {
        this.giftModel = giftModel;
    }

    public static List<BrandSetDetailModel> getListBrandSetDetail(Realm realm, int brandId) {
        CurrentBrandModel currentBrandModel = realm.where(CurrentBrandModel.class)
                .equalTo("BrandID", brandId)
                .equalTo("IsUsed", true).findFirst();
        RealmResults<BrandSetDetailModel> results = realm.where(BrandSetDetailModel.class).equalTo("BrandSetID", currentBrandModel.getBrandSetID()).findAll();
        return realm.copyFromRealm(results);
    }
}
