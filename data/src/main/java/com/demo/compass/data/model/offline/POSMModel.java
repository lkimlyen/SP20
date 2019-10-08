package com.demo.compass.data.model.offline;

import com.demo.compass.data.model.POSMEntity;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class POSMModel extends RealmObject{
    @PrimaryKey
    private int Id;
    private int BrandId;
    private int OutletType;
    private String POSMName;
    private String Description;
    private String Image;

    public POSMModel() {
    }

    public POSMModel(int id, int brandId, int outletType, String POSMName, String description, String image) {
        Id = id;
        BrandId = brandId;
        OutletType = outletType;
        this.POSMName = POSMName;
        Description = description;
        Image = image;
    }

    public static void addPOSM(Realm realm, POSMEntity posmEntity, String path) {
        POSMModel posmModel = new POSMModel(posmEntity.getId(), posmEntity.getBrandId(),
                posmEntity.getOutletType(),posmEntity.getPosmName(),posmEntity.getDescription(), path);
        realm.copyToRealm(posmModel);
    }

    public static void deletePOSM(Realm realm) {
        RealmResults<POSMModel> results = realm.where(POSMModel.class).findAll();
        for (POSMModel posmModel : results) {
            File file = new File(posmModel.Image);
            file.delete();
        }
        results.deleteAllFromRealm();
    }

    public int getId() {
        return Id;
    }

    public int getBrandId() {
        return BrandId;
    }

    public int getOutletType() {
        return OutletType;
    }

    public String getPOSMName() {
        return POSMName;
    }

    public String getDescription() {
        return Description;
    }

    public String getImage() {
        return Image;
    }
}
