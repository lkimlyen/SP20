package com.demo.compass.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.ConvertUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class POSMReportModel extends RealmObject {
    @PrimaryKey
    private long Id;

    @SerializedName("pOutletID")
    @Expose
    private int OutletID;

    @SerializedName("pPOSMID")
    @Expose
    private int POSMID;

    @SerializedName("pNumber")
    @Expose
    private int Number;

    @SerializedName("pTeamOutletID")
    @Expose
    private int CreatedBy;

    private Date DateCreate;
    private String CreatedDateTime;
    private int LastUpdatedBy;
    @SerializedName("pDeviceDateTime")
    @Expose
    private String LastUpdatedDateTime;
    private int Status;
    private int RowVersion;

    public POSMReportModel() {
    }

    public POSMReportModel(int outletID, int POSMID, int number, int createdBy) {
        OutletID = outletID;
        this.POSMID = POSMID;
        Number = number;
        CreatedBy = createdBy;
    }

    public POSMReportModel(long id, int outletID, int POSMID, int number, int createdBy, Date dateCreate, String createdDateTime, String lastUpdatedDateTime, int status, int rowVersion) {
        Id = id;
        OutletID = outletID;
        this.POSMID = POSMID;
        Number = number;
        CreatedBy = createdBy;
        DateCreate = dateCreate;
        CreatedDateTime = createdDateTime;
        LastUpdatedDateTime = lastUpdatedDateTime;
        Status = status;
        RowVersion = rowVersion;
    }

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(POSMReportModel.class).max("Id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static void addPOSMReport(Realm realm, List<POSMReportModel> list) {
        Date dateCurrent = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());
        for (POSMReportModel item : list) {
            POSMReportModel posmReportModel = realm.where(POSMReportModel.class).equalTo("OutletID", item.OutletID)
                    .equalTo("POSMID", item.POSMID)
                    .greaterThanOrEqualTo("DateCreate", dateCurrent)
                    .equalTo("Status", Constants.WAITING_UPLOAD).findFirst();
            if (posmReportModel != null) {
                posmReportModel.setNumber(item.Number);
                posmReportModel.setLastUpdatedBy(item.LastUpdatedBy);
                posmReportModel.setLastUpdatedDateTime(ConvertUtils.getDateTimeCurrent());
                posmReportModel.setRowVersion(posmReportModel.RowVersion + 1);
            } else {
                posmReportModel = new POSMReportModel(id(realm) + 1, item.OutletID,
                        item.POSMID, item.Number, item.CreatedBy, dateCurrent, ConvertUtils.getDateTimeCurrent(),
                        ConvertUtils.getDateTimeCurrent(), Constants.WAITING_UPLOAD, 0);
                realm.copyToRealm(posmReportModel);
            }
        }
    }

    public static LinkedHashMap<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>> getPOSMByDate(Realm realm, int ouletId) {
        LinkedHashMap<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>> brandModelLinkedHashMapLinkedHashMap = new LinkedHashMap<>();
        //lấy tất cả ds brand
        RealmResults<BrandModel> brandModels = realm.where(BrandModel.class).findAll();
        Date dateCurrent = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());
        for (BrandModel brandModel : brandModels) {
            //lấy ds posm theo brand id
            RealmResults<POSMModel> posmModels = realm.where(POSMModel.class).equalTo("BrandId", brandModel.getId()).findAll();
            LinkedHashMap<POSMModel, POSMReportModel> list = new LinkedHashMap<>();
            for (POSMModel posmModel : posmModels) {
                //tim ds report posm theo posm id trong ngày
                RealmResults<POSMReportModel> results = realm.where(POSMReportModel.class).equalTo("OutletID", ouletId).equalTo("POSMID", posmModel.getId())
                        .greaterThanOrEqualTo("DateCreate", dateCurrent)
                        .findAll();
//kiểm tra xem list kết quả trả về có giá trị hay ko nếu có lấy giá trị cuối cùng
                if (results.size() > 0) {
                    list.put(posmModel, results.last());
                } else {
                    list.put(posmModel, null);
                }
            }
            if (list.size() > 0) {
                brandModelLinkedHashMapLinkedHashMap.put(brandModel, list);
            }

        }
        return brandModelLinkedHashMapLinkedHashMap;
    }


    public static List<POSMReportModel> getListPOSMReportWaitingUpload(Realm realm, int outletId) {
        RealmResults<POSMReportModel> results = realm.where(POSMReportModel.class).equalTo("OutletID", outletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        return realm.copyFromRealm(results);
    }

    public static int updateStatusPOSMReport(Realm realm, int outletId) {
        RealmResults<POSMReportModel> results = realm.where(POSMReportModel.class).equalTo("OutletID", outletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        for (POSMReportModel posmReportModel : results) {
            posmReportModel.setStatus(Constants.UPLOADED);
        }
        return results.size();
    }

    public long getId() {
        return Id;
    }

    public int getOutletID() {
        return OutletID;
    }

    public int getPOSMID() {
        return POSMID;
    }

    public int getNumber() {
        return Number;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public Date getDateCreate() {
        return DateCreate;
    }

    public String getCreatedDateTime() {
        return CreatedDateTime;
    }

    public int getLastUpdatedBy() {
        return LastUpdatedBy;
    }

    public String getLastUpdatedDateTime() {
        return LastUpdatedDateTime;
    }

    public int getStatus() {
        return Status;
    }

    public int getRowVersion() {
        return RowVersion;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public void setLastUpdatedBy(int lastUpdatedBy) {
        LastUpdatedBy = lastUpdatedBy;
    }

    public void setLastUpdatedDateTime(String lastUpdatedDateTime) {
        LastUpdatedDateTime = lastUpdatedDateTime;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public void setRowVersion(int rowVersion) {
        RowVersion = rowVersion;
    }
}
