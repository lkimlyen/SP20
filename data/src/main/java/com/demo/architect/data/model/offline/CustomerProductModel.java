package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.ConvertUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class CustomerProductModel extends RealmObject implements Serializable {
    @PrimaryKey
    private int Id;
    private int CusID;
    private int OutletID;

    @SerializedName("pProductID")
    @Expose
    private int ProductID;

    @SerializedName("pNumber")
    @Expose
    private int Number;

    @SerializedName("pCustomerID")
    @Expose
    private int CusIDServer;

    @SerializedName("pTeamOutletID")
    @Expose
    private int CreatedBy;
    private String CreatedDateTime;
    private int Status;

    public CustomerProductModel() {
    }

    public CustomerProductModel(int cusID, int outletID, int productID, int number, int createdBy) {
        CusID = cusID;
        OutletID = outletID;
        ProductID = productID;
        Number = number;
        CreatedBy = createdBy;
    }
    public CustomerProductModel(int outletID, int productID, int number, int createdBy) {
        OutletID = outletID;
        ProductID = productID;
        Number = number;
        CreatedBy = createdBy;
    }
    public CustomerProductModel(int id, int cusID,int outletID, int productID, int number, int createdBy, String createdDateTime, int status) {
        Id = id;
        CusID = cusID;
        OutletID = outletID;
        ProductID = productID;
        Number = number;
        CreatedBy = createdBy;
        CreatedDateTime = createdDateTime;
        Status = status;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(CustomerProductModel.class).max("Id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static void addCustomerProduct(Realm realm, List<CustomerProductModel> list, int cusID) {
        for (CustomerProductModel customerProductModel : list) {
            CustomerProductModel giftModel = new CustomerProductModel(id(realm) + 1, cusID,
                    customerProductModel.OutletID,
                    customerProductModel.ProductID, customerProductModel.Number, customerProductModel.CreatedBy,
                    ConvertUtils.getDateTimeCurrent(), Constants.WAITING_UPLOAD);
            realm.copyToRealmOrUpdate(giftModel);
        }

    }

    public int getId() {
        return Id;
    }

    public int getCusID() {
        return CusID;
    }

    public int getProductID() {
        return ProductID;
    }

    public int getNumber() {
        return Number;
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

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public void setNumber(int number) {
        Number = number;
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
    public int getOutletID(){
        return OutletID;
    }

    public static List<CustomerProductModel> getListCustomerProductWaitingUpload(Realm realm, int outletId) {
        RealmResults<CustomerProductModel> results = realm.where(CustomerProductModel.class)
                .equalTo("OutletID",outletId)
                .equalTo("Status",Constants.WAITING_UPLOAD).findAll();
        return realm.copyFromRealm(results);
    }

    public static void updateStatusCustomerProduct(Realm realm, int outletId) {
        RealmResults<CustomerProductModel> results = realm.where(CustomerProductModel.class)
                .equalTo("OutletID",outletId)
                .equalTo("Status",Constants.WAITING_UPLOAD).findAll();
        for (CustomerProductModel customerProductModel : results){
            customerProductModel.setStatus(Constants.UPLOADED);
        }
    }


    public static List<CustomerProductModel> getListProductByCustomer(Realm realm, int cusId) {
        RealmResults<CustomerProductModel> results = realm.where(CustomerProductModel.class)
                .equalTo("CusID",cusId)
                .equalTo("Status",Constants.WAITING_UPLOAD).findAll();
        return realm.copyFromRealm(results);
    }
}
