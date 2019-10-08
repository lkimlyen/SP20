package com.demo.compass.data.model.offline;

import com.demo.compass.data.model.GiftEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class GiftModel extends RealmObject {
    @PrimaryKey
    private int Id;
    private String GiftCode;
    private String GiftName;
    private String FilePath;

    public GiftModel() {
    }

    public GiftModel(int id, String giftCode, String giftName, String filePath) {
        Id = id;
        GiftCode = giftCode;
        GiftName = giftName;
        FilePath = filePath;
    }

    public static void addGift(Realm realm, GiftEntity giftEntity, String path) {
        GiftModel giftModel = new GiftModel(giftEntity.getId(), giftEntity.getGiftCode(),
                giftEntity.getGiftName(), path);
        giftModel = realm.copyToRealm(giftModel);
        RealmResults<CustomerGiftModel> results = realm.where(CustomerGiftModel.class).equalTo("GiftID", giftModel.getId()).findAll();
        for (CustomerGiftModel customerGiftModel : results) {
            customerGiftModel.setGiftModel(giftModel);
        }
    }

    public static void deleteGift(Realm realm) {
        RealmResults<GiftModel> results = realm.where(GiftModel.class).findAll();
        for (GiftModel giftModel : results) {
            File file = new File(giftModel.FilePath);
            file.delete();
        }
        results.deleteAllFromRealm();
    }


    public int getId() {
        return Id;
    }

    public String getGiftCode() {
        return GiftCode;
    }

    public String getGiftName() {
        return GiftName;
    }

    public String getFilePath() {
        return FilePath;
    }

    public static List<GiftModel> getListGiftByBrandIdAndSetId(Realm realm, int outletId, int brandId) {
        List<GiftModel> list = new ArrayList<>();

        //lấy brand hiện tại của outlet
        CurrentBrandModel currentBrandModel = realm.where(CurrentBrandModel.class)
                .equalTo("BrandID", brandId)
                .equalTo("IsUsed", true)
                .equalTo("OutletID", outletId).findFirst();
        //lấy ds quà hiện tại của brand set
        RealmResults<BrandSetDetailModel> results = realm.where(BrandSetDetailModel.class).equalTo("BrandSetID", currentBrandModel.getBrandSetID()).findAll();
        for (BrandSetDetailModel brandSetDetailModel : results) {
            GiftModel giftModel = realm.where(GiftModel.class).equalTo("Id", brandSetDetailModel.getGiftID()).findFirst();
            list.add(giftModel);
        }
        return list;
    }
}
