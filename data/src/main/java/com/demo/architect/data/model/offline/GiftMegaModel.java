package com.demo.architect.data.model.offline;


import com.demo.architect.data.model.GiftMegaEntity;
import com.demo.architect.data.model.GiftMegaInTimeEntity;
import com.demo.architect.utils.view.ConvertUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class GiftMegaModel extends RealmObject {
    @PrimaryKey
    private int Id;
    private String GiftName;
    private String FilePath;
    private boolean IsGift;

    public GiftMegaModel() {
    }

    public GiftMegaModel(int id, String giftName, String filePath, boolean isGift) {
        Id = id;
        GiftName = giftName;
        FilePath = filePath;
        IsGift = isGift;
    }

    public static void addGift(Realm realm, List<GiftMegaEntity> list) {
        for (GiftMegaEntity giftMegaEntity : list) {
            TimeRotationModel timeRotationModel = new TimeRotationModel(giftMegaEntity.getId(), ConvertUtils.ConvertStringToShortDate(giftMegaEntity.getDateStart()),
                    ConvertUtils.ConvertStringToShortDate(giftMegaEntity.getDateEnd()));
            timeRotationModel = realm.copyToRealm(timeRotationModel);
            RealmList<GiftMegaModel> realmList = timeRotationModel.getGiftList();
            for (GiftMegaInTimeEntity item : giftMegaEntity.getListGift()) {
                GiftMegaModel giftMegaModel = new GiftMegaModel(item.getId(), item.getGiftName(), item.getFilePath(), item.isGift());
                giftMegaModel = realm.copyToRealm(giftMegaModel);
                realmList.add(giftMegaModel);
            }
        }

    }

    public static void deleteGiftMega(Realm realm) {
        RealmResults<TimeRotationModel> timeRotationModels = realm.where(TimeRotationModel.class).findAll();
        timeRotationModels.deleteAllFromRealm();
        RealmResults<GiftMegaModel> results = realm.where(GiftMegaModel.class).findAll();
        for (GiftMegaModel giftModel : results) {
            File file = new File(giftModel.getFilePath());
            if (file.exists()){
                file.delete();
            }
        }
        results.deleteAllFromRealm();
    }


    public int getId() {
        return Id;
    }

    public String getGiftName() {
        return GiftName;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public static TimeRotationModel getLListGiftMegaByDate(Realm realm) {
        Date dateCurrent = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());
        RealmResults<TimeRotationModel> results = realm.where(TimeRotationModel.class)
                .findAll();
        TimeRotationModel timeRotationModel = null;
        for (TimeRotationModel item : results) {
            if (item.getDateStart().equals(dateCurrent) || item.getDateEnd().equals(dateCurrent) || (dateCurrent.after(item.getDateStart()) && dateCurrent.before(item.getDateEnd()))){
                timeRotationModel= realm.copyFromRealm(item);
                break;
            }
        }
        return timeRotationModel;
    }

    public static void updateFilePathGiftMega(Realm realm, int id, String path) {
        GiftMegaModel giftMegaModel = realm.where(GiftMegaModel.class).equalTo("Id", id).findFirst();
        giftMegaModel.setFilePath(path);
    }

    public boolean isGift() {
        return IsGift;
    }
}
