package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.CurrentGift;
import com.demo.architect.utils.view.ConvertUtils;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TotalRotationBrandModel extends RealmObject {
    @PrimaryKey
    private int Id;
    private int CustomerId;
    private int BrandId;
    private int NumberTotal;
    private int NumberTurned;
    private boolean Finished;

    public TotalRotationBrandModel() {
    }

    public TotalRotationBrandModel(int customerId, int brandId, int numberTotal) {
        CustomerId = customerId;
        BrandId = brandId;
        NumberTotal = numberTotal;
    }
    public TotalRotationBrandModel( int brandId, int numberTotal) {
        BrandId = brandId;
        NumberTotal = numberTotal;
    }
    public TotalRotationBrandModel(int id, int customerId, int brandId, int numberTotal, int numberTurned, boolean finished) {
        Id = id;
        CustomerId = customerId;
        BrandId = brandId;
        NumberTotal = numberTotal;
        NumberTurned = numberTurned;
        Finished = finished;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(TotalRotationBrandModel.class).max("Id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static void create(Realm realm, List<TotalRotationBrandModel> list) {
        for (TotalRotationBrandModel item : list) {
            TotalRotationBrandModel totalRotationBrandModel = new TotalRotationBrandModel(id(realm) + 1, item.getCustomerId(),
                    item.getBrandId(), item.getNumberTotal(), 0, false);
            realm.copyToRealm(totalRotationBrandModel);
        }

    }
    public static void create(Realm realm, List<TotalRotationBrandModel> list, int customerId) {
        for (TotalRotationBrandModel item : list) {
            TotalRotationBrandModel totalRotationBrandModel = new TotalRotationBrandModel(id(realm) + 1, customerId,
                    item.getBrandId(), item.getNumberTotal(), 0, false);
            realm.copyToRealm(totalRotationBrandModel);
        }

    }
    public int getId() {
        return Id;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public int getBrandId() {
        return BrandId;
    }

    public int getNumberTotal() {
        return NumberTotal;
    }

    public int getNumberTurned() {
        return NumberTurned;
    }

    public boolean isFinished() {
        return Finished;
    }

    public void setNumberTurned(int numberTurned) {
        NumberTurned = numberTurned;
    }

    public void setFinished(boolean finished) {
        Finished = finished;
    }

    public static void updateNumberTurnedAndSaveGift(Realm realm, int id, CustomerGiftModel model, final List<CurrentGift> currentGiftList, final CurrentBrandModel currentBrandModel, boolean finish) {
        CustomerGiftModel customerGiftModel = realm.where(CustomerGiftModel.class).equalTo("CusId", model.getCusId())
                .equalTo("GiftID", model.getGiftID()).findFirst();
        if (customerGiftModel != null) {
            customerGiftModel.setNumberGift(customerGiftModel.getNumberGift() + 1);
        } else {
            customerGiftModel = new CustomerGiftModel(CustomerGiftModel.id(realm) + 1, model.getCusId(),
                    model.getOutletID(), model.getGiftID(), model.getNumberGift(),
                    model.getCreatedBy(), ConvertUtils.getDateTimeCurrent(), Constants.WAITING_UPLOAD);
            customerGiftModel = realm.copyToRealm(customerGiftModel);

            GiftModel giftModel = realm.where(GiftModel.class).equalTo("Id", customerGiftModel.getGiftID()).findFirst();
            customerGiftModel.setGiftModel(giftModel);
        }
        TotalRotationBrandModel totalRotationBrandModel = realm.where(TotalRotationBrandModel.class)
                .equalTo("Id", id).findFirst();
        totalRotationBrandModel.setNumberTurned(totalRotationBrandModel.getNumberTurned() + 1);
        if (totalRotationBrandModel.getNumberTurned() == totalRotationBrandModel.NumberTotal) {
            totalRotationBrandModel.setFinished(true);
        }
        CurrentGiftModel.addOrUpdateCurrentGift(realm, currentGiftList, model.getCreatedBy());
        CurrentBrandModel.updateNumberCurrentBrand(realm, currentBrandModel);

        if (finish) {
            CustomerModel customerModel = realm.where(CustomerModel.class).equalTo("Id", totalRotationBrandModel.getCustomerId()).findFirst();
            customerModel.setFinishedSP(true);
        }
    }
}
