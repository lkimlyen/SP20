package com.demo.architect.data.model.offline;

import com.demo.architect.data.model.GiftEntity;

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
    private boolean IsTopupCard;
    public GiftModel() {
    }

    public GiftModel(int id, String giftCode, String giftName, String filePath, boolean isTopupCard) {
        Id = id;
        GiftCode = giftCode;
        GiftName = giftName;
        FilePath = filePath;
        IsTopupCard = isTopupCard;
    }

    public static void addGift(Realm realm, GiftEntity giftEntity, String path) {
        GiftModel giftModel = new GiftModel(giftEntity.getId(), giftEntity.getGiftCode(),
                giftEntity.getGiftName(), path, giftEntity.isTopupCard());
        giftModel = realm.copyToRealmOrUpdate(giftModel);
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

    public boolean isTopupCard() {
        return IsTopupCard;
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
