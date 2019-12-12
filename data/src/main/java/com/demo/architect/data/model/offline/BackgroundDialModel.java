package com.demo.architect.data.model.offline;


import com.demo.architect.data.model.BackgroundDialEntity;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class BackgroundDialModel extends RealmObject {
    @PrimaryKey
    private int id;

    private int ProjectID;
    private int BrandID;
    private String BGButton;
    private String BGRing;
    private String BGArrow;
    private String BGLayout;
    private String Rotation;
    private String ColorBorder;


    public BackgroundDialModel() {
    }

    public BackgroundDialModel(int id, int projectID, int brandID, String BGButton, String BGRing, String BGArrow, String bgLayout, String colorBorder) {
        this.id = id;
        ProjectID = projectID;
        BrandID = brandID;
        this.BGButton = BGButton;
        this.BGRing = BGRing;
        this.BGArrow = BGArrow;
        BGLayout = bgLayout;
        ColorBorder = colorBorder;
    }

    public static void addBackgroundDial(Realm realm, BackgroundDialEntity backgroundDialEntity, String pathArrow, String pathButton, String pathLayout, String pathCircle) {

        BackgroundDialModel backgroundDialModel = new BackgroundDialModel(backgroundDialEntity.getId(),
                backgroundDialEntity.getProjectId(), backgroundDialEntity.getBrandId(),
                pathButton, pathCircle, pathArrow, pathLayout,
                backgroundDialEntity.getColorBorder());
        realm.copyToRealmOrUpdate(backgroundDialModel);

    }

    public static void deleteBackgroundDial(Realm realm) {
        RealmResults<BackgroundDialModel> results = realm.where(BackgroundDialModel.class).findAll();
        if (results.size() > 0) {
            for (BackgroundDialModel backgroundDialModel : results) {
                File fileArrow = new File(backgroundDialModel.BGArrow);
                fileArrow.delete();

                File fileButton = new File(backgroundDialModel.BGButton);
                fileButton.delete();

                File fileRing = new File(backgroundDialModel.BGRing);
                fileRing.delete();

                File fileLayout = new File(backgroundDialModel.BGLayout);
                fileLayout.delete();
            }

            results.deleteAllFromRealm();
        }

    }


    public int getId() {
        return id;
    }

    public int getProjectID() {
        return ProjectID;
    }

    public int getBrandID() {
        return BrandID;
    }

    public String getBGButton() {
        return BGButton;
    }

    public String getBGRing() {
        return BGRing;
    }

    public String getBGArrow() {
        return BGArrow;
    }

    public String getColorBorder() {
        return ColorBorder;
    }

    public String getBGLayout() {
        return BGLayout;
    }

    public String getRotation() {
        return Rotation;
    }

    public void setRotation(String rotation) {
        Rotation = rotation;
    }

    public static BackgroundDialModel getBackgroundDialByBrandId(Realm realm, int brandId) {
        BackgroundDialModel backgroundDialModel = realm.where(BackgroundDialModel.class).equalTo("BrandID", brandId).findFirst();
        return backgroundDialModel;
    }

    public static void addRotation(Realm realm, int brandId,String path){
        BackgroundDialModel backgroundDialModel = realm.where(BackgroundDialModel.class).equalTo("BrandID", brandId).findFirst();
        backgroundDialModel.setRotation(path);

    }
}
