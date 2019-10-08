package com.demo.compass.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.ConvertUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class DetailCurrentGiftModel extends RealmObject {
    @PrimaryKey
    private int Id;
    private int OutletID;
    private int GiftID;
    private int Number;
    private Date DateCreate;
    private int CreatedBy;

    public DetailCurrentGiftModel() {
    }

    public DetailCurrentGiftModel(int outletID, int giftID, int number, int createdBy) {
        OutletID = outletID;
        GiftID = giftID;
        Number = number;
        CreatedBy = createdBy;
    }

    public DetailCurrentGiftModel(int id, int outletID, int giftID, int number, Date dateCreate, int createdBy) {
        Id = id;
        OutletID = outletID;
        GiftID = giftID;
        Number = number;
        DateCreate = dateCreate;
        CreatedBy = createdBy;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(DetailCurrentGiftModel.class).max("Id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static void addDetailCurrentGift(Realm realm, DetailCurrentGiftModel model) {

        DetailCurrentGiftModel detailCurrentGiftModel = realm.where(DetailCurrentGiftModel.class)
                .equalTo("OutletID", model.OutletID).equalTo("GiftID", model.GiftID)
                .greaterThanOrEqualTo("DateCreate", ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort()))
                .findFirst();
        if (detailCurrentGiftModel != null) {
            detailCurrentGiftModel.setNumber(detailCurrentGiftModel.Number + model.Number);
        } else {
            detailCurrentGiftModel = new DetailCurrentGiftModel(id(realm) + 1, model.OutletID, model.GiftID, model.Number,
                   ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort()), model.CreatedBy);
            realm.copyToRealm(detailCurrentGiftModel);
        }
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

    public int getNumber() {
        return Number;
    }

    public Date getDateCreate() {
        return DateCreate;
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

    public void setNumber(int number) {
        Number = number;
    }

    public void setDateCreate(Date dateCreate) {
        DateCreate = dateCreate;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public static LinkedHashMap<GiftModel, DetailCurrentGiftModel> getListGiftChangeByDate(Realm realm, int outletID,
                                                                                           List<Integer> brandIdList) {
        LinkedHashMap<GiftModel, DetailCurrentGiftModel> list = new LinkedHashMap<>();
        RealmList<BrandSetDetailModel> brandSetDetailList = new RealmList<>();
        RealmList<GiftModel> giftList = new RealmList<>();

        for (Integer brandId : brandIdList) {
            BrandModel brandModel = realm.where(BrandModel.class).equalTo("id", brandId).findFirst();
            if (!brandModel.isDialLucky()) {
                RealmList<ProductModel> realmList = brandModel.getProductList();
                for (ProductModel productModel : realmList) {
                    RealmResults<ProductGiftModel> results = realm.where(ProductGiftModel.class).equalTo("ProductID", productModel.getId()).findAll();
                    for (ProductGiftModel productGiftModel : results) {
                        GiftModel giftModel = realm.where(GiftModel.class).equalTo("Id", productGiftModel.getGiftID()).findFirst();
                        giftList.add(giftModel);
                    }
                }
            }else {
                CurrentBrandModel currentBrandModel = realm.where(CurrentBrandModel.class)
                        .equalTo("BrandID",brandId)
                        .equalTo("IsUsed",true)
                        .equalTo("OutletID",outletID).findFirst();
                RealmResults<BrandSetDetailModel> results = realm.where(BrandSetDetailModel.class).equalTo("BrandSetID", currentBrandModel.getBrandSetID()).findAll();
                for (BrandSetDetailModel brandSetDetailModel : results) {
                    GiftModel giftModel = realm.where(GiftModel.class).equalTo("Id", brandSetDetailModel.getGiftID()).findFirst();
                    giftList.add(giftModel);
                }

            }
        }
        for (GiftModel giftModel :  giftList){
            DetailCurrentGiftModel detailCurrentGiftModel =  realm.where(DetailCurrentGiftModel.class)
                    .equalTo("OutletID",outletID)
                    .equalTo("GiftID",giftModel.getId())
                    .greaterThanOrEqualTo("DateCreate",ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort()))
                    .findFirst();
            list.put(giftModel,detailCurrentGiftModel);
        }
        return list;
    }
}
