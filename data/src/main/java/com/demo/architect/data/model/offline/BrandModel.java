package com.demo.architect.data.model.offline;

import com.demo.architect.data.model.BrandEntitiy;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class BrandModel extends RealmObject {
    @PrimaryKey
    private int id;

    private String BrandCode;

    private String BrandName;

    private boolean IsDialLucky;

    private int MaximumChangeGift;
    private int NumberOfEnough;
    private int NumberGiftOfDay;

    @SuppressWarnings("unused")
    private RealmList<ProductModel> productList;

    public BrandModel() {
    }

    public BrandModel(int id, String brandCode, String brandName, boolean isDialLucky, int maximumChangeGift, int numberOfEnough, int numberGiftOfDay) {
        this.id = id;
        BrandCode = brandCode;
        BrandName = brandName;
        IsDialLucky = isDialLucky;
        MaximumChangeGift = maximumChangeGift;
        NumberOfEnough = numberOfEnough;
        NumberGiftOfDay = numberGiftOfDay;
    }

    public static void addBrand(Realm realm, List<BrandEntitiy> list) {
        RealmResults<BrandModel> results = realm.where(BrandModel.class).findAll();
        results.deleteAllFromRealm();
        for (BrandEntitiy item : list) {
            BrandModel brandModel = new BrandModel(item.getId(), item.getBrandCode(),
                    item.getBrandName(), item.isDialLucky(), item.getMaximumChangeGift(), item.getNumberOfEnough(), item.getNumberGiftOfDay());
            brandModel = realm.copyToRealmOrUpdate(brandModel);

            RealmList<ProductModel> realmList = brandModel.getProductList();
            RealmResults<ProductModel> productList = realm.where(ProductModel.class).equalTo("BrandID", brandModel.id).findAll();
            realmList.addAll(productList);
        }

    }

    public int getId() {
        return id;
    }

    public String getBrandCode() {
        return BrandCode;
    }

    public String getBrandName() {
        return BrandName;
    }

    public boolean isDialLucky() {
        return IsDialLucky;
    }

    public int getMaximumChangeGift() {
        return MaximumChangeGift;
    }

    public int getNumberOfEnough() {
        return NumberOfEnough;
    }

    public RealmList<ProductModel> getProductList() {
        return productList;
    }

    public int getNumberGiftOfDay() {
        return NumberGiftOfDay;
    }

    public static List<BrandModel> getListBrandById(Realm realm, List<Integer> idList) {
        List<BrandModel> list = new ArrayList<>();
        for (Integer id : idList) {
            BrandModel brandModel = realm.where(BrandModel.class).equalTo("id", id).findFirst();
            if (brandModel != null) {
                list.add(realm.copyFromRealm(brandModel));
            }
        }
        return list;
    }

    public static BrandModel getBrandCurrent(Realm realm, int brandId) {
        BrandModel brandModel = realm.where(BrandModel.class).equalTo("id", brandId).findFirst();
        return brandModel;
    }
}
