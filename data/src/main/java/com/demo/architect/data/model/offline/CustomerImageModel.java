package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.ConvertUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class CustomerImageModel extends RealmObject {
    @PrimaryKey
    private int Id;
    private int CusID;
    private int OutletID;
    private int ImageID;
    @SerializedName("pImageID")
    @Expose
    private int ImageIDServer;

    @SerializedName("pCustomerID")
    @Expose
    private int CusIDServer;

    @SerializedName("pTeamOutletID")
    @Expose
    private int CreatedBy;
    private String CreatedDateTime;
    private int Status;

    public CustomerImageModel() {
    }

    public CustomerImageModel(int cusID, int outletID, int imageID, int createdBy) {
        CusID = cusID;
        OutletID = outletID;
        ImageID = imageID;
        CreatedBy = createdBy;
    }

    public CustomerImageModel(int id, int cusID, int outletID, int imageID, int createdBy, String createdDateTime, int status) {
        Id = id;
        CusID = cusID;
        OutletID = outletID;
        ImageID = imageID;
        CreatedBy = createdBy;
        CreatedDateTime = createdDateTime;
        Status = status;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(CustomerImageModel.class).max("Id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static void addCustomerImage(Realm realm, List<CustomerImageModel> list) {
        for (CustomerImageModel customerImageModel : list) {
            CustomerImageModel giftModel = new CustomerImageModel(id(realm) + 1, customerImageModel.CusID,
                    customerImageModel.OutletID, customerImageModel.ImageID, customerImageModel.CreatedBy,
                    ConvertUtils.getDateTimeCurrent(), Constants.WAITING_UPLOAD);
            realm.copyToRealm(giftModel);
        }

    }

    public int getId() {
        return Id;
    }

    public int getCusID() {
        return CusID;
    }

    public int getImageID() {
        return ImageID;
    }

    public int getImageIDServer() {
        return ImageIDServer;
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

    public void setCusID(int cusID) {
        CusID = cusID;
    }

    public void setImageID(int imageID) {
        ImageID = imageID;
    }

    public void setImageIDServer(int imageIDServer) {
        ImageIDServer = imageIDServer;
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

    public static List<CustomerImageModel> getListCustomerImageWaitingUpload(Realm realm, int outletId) {
        RealmResults<CustomerImageModel> results = realm.where(CustomerImageModel.class)
                .equalTo("OutletID", outletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        return realm.copyFromRealm(results);
    }

    public static int updateStatusCustomerImage(Realm realm, int outletId) {
        RealmResults<CustomerImageModel> results = realm.where(CustomerImageModel.class)
                .equalTo("OutletID", outletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        Set<CustomerModel> customerModelSet = new HashSet<>();
        for (CustomerImageModel customerImageModel : results) {
            customerImageModel.setStatus(Constants.UPLOADED);
            CustomerModel customerModel = realm.where(CustomerModel.class).equalTo("Id", customerImageModel.CusID).findFirst();
            customerModel.setStatus(Constants.UPLOADED);
            customerModelSet.add(customerModel);
        }

        return customerModelSet.size();
    }

    public static List<CustomerImageModel> getListImageByCustomer(Realm realm, int cusId) {
        RealmResults<CustomerImageModel> results = realm.where(CustomerImageModel.class)
                .equalTo("CusID", cusId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        return realm.copyFromRealm(results);
    }
}
