package com.demo.architect.data.model.offline;


import com.demo.architect.data.model.NotificationEntity;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

public class NotificationModel extends RealmObject {
    @PrimaryKey
    private int Id;

    private String sessionCode;

    private String description;

    private String title;

    private String date;

    public NotificationModel() {
    }

    public NotificationModel(int Id, String sessionCode, String description, String title, String date) {
        this.Id = Id;
        this.sessionCode = sessionCode;
        this.description = description;
        this.title = title;
        this.date = date;
    }

    public static void delete(Realm realm, int id) {
        NotificationModel item = realm.where(NotificationModel.class).equalTo("Id", id).findFirst();
        // Otherwise it has been deleted already.
        if (item != null) {
            item.deleteFromRealm();
        }
    }

    public static void create(Realm realm, List<NotificationEntity> list) {
        for (NotificationEntity notificationEntity : list) {
            NotificationModel notificationModel = new NotificationModel(notificationEntity.getId(), notificationEntity.getCode(),
                    notificationEntity.getDescription(), notificationEntity.getTitle(), notificationEntity.getDate());
            realm.copyToRealmOrUpdate(notificationModel);
        }


    }

    public static List<NotificationModel> getListNoti(Realm realm) {
        RealmResults<NotificationModel> results = realm.where(NotificationModel.class).findAll().sort("Id", Sort.DESCENDING);
        return realm.copyFromRealm(results);
    }

    public int getId() {
        return Id;
    }

    public String getSessionCode() {
        return sessionCode;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }
}
