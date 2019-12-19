package com.demo.architect.data.model.offline;


import com.demo.architect.data.model.BrandSetEntity;

import java.util.LinkedHashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class BrandSetModel extends RealmObject {
    @PrimaryKey
    private int Id;
    private int OutletTypeID;
    private int BrandID;
    private String SetName;
    private int TotalGift;

    public BrandSetModel() {
    }

    public BrandSetModel(int id, int outletTypeID, int brandID, String setName, int totalGift) {
        Id = id;
        OutletTypeID = outletTypeID;
        BrandID = brandID;
        SetName = setName;
        TotalGift = totalGift;
    }

    public static void addBrandSet(Realm realm, List<BrandSetEntity> list) {
        RealmResults<BrandSetModel> results = realm.where(BrandSetModel.class).findAll();
        results.deleteAllFromRealm();
        for (BrandSetEntity item : list) {
            BrandSetModel brandSetModel = new BrandSetModel(item.getBrandSetId(),
                    item.getOutletTypeId(), item.getBrandId(), item.getSetName(), item.getTotalGift());
            realm.copyToRealmOrUpdate(brandSetModel);
        }
    }


    public int getId() {
        return Id;
    }

    public int getOutletTypeID() {
        return OutletTypeID;
    }

    public int getBrandID() {
        return BrandID;
    }

    public String getSetName() {
        return SetName;
    }

    public static List<BrandSetModel> getListBrandSetByBrandId(Realm realm, int brandId, int outletType) {
        RealmResults<BrandSetModel> results = realm.where(BrandSetModel.class)
                .equalTo("OutletTypeID", outletType)
                .equalTo("BrandID", brandId).findAll();
        return realm.copyFromRealm(results);
    }

    public static LinkedHashMap<Integer, LinkedHashMap<BrandModel, BrandSetModel>> getListBrandSetById(Realm realm) {
        LinkedHashMap<Integer, LinkedHashMap<BrandModel, BrandSetModel>> listResult = new LinkedHashMap<>();
        RealmResults<BrandSetModel> results = realm.where(BrandSetModel.class).findAll();
        for (BrandSetModel brandSetModel : results) {
            LinkedHashMap<BrandModel, BrandSetModel> linkedHashMap = new LinkedHashMap<>();
            BrandModel brandModel = realm.where(BrandModel.class).equalTo("id", brandSetModel.getBrandID()).findFirst();
            linkedHashMap.put(brandModel, brandSetModel);
            listResult.put(brandSetModel.getId(), linkedHashMap);
        }
        return listResult;
    }


    public int getTotalGift() {
        return TotalGift;
    }


}
