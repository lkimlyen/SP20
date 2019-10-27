package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.ConvertUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class NoteStockModel extends RealmObject {
    @PrimaryKey
    private long Id;
    @SerializedName("pOutletID")
    @Expose
    private int OutletID;
    private Date DateCreate;
    @SerializedName("pDeviceDateTime")
    @Expose
    private String CreatedDateTime;
    @SerializedName("pNumberType")
    @Expose
    private int NumberType;
    private int Status;

    public NoteStockModel() {
    }

    public NoteStockModel(long id, int outletID, Date dateCreate, String createdDateTime, int numberType, int status) {
        Id = id;
        OutletID = outletID;
        DateCreate = dateCreate;
        CreatedDateTime = createdDateTime;
        NumberType = numberType;
        Status = status;
    }

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(NoteStockModel.class).max("Id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static void addNote(Realm realm, int outletId, int type) {
        //lấy ngày hiện tại theo format dd/MM/yyyy
        Date dateCurrent = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());
        //lấy note hiện tại theo ngày và trạng thái chưa upload
        NoteStockModel note = realm.where(NoteStockModel.class).equalTo("OutletID", outletId)
                .greaterThanOrEqualTo("DateCreate", dateCurrent)
                .equalTo("Status", Constants.WAITING_UPLOAD).findFirst();

        //nếu note khác null  thì update lại type
        if (note != null) {
            note.setNumberType(type);
        } else {//note = null thì tạo mới
            note = new NoteStockModel(id(realm) + 1, outletId, ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort()), ConvertUtils.getDateTimeCurrent(),
                    type, Constants.WAITING_UPLOAD);
            realm.copyToRealm(note);
        }
    }

    public static NoteStockModel getNoteStockByDate(Realm realm, int outletId) {
        Date dateCurrent = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());
        RealmResults<NoteStockModel> results = realm.where(NoteStockModel.class).equalTo("OutletID", outletId)
                .equalTo("DateCreate", dateCurrent)
                .findAll();
        return (results.size() > 0) ? results.last() : null;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public int getOutletID() {
        return OutletID;
    }

    public void setOutletID(int outletID) {
        OutletID = outletID;
    }


    public Date getDateCreate() {
        return DateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        DateCreate = dateCreate;
    }

    public String getCreatedDateTime() {
        return CreatedDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        CreatedDateTime = createdDateTime;
    }

    public int getNumberType() {
        return NumberType;
    }

    public void setNumberType(int numberType) {
        NumberType = numberType;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public static NoteStockModel getNoteWaitingUpload(Realm realm, int outletId) {
        NoteStockModel results = realm.where(NoteStockModel.class).equalTo("OutletID", outletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findFirst();

        return results == null ? null : realm.copyFromRealm(results);
    }

    public static int updateStatusNote(Realm realm, int outletId) {
        NoteStockModel results = realm.where(NoteStockModel.class).equalTo("OutletID", outletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findFirst();

        results.setStatus(Constants.UPLOADED);

        return 1;
    }
}
