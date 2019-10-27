package com.demo.sp19.screen.emergency_report;


import com.demo.architect.data.model.offline.DetailCurrentGiftModel;
import com.demo.architect.data.model.offline.EmergencyModel;
import com.demo.architect.data.model.offline.GiftModel;
import com.demo.architect.data.model.offline.ReasonModel;
import com.demo.sp19.app.base.BasePresenter;
import com.demo.sp19.app.base.BaseView;

import java.util.LinkedHashMap;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public interface EmergencyReportContract {
    interface View extends BaseView<Presenter> {
        void showListReason(List<ReasonModel> list);

        //show báo cáo khẩn cấp chưa kết thúc
        void showEmergency(RealmResults<EmergencyModel> list);
    }

    interface Presenter extends BasePresenter {
        //lấy lý do khẩn cấp
        void getListReason();

        //tạo thông báo khẩn cấp
        void addEmergency(String employeeId, int reasonId);

        //lấy ds báo cáo khẩn cấp chưa kết thúc
        void getEmergencyNotFinished();

        //kêt thúc khẩn cấp
        void completeEmergency(int emergencyId);
    }
}
