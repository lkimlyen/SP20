package com.demo.compass.data.model.offline;

import com.demo.architect.data.helper.Constants;
import com.demo.architect.utils.view.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ImageModel extends RealmObject {
    @PrimaryKey
    private int id;
    private String imageCode;
    private double latitude;
    private double longitude;
    private String fileName;
    private String dateCreate;
    private String path;
    private int imageType;
    private int createdBy;
    private int status;

    public ImageModel() {
    }

    public ImageModel(double latitude, double longitude, String fileName, String path, int imageType, int createdBy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.fileName = fileName;
        this.path = path;
        this.imageType = imageType;
        this.createdBy = createdBy;
    }

    public ImageModel(int id, String imageCode, double latitude, double longitude, String fileName, String dateCreate, String path, int imageType, int createdBy, int status) {
        this.id = id;
        this.imageCode = imageCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fileName = fileName;
        this.dateCreate = dateCreate;
        this.path = path;
        this.imageType = imageType;
        this.createdBy = createdBy;
        this.status = status;
    }

    public static int id(Realm realm) {
        int nextId = 0;
        Number maxValue = realm.where(ImageModel.class).max("id");
        // If id is null, set it to 1, else set increment it by 1
        nextId = (maxValue == null) ? 0 : maxValue.intValue();
        return nextId;
    }

    public static List<Integer> addImageModel(Realm realm, List<ImageModel> imageModels) {
        List<Integer> integerList = new ArrayList<>();
        for (ImageModel imageModel : imageModels) {
            ImageModel imageModel1 = new ImageModel(id(realm) + 1, ConvertUtils.getCodeGenerationByTime(),
                    imageModel.latitude, imageModel.longitude, imageModel.fileName, ConvertUtils.getDateTimeCurrent(),
                    imageModel.path, imageModel.imageType, imageModel.createdBy, Constants.WAITING_UPLOAD);
            int id = realm.copyToRealm(imageModel1).id;
            integerList.add(id);
        }
        return integerList;
    }

    public int getId() {
        return id;
    }

    public String getImageCode() {
        return imageCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public String getPath() {
        return path;
    }

    public int getImageType() {
        return imageType;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
