package com.demo.sp19.screen.posm;


import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.POSMModel;
import com.demo.architect.data.model.offline.POSMReportModel;
import com.demo.architect.data.model.offline.RequestGiftModel;
import com.demo.sp19.app.base.BasePresenter;
import com.demo.sp19.app.base.BaseView;

import java.util.LinkedHashMap;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public interface POSMContract {
    interface View extends BaseView<Presenter> {
        //show ds report của posm theo brand
        void showListPOSM(LinkedHashMap<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>> list);
    }

    interface Presenter extends BasePresenter {
        //lấy danh sách report của posm theo brand by date
        void getListPOSM();

        //lưu ds báo cáo report
        void saveListPOSMReport(List<POSMReportModel> list);
    }
}
