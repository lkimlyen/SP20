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

public class TotalRotationModel extends RealmObject {
    @PrimaryKey
    private int Id;

    private int CustomerId;

    @SerializedName("pCustomerID")
    @Expose
    private int CusServerId;

    @SerializedName("pNumber")
    @Expose
    private int Number;

    private int NumberTurned;

    @SerializedName("pTeamOutletID")
    @Expose
    private int CreatedBy;

    private String CreatedDateTime;

    private int Status;

    public TotalRotationModel() {
    }

    public TotalRotationModel(int id, int customerId, int number, int numberTurned, int createdBy, String createdDateTime, int status) {
        Id = id;
        this.CustomerId = customerId;
        this.Number = number;
        NumberTurned = numberTurned;
        CreatedBy = createdBy;
        CreatedDateTime = createdDateTime;
        Status = status;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(TotalRotationModel.class).max("Id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static void addTotalRotaion(Realm realm, int customerId, int number, int createdBy) {
        TotalRotationModel totalRotationModel = new TotalRotationModel(id(realm) + 1, customerId, number, 0, createdBy, ConvertUtils.getDateTimeCurrent(), Constants.WAITING_UPLOAD);
        realm.copyToRealm(totalRotationModel);

    }

    public static int updateStatusRotation(Realm realm) {
        RealmResults<TotalRotationModel> results = realm.where(TotalRotationModel.class)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        for (TotalRotationModel totalRotationModel : results) {
            totalRotationModel.setStatus(Constants.UPLOADED);
        }

        return results.size();
    }

    public static List<TotalRotationModel> getListTotalRotationWaitingUpload(Realm realm) {
        RealmResults<TotalRotationModel> results = realm.where(TotalRotationModel.class)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        return realm.copyFromRealm(results);
    }

    public int getId() {
        return Id;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public int getNumber() {
        return Number;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCusServerId(int cusServerId) {
        CusServerId = cusServerId;
    }

    public int getNumberTurned() {
        return NumberTurned;
    }

    public void setNumberTurned(int numberTurned) {
        NumberTurned = numberTurned;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public static void saveTotalRotaionWithServerId(Realm realm, int cusId, int cusIDServer, int totalRotation, int teamOutletId) {
        TotalRotationModel totalRotationModel = new TotalRotationModel(id(realm) + 1, cusId, totalRotation, 0, teamOutletId, ConvertUtils.getDateTimeCurrent(), Constants.WAITING_UPLOAD);
        totalRotationModel = realm.copyToRealm(totalRotationModel);
        totalRotationModel.setCusServerId(cusIDServer);
    }

    public static void updateNumberTunedRotation(Realm realm, int customerId) {
        TotalRotationModel totalRotationModel = realm.where(TotalRotationModel.class).equalTo("CustomerId", customerId).findFirst();
        totalRotationModel.setNumberTurned(totalRotationModel.getNumberTurned() + 1);
        if (totalRotationModel.getNumber() == totalRotationModel.getNumberTurned()) {
            CustomerModel customerModel = realm.where(CustomerModel.class)
                    .equalTo("Id", customerId).findFirst();
            customerModel.setFinished(true);
        }
    }
}
