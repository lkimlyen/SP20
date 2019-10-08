package com.demo.compass.data.model.offline;

import com.demo.compass.data.helper.Constants;
import com.felix.kotline.utils.ConvertUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class CustomerGiftModel extends RealmObject {
    @PrimaryKey
    private int Id;
    private int CusId;
    private int OutletID;

    @SerializedName("pGiftID")
    @Expose
    private int GiftID;

    @SerializedName("pNumberGift")
    @Expose
    private int NumberGift;

    @SerializedName("pCustomerID")
    @Expose
    private int CusIDServer;

    private GiftModel giftModel;
    @SerializedName("pTeamOutletID")
    @Expose
    private int CreatedBy;
    private String CreatedDateTime;
    private int Status;

    public CustomerGiftModel() {
    }

    public CustomerGiftModel(int cusId, int outletID, int giftID, int numberGift, int createdBy) {
        CusId = cusId;
        OutletID = outletID;
        GiftID = giftID;
        NumberGift = numberGift;
        CreatedBy = createdBy;
    }

    public CustomerGiftModel(int id, int cusId, int outletID, int GiftID, int numberGift, int createdBy, String createdDateTime, int status) {
        Id = id;
        CusId = cusId;
        OutletID = outletID;
        this.GiftID = GiftID;
        this.NumberGift = numberGift;
        CreatedBy = createdBy;
        CreatedDateTime = createdDateTime;
        Status = status;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(CustomerGiftModel.class).max("Id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static void addCustomerGift(Realm realm, List<CustomerGiftModel> list, int customerId) {
        for (CustomerGiftModel customerGiftModel : list) {
            CustomerGiftModel customerGiftModel1 = new CustomerGiftModel(id(realm) + 1, customerGiftModel.CusId,
                    customerGiftModel.OutletID, customerGiftModel.GiftID, customerGiftModel.NumberGift,
                    customerGiftModel.CreatedBy, ConvertUtils.getDateTimeCurrent(), Constants.WAITING_UPLOAD);
            customerGiftModel1 = realm.copyToRealm(customerGiftModel1);

            GiftModel giftModel = realm.where(GiftModel.class).equalTo("Id", customerGiftModel.GiftID).findFirst();
            customerGiftModel1.setGiftModel(giftModel);

        }

        CustomerModel customerModel = realm.where(CustomerModel.class).equalTo("Id", customerId).findFirst();
        customerModel.setFinishedSP(true);

        TotalChangeGiftModel totalChangeGiftModel = realm.where(TotalChangeGiftModel.class).equalTo("CustomerId", customerId).findFirst();
        totalChangeGiftModel.setFinished(true);
    }

    public int getId() {
        return Id;
    }

    public int getCusId() {
        return CusId;
    }

    public int getGiftID() {
        return GiftID;
    }

    public int getNumberGift() {
        return NumberGift;
    }

    public int getCusIDServer() {
        return CusIDServer;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public String getCreatedDateTime() {
        return CreatedDateTime;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setCusId(int cusId) {
        CusId = cusId;
    }

    public void setGiftID(int giftID) {
        GiftID = giftID;
    }

    public int getOutletID() {
        return OutletID;
    }

    public int getStatus() {
        return Status;
    }

    public void setNumberGift(int numberGift) {
        NumberGift = numberGift;
    }

    public void setCusIDServer(int cusIDServer) {
        CusIDServer = cusIDServer;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public void setCreatedDateTime(String createdDateTime) {
        CreatedDateTime = createdDateTime;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public GiftModel getGiftModel() {
        return giftModel;
    }

    public void setGiftModel(GiftModel giftModel) {
        this.giftModel = giftModel;
    }

    public static List<CustomerGiftModel> getListCustomerGiftWaitingUpload(Realm realm, int outletId) {
        RealmResults<CustomerGiftModel> results = realm.where(CustomerGiftModel.class)
                .equalTo("OutletID", outletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        return realm.copyFromRealm(results);

    }

    public static void updateStatusCustomerGift(Realm realm, int outletId) {
        RealmResults<CustomerGiftModel> results = realm.where(CustomerGiftModel.class)
                .equalTo("OutletID", outletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        for (CustomerGiftModel giftModel : results) {
            giftModel.setStatus(Constants.UPLOADED);
        }
    }


    public static List<CustomerGiftModel> getGiftByCustomerId(Realm realm, int customerId) {

        RealmResults<CustomerGiftModel> results = realm.where(CustomerGiftModel.class).equalTo("CusId", customerId).findAll();
        return realm.copyFromRealm(results);
    }
}
