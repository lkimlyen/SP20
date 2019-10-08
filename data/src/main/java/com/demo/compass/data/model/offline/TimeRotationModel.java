package com.demo.compass.data.model.offline;

import java.sql.Time;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TimeRotationModel extends RealmObject {
    @PrimaryKey
    private int Id;
    private Date DateStart;
    private Date DateEnd;
    private String ImageRotation;
    private RealmList<GiftMegaModel> giftList;

    public TimeRotationModel() {
    }

    public TimeRotationModel(int id, Date dateStart, Date dateEnd) {
        Id = id;
        DateStart = dateStart;
        DateEnd = dateEnd;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setDateStart(Date dateStart) {
        DateStart = dateStart;
    }

    public void setDateEnd(Date dateEnd) {
        DateEnd = dateEnd;
    }

    public void setGiftList(RealmList<GiftMegaModel> giftList) {
        this.giftList = giftList;
    }

    public int getId() {
        return Id;
    }

    public Date getDateStart() {
        return DateStart;
    }

    public Date getDateEnd() {
        return DateEnd;
    }

    public void setImageRotation(String imageRotation) {
        ImageRotation = imageRotation;
    }

    public RealmList<GiftMegaModel> getGiftList() {
        return giftList;
    }

    public String getImageRotation() {
        return ImageRotation;
    }

    public static void addImageRotation(Realm realm, int id, String path) {
        TimeRotationModel timeRotationModel = realm.where(TimeRotationModel.class)
                .equalTo("Id",id).findFirst();
        timeRotationModel.setImageRotation(path);
    }
}
