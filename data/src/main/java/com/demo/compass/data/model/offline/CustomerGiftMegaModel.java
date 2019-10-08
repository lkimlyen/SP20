package com.demo.compass.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.ConvertUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class CustomerGiftMegaModel extends RealmObject {
    @PrimaryKey
    private int Id;
    private int CusId;
    private int OutletID;

    @SerializedName("pGiftID")
    @Expose
    private int GiftID;

    @SerializedName("pCustomerID")
    @Expose
    private int CusIDServer;

    @SerializedName("pTeamOutletID")
    @Expose
    private int CreatedBy;
    private String CreatedDateTime;
    private int Status;

    public CustomerGiftMegaModel() {
    }

    public CustomerGiftMegaModel(int cusId, int outletID, int giftID, int createdBy) {
        CusId = cusId;
        OutletID = outletID;
        GiftID = giftID;
        CreatedBy = createdBy;
    }

    public CustomerGiftMegaModel(int id, int cusId, int outletID, int GiftID, int createdBy, String createdDateTime, int status) {
        Id = id;
        CusId = cusId;
        OutletID = outletID;
        this.GiftID = GiftID;
        CreatedBy = createdBy;
        CreatedDateTime = createdDateTime;
        Status = status;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(CustomerGiftMegaModel.class).max("Id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static void addCustomerGift(Realm realm, CustomerGiftMegaModel customerGiftMegaModel) {
        CustomerGiftMegaModel customerGiftModel1 = new CustomerGiftMegaModel(id(realm) + 1, customerGiftMegaModel.CusId,
                customerGiftMegaModel.OutletID, customerGiftMegaModel.GiftID,
                customerGiftMegaModel.CreatedBy, ConvertUtils.getDateTimeCurrent(), Constants.WAITING_UPLOAD);
        realm.copyToRealm(customerGiftModel1);


    }

    public int getId() {
        return Id;
    }

    public int getCusId() {
        return CusId;
    }

    public int getOutletID() {
        return OutletID;
    }

    public int getGiftID() {
        return GiftID;
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

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public void setCusIDServer(int cusIDServer) {
        CusIDServer = cusIDServer;
    }

    public static CustomerGiftMegaModel getCustomerGiftMega(Realm realm, int cusId) {
        CustomerGiftMegaModel customerGiftModel = realm.where(CustomerGiftMegaModel.class)
                .equalTo("CusId", cusId)
                .equalTo("Status", Constants.WAITING_UPLOAD)
                .findFirst();
        return realm.copyFromRealm(customerGiftModel);
    }

    public static void updateStatusCustomerGiftMega(Realm realm) {
        CustomerGiftMegaModel customerGiftModel = realm.where(CustomerGiftMegaModel.class)
                .equalTo("Status", Constants.WAITING_UPLOAD)
                .findFirst();
        customerGiftModel.setStatus(Constants.UPLOADED);
    }

    public static CustomerGiftMegaModel getCustomerGiftMegaWaitingUpload(Realm realm) {
        CustomerGiftMegaModel customerGiftModel = realm.where(CustomerGiftMegaModel.class)
                .equalTo("Status", Constants.WAITING_UPLOAD)
                .findFirst();
        return customerGiftModel != null ? realm.copyFromRealm(customerGiftModel):null;
    }
}
