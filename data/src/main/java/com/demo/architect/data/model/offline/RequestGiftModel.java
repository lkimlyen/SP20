package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class RequestGiftModel extends RealmObject {
    @PrimaryKey
    private int Id;
    private String Code;
    private int OutletID;
    private int CreatedBy;
    private String CreatedDateTime;
    private int State;
    @SuppressWarnings("unused")
    private RealmList<DetailRequestGiftModel> requestGiftList;

    public RequestGiftModel() {
    }

    public RequestGiftModel(int id, String code, int outletID, int createdBy, String createdDateTime, int state) {
        Id = id;
        Code = code;
        OutletID = outletID;
        CreatedBy = createdBy;
        CreatedDateTime = createdDateTime;
        State = state;
    }

    public void setState(int state) {
        State = state;
    }

    public int getId() {
        return Id;
    }

    public String getCode() {
        return Code;
    }

    public int getOutletID() {
        return OutletID;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public String getCreatedDateTime() {
        return CreatedDateTime;
    }

    public int getState() {
        return State;
    }

    public RealmList<DetailRequestGiftModel> getRequestGiftList() {
        return requestGiftList;
    }

    public void setRequestGiftList(RealmList<DetailRequestGiftModel> requestGiftList) {
        this.requestGiftList = requestGiftList;
    }

    public static void saveRequestGift(Realm realm, RequestGiftModel requestGiftModel, List<DetailRequestGiftModel> list) {
        requestGiftModel = realm.copyToRealm(requestGiftModel);
        RealmList<DetailRequestGiftModel> parentList = requestGiftModel.requestGiftList;
        for (DetailRequestGiftModel detailRequestGiftModel : list) {
            DetailRequestGiftModel detailRequestGiftModel1 = DetailRequestGiftModel.addRequestCode(realm, detailRequestGiftModel);
            parentList.add(detailRequestGiftModel1);
        }
    }

    public static RealmResults<RequestGiftModel> getListConfirmReciever(Realm realm, int outletID) {
        RealmResults<RequestGiftModel> requestGiftModels = realm.where(RequestGiftModel.class)
                .equalTo("OutletID", outletID).findAll();
        return requestGiftModels;
    }

    public static void updateStateRequest(Realm realm,int id) {
        RequestGiftModel requestGiftModel = realm.where(RequestGiftModel.class).equalTo("Id",id).findFirst();
        requestGiftModel.setState(Constants.CONFIRMED);
    }
}
