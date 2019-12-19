package com.demo.architect.data.model.offline;

import com.demo.architect.utils.view.ConvertUtils;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TotalTopupModel extends RealmObject {
    @PrimaryKey
    private int Id;
    private int CustomerId;
    private String Phone;
    private int BrandId;
    private int NumberTotal;
    private int NumberSend;
    private Date DateCreate = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());
    private boolean Finished;

    public TotalTopupModel() {
    }

    public TotalTopupModel(int customerId, int brandId, int numberTotal) {
        CustomerId = customerId;
        BrandId = brandId;
        NumberTotal = numberTotal;
    }
    public TotalTopupModel(int brandId, String phone,int numberTotal) {
        BrandId = brandId;
        Phone = phone;
        NumberTotal = numberTotal;
    }
    public TotalTopupModel(int id, int customerId, int brandId, int numberTotal,  boolean finished) {
        Id = id;
        CustomerId = customerId;
        BrandId = brandId;
        NumberTotal = numberTotal;
        Finished = finished;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(TotalTopupModel.class).max("Id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static void create(Realm realm,TotalTopupModel totalTopupModel) {


            realm.copyToRealm(totalTopupModel);


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

    public boolean isFinished() {
        return Finished;
    }

    public void setFinished(boolean finished) {
        Finished = finished;
    }

//    public static void updateNumberTurnedAndSaveGift(Realm realm, int id, CustomerGiftModel model, final List<CurrentGift> currentGiftList, final CurrentBrandModel currentBrandModel, boolean finish) {
//        CustomerGiftModel customerGiftModel = realm.where(CustomerGiftModel.class).equalTo("CusId", model.getCusId())
//                .equalTo("GiftID", model.getGiftID()).findFirst();
//        if (customerGiftModel != null) {
//            customerGiftModel.setNumberGift(customerGiftModel.getNumberGift() + 1);
//        } else {
//            customerGiftModel = new CustomerGiftModel(CustomerGiftModel.id(realm) + 1, model.getCusId(),
//                    model.getOutletID(), model.getGiftID(), model.getNumberGift(),
//                    model.getCreatedBy(), ConvertUtils.getDateTimeCurrent(), Constants.WAITING_UPLOAD);
//            customerGiftModel = realm.copyToRealm(customerGiftModel);
//
//            GiftModel giftModel = realm.where(GiftModel.class).equalTo("Id", customerGiftModel.getGiftID()).findFirst();
//            customerGiftModel.setGiftModel(giftModel);
//        }
//        TotalTopupModel totalRotationBrandModel = realm.where(TotalTopupModel.class)
//                .equalTo("Id", id).findFirst();
//        totalRotationBrandModel.setNumberTurned(totalRotationBrandModel.getNumberTurned() + 1);
//        if (totalRotationBrandModel.getNumberTurned() == totalRotationBrandModel.NumberTotal) {
//            totalRotationBrandModel.setFinished(true);
//        }
//        CurrentGiftModel.addOrUpdateCurrentGift(realm, currentGiftList, model.getCreatedBy());
//        CurrentBrandModel.updateNumberCurrentBrand(realm, currentBrandModel);
//
//        if (finish) {
//            CustomerModel customerModel = realm.where(CustomerModel.class).equalTo("Id", totalRotationBrandModel.getCustomerId()).findFirst();
//            customerModel.setFinishedSP(true);
//        }
//    }


    public String getPhone() {
        return Phone;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setNumberSend(int numberSend) {
        NumberSend = numberSend;
    }

    public int getNumberSend() {
        return NumberSend;
    }
}
