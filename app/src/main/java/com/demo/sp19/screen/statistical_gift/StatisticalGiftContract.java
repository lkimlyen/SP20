package com.demo.sp19.screen.statistical_gift;


import com.demo.architect.data.model.offline.CustomerGiftModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.DetailCurrentGiftModel;
import com.demo.architect.data.model.offline.GiftModel;
import com.demo.sp19.app.base.BasePresenter;
import com.demo.sp19.app.base.BaseView;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface StatisticalGiftContract {
    interface View extends BaseView<Presenter> {
        void showListCustomerGift(LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list);
    }

    interface Presenter extends BasePresenter {
        void getListCustomerGiftByDate(String date);
    }
}
