package com.demo.architect.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.ConvertUtils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class TakeOffVolumnModel extends RealmObject {
    @PrimaryKey
    private long Id;
    @SerializedName("pTakeOffVolumnCode")
    @Expose
    private String TakeOffVolumnCode;

    @SerializedName("pOutletID")
    @Expose
    private int OutletID;

    @SerializedName("pProductID")
    @Expose
    private int ProductID;

    @SerializedName("pNumber")
    @Expose
    private int NumberSP;

    @SerializedName("pTeamOutletID")
    @Expose
    private int CreatedBy;

    private Date DateCreate;
    private String CreatedDateTime;
    @SerializedName("pDeviceDateTime")
    @Expose
    private String LastUpdatedDateTime;
    private int Status;
    private int RowVersion;

    public TakeOffVolumnModel() {
    }

    public TakeOffVolumnModel(int outletID, int productID, int numberSP, int createdBy) {
        OutletID = outletID;
        ProductID = productID;
        NumberSP = numberSP;
        CreatedBy = createdBy;
    }

    public TakeOffVolumnModel(long id, String takeOffVolumnCode, int outletID, int productID, int numberSP, int createdBy, Date dateCreate, String createdDateTime, String lastUpdatedDateTime, int status, int rowVersion) {
        Id = id;
        TakeOffVolumnCode = takeOffVolumnCode;
        OutletID = outletID;
        ProductID = productID;
        NumberSP = numberSP;
        CreatedBy = createdBy;
        DateCreate = dateCreate;
        CreatedDateTime = createdDateTime;
        LastUpdatedDateTime = lastUpdatedDateTime;
        Status = status;
        RowVersion = rowVersion;
    }

    public static long id(Realm realm) {
        long nextId = 0;
        Number maxValue = realm.where(TakeOffVolumnModel.class).max("Id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.longValue();
        return nextId;
    }

    public static void addTakeOffVolumn(Realm realm, List<TakeOffVolumnModel> list) {
        //lấy ngày hiện tại theo format dd/MM/yyyy
        Date dateCurrent = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());

        //add từng item trong list
        for (TakeOffVolumnModel item : list) {
//kiểm tra off take volume trong ngày đã được lưu và upload lên hay chưa
            TakeOffVolumnModel takeOffVolumn = realm.where(TakeOffVolumnModel.class).equalTo("OutletID", item.OutletID)
                    .equalTo("CreatedBy", item.CreatedBy)
                    .equalTo("ProductID", item.ProductID)
                    .greaterThanOrEqualTo("DateCreate", dateCurrent)
                    .equalTo("Status", Constants.WAITING_UPLOAD).findFirst();
            //nếu off take volume khác null thì update giá trị vào
            if (takeOffVolumn != null) {
                takeOffVolumn.setNumberSP(item.NumberSP);
                takeOffVolumn.setLastUpdatedDateTime(ConvertUtils.getDateTimeCurrent());
                takeOffVolumn.setRowVersion(takeOffVolumn.RowVersion + 1);
            } else { //nếu null thì tạo mới
                takeOffVolumn = new TakeOffVolumnModel(id(realm) + 1, ConvertUtils.getCodeGenerationByTime(), item.OutletID,
                        item.ProductID, item.NumberSP, item.CreatedBy, ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort()),
                        ConvertUtils.getDateTimeCurrent(), ConvertUtils.getDateTimeCurrent(), Constants.WAITING_UPLOAD, 0);
                realm.copyToRealm(takeOffVolumn);
            }
        }
    }

    public static LinkedHashMap<ProductModel, TakeOffVolumnModel> getTakeOffVolumnByDate(Realm realm, int teamId, int ouletId) {
        LinkedHashMap<ProductModel, TakeOffVolumnModel> list = new LinkedHashMap<>();
        //lấy ds product có trong off take volume
        RealmResults<ProductModel> listProduct = realm.where(ProductModel.class).equalTo("IsTakeOffVolume", true).findAll();
        //lấy ngày hiện tại theo format dd/MM/yyyy
        Date dateCurrent = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());

        //lấy off take volume theo product
        for (ProductModel productModel : listProduct) {
            //lấy ds off take volume theo ngày
            RealmResults<TakeOffVolumnModel> results = realm.where(TakeOffVolumnModel.class).equalTo("OutletID", ouletId)
                    .equalTo("CreatedBy", teamId)
                    .equalTo("ProductID", productModel.getId())
                    .greaterThanOrEqualTo("DateCreate", dateCurrent)
                    .findAll();

            //kiểm tra list kết quả có hay không nếu có lấy giá trị cuối cùng or truyền vào null để add vào list
            if (results.size() > 0) {
                list.put(productModel, results.last());
            } else {
                list.put(productModel, null);
            }
        }

        return list;
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

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public int getNumberSP() {
        return NumberSP;
    }

    public void setNumberSP(int numberSP) {
        NumberSP = numberSP;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDateTime() {
        return CreatedDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        CreatedDateTime = createdDateTime;
    }

    public String getTakeOffVolumnCode() {
        return TakeOffVolumnCode;
    }

    public Date getDateCreate() {
        return DateCreate;
    }

    public String getLastUpdatedDateTime() {
        return LastUpdatedDateTime;
    }

    public void setLastUpdatedDateTime(String lastUpdatedDateTime) {
        LastUpdatedDateTime = lastUpdatedDateTime;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getRowVersion() {
        return RowVersion;
    }

    public void setRowVersion(int rowVersion) {
        RowVersion = rowVersion;
    }


    public static List<TakeOffVolumnModel> getListTakeOffVolumnWaitingUpload(Realm realm, int teamId, int outletId) {
        RealmResults<TakeOffVolumnModel> results = realm.where(TakeOffVolumnModel.class)
                .equalTo("CreatedBy", teamId)
                .equalTo("OutletID", outletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        //nếu để return result; thì khi parse sang json sẽ không lấy được giá trị của field
        return realm.copyFromRealm(results);
    }

    public static int updateStatusTakeOffVolumn(Realm realm, int teamId, int outletId) {
        //lấy ds chưa tải lên server và cập nhật lại trạng thái đã upload
        RealmResults<TakeOffVolumnModel> results = realm.where(TakeOffVolumnModel.class)
                .equalTo("CreatedBy", teamId)
                .equalTo("OutletID", outletId)
                .equalTo("Status", Constants.WAITING_UPLOAD).findAll();
        for (TakeOffVolumnModel takeOffVolumn : results) {
            takeOffVolumn.setStatus(Constants.UPLOADED);
        }
        return results.size();
    }
}
