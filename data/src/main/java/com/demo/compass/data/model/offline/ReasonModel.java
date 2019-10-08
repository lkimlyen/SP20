package com.demo.compass.data.model.offline;

import com.demo.compass.data.model.ReasonEntity;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class ReasonModel extends RealmObject {
    @PrimaryKey
    private int Id;

    private String EmergencyCode;

    private String EmergencyName;

    private String EmergencyDescription;

    public ReasonModel() {
    }

    public ReasonModel(int id, String emergencyCode, String emergencyName, String emergencyDescription) {
        Id = id;
        EmergencyCode = emergencyCode;
        EmergencyName = emergencyName;
        EmergencyDescription = emergencyDescription;
    }

    public int getId() {
        return Id;
    }

    public String getEmergencyCode() {
        return EmergencyCode;
    }

    public String getEmergencyName() {
        return EmergencyName;
    }

    public String getEmergencyDescription() {
        return EmergencyDescription;
    }

    public static void addReason(Realm realm, List<ReasonEntity> list) {
        RealmResults<ReasonModel> results = realm.where(ReasonModel.class).findAll();
        results.deleteAllFromRealm();

        for (ReasonEntity reasonEntity : list) {
            ReasonModel reasonModel = new ReasonModel(reasonEntity.getId(), reasonEntity.getEmergencyCode(),
                    reasonEntity.getEmergencyName(), reasonEntity.getEmergencyDescription());
            realm.copyToRealm(reasonModel);
        }
    }

    public static List<ReasonModel> getListReason(Realm realm) {
        RealmResults<ReasonModel> results = realm.where(ReasonModel.class).findAll();
        return realm.copyFromRealm(results);
    }
}
