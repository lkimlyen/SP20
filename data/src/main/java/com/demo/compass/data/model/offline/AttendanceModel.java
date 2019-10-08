package com.demo.compass.data.model.offline;

import com.demo.architect.utils.view.ConvertUtils;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class AttendanceModel extends RealmObject {
    @PrimaryKey
    private int Id;
    private String Number;
    private double Latitude;
    private double Longitude;
    private String PathImage;
    private String Type;
    private Date DateCreateShort;
    private String ImageServerId;
    private String DateCreate;
    private String LastDateUpdate;
    private int CreateBy;

    public AttendanceModel() {
    }

    public AttendanceModel(int id, String number, double latitude, double longitude, String type, Date dateCreateShort, String dateCreate, int createBy) {
        this.Id = id;
        this.Number = number;
        this.Latitude = latitude;
        this.Longitude = longitude;
        Type = type;
        DateCreateShort = dateCreateShort;
        this.DateCreate = dateCreate;
        this.CreateBy = createBy;
    }

    public static void addAttendance(Realm realm, AttendanceModel model) {
        AttendanceModel attendanceModel = realm.where(AttendanceModel.class).equalTo("Id", model.Id).findFirst();
        if (attendanceModel == null) {
            realm.copyToRealm(model);
        } else {
            attendanceModel.setLastDateUpdate(ConvertUtils.getDateTimeCurrent());
            attendanceModel.setNumber(attendanceModel.Number + "," + model.Number);
        }
    }

    public static void addImageToAtten(Realm realm, int attendanceId, int imageId, String path) {
        AttendanceModel attendanceModel = realm.where(AttendanceModel.class).equalTo("Id", attendanceId).findFirst();
        attendanceModel.setPathImage(attendanceModel.PathImage != null && attendanceModel.PathImage.length() > 0 ?
                attendanceModel.PathImage + "," + path : path);
        attendanceModel.setImageServerId(attendanceModel.ImageServerId != null && attendanceModel.ImageServerId.length() > 0 ?
                attendanceModel.ImageServerId + "," + String.valueOf(imageId) : String.valueOf(imageId));
    }

    public void setId(int id) {
        Id = id;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public void setPathImage(String pathImage) {
        PathImage = pathImage;
    }

    public void setImageServerId(String imageServerId) {
        ImageServerId = imageServerId;
    }

    public void setDateCreate(String dateCreate) {
        DateCreate = dateCreate;
    }

    public void setLastDateUpdate(String lastDateUpdate) {
        LastDateUpdate = lastDateUpdate;
    }

    public void setCreateBy(int createBy) {
        CreateBy = createBy;
    }

    public String getPathImage() {
        return PathImage;
    }

    public static boolean checkUserCheckIn(Realm realm, int teamOutletId) {
        Date dateCurrent = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());
        AttendanceModel results = realm.where(AttendanceModel.class)
                .equalTo("CreateBy", teamOutletId)
                .equalTo("Type","IN_TIME")
                .equalTo("DateCreateShort", dateCurrent).findFirst();
        return results != null;
    }
}
