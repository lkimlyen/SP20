package com.demo.compass.data.model.offline;

import com.demo.architect.data.helper.Constants;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class EmergencyModel extends RealmObject {
    @PrimaryKey
    private int Id;
    private String EmergencyCode;
    private int ReasonId;
    private String EmployeeId;
    private int CreatedBy;
    private int OutletId;
    private String StartDateTime;
    private ReasonModel reasonModel;
    private int State;
    private int EndDateTime;

    public EmergencyModel() {
    }

    public EmergencyModel(int id, String emergencyCode, int reasonId, String employeeId, int createdBy, int outletId, String startDateTime, int state) {
        Id = id;
        EmergencyCode = emergencyCode;
        ReasonId = reasonId;
        EmployeeId = employeeId;
        CreatedBy = createdBy;
        OutletId = outletId;
        StartDateTime = startDateTime;
        State = state;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getEmergencyCode() {
        return EmergencyCode;
    }

    public void setEmergencyCode(String emergencyCode) {
        EmergencyCode = emergencyCode;
    }

    public int getReasonId() {
        return ReasonId;
    }

    public void setReasonId(int reasonId) {
        ReasonId = reasonId;
    }

    public String getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        EmployeeId = employeeId;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public int getOutletId() {
        return OutletId;
    }

    public void setOutletId(int outletId) {
        OutletId = outletId;
    }

    public String getStartDateTime() {
        return StartDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        StartDateTime = startDateTime;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public int getEndDateTime() {
        return EndDateTime;
    }

    public void setEndDateTime(int endDateTime) {
        EndDateTime = endDateTime;
    }

    public static void addEmergency(Realm realm, EmergencyModel emergencyModel) {
        //lấy lý do theo id
        ReasonModel reasonModel = realm.where(ReasonModel.class).equalTo("Id",emergencyModel.ReasonId).findFirst();
        //tạo mới báo cáo khẩn cấp
        emergencyModel = realm.copyToRealm(emergencyModel);
        //update lý do model vào báo cáo khẩn cấp vừa tạo
        emergencyModel.setReasonModel(reasonModel);
    }

    public static RealmResults<EmergencyModel> getEmergencyNotEnd(Realm realm, int createdBy) {
        //lấy ds trạng thái chưa hoàn thành
        RealmResults<EmergencyModel> results = realm.where(EmergencyModel.class)
                .equalTo("CreatedBy", createdBy)
                .equalTo("State", Constants.NOT_FINISHED).findAll();
        return results;
    }

    public static void updateStateEmergency(Realm realm, int emergencyId) {
        EmergencyModel emergencyModel = realm.where(EmergencyModel.class).equalTo("Id", emergencyId)
                .findFirst();
        emergencyModel.setState(Constants.FINISHED);
    }


    public ReasonModel getReasonModel() {
        return reasonModel;
    }

    public void setReasonModel(ReasonModel reasonModel) {
        this.reasonModel = reasonModel;
    }
}
