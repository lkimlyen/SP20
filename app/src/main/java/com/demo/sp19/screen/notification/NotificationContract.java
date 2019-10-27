package com.demo.sp19.screen.notification;


import com.demo.architect.data.model.offline.NotificationModel;
import com.demo.sp19.app.base.BasePresenter;
import com.demo.sp19.app.base.BaseView;

import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface NotificationContract {
    interface View extends BaseView<Presenter> {
        //show ds thông báo
        void showNotification(List<NotificationModel> list);
    }

    interface Presenter extends BasePresenter {
        //lấy ds thông báo từ database
        void getNotification();
        //get ds thông báo từ server
        void downloadNotification();
    }
}
