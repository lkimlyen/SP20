package com.demo.architect.data.model.offline;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class TotalChangeGiftModel extends RealmObject {
    @PrimaryKey
    private int Id;
    private int CustomerId;
    private int ProductId;
    private int BrandId;
    private int NumberChange;
    private boolean Finished;

    public TotalChangeGiftModel() {
    }

    public TotalChangeGiftModel(int customerId, int productId, int brandId, int numberChange) {
        CustomerId = customerId;
        ProductId = productId;
        BrandId = brandId;
        NumberChange = numberChange;
    }

    public TotalChangeGiftModel(int id, int customerId, int productId, int brandId, int numberChange, boolean finished) {
        Id = id;
        CustomerId = customerId;
        ProductId = productId;
        BrandId = brandId;
        NumberChange = numberChange;
        Finished = finished;
    }

    public static void create(Realm realm, List<TotalChangeGiftModel> list) {
        for (TotalChangeGiftModel item : list) {
            TotalChangeGiftModel totalChangeGiftModel = new TotalChangeGiftModel(id(realm) + 1, item.getCustomerId(), item.getProductId(),
                    item.getBrandId(), item.getNumberChange(), false);
            realm.copyToRealm(totalChangeGiftModel);
        }
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(TotalChangeGiftModel.class).max("Id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public int getId() {
        return Id;
    }

    public int getProductId() {
        return ProductId;
    }

    public int getNumberChange() {
        return NumberChange;
    }

    public boolean isFinished() {
        return Finished;
    }

    public int getBrandId() {
        return BrandId;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setFinished(boolean finished) {
        Finished = finished;
    }

    public static List<TotalChangeGiftModel> getListProductChooseGift(Realm realm, int customerId) {
        RealmResults<TotalChangeGiftModel> results = realm.where(TotalChangeGiftModel.class)
                .equalTo("CustomerId", customerId).findAll();
        return realm.copyFromRealm(results);

    }
}
