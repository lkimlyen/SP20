package com.demo.sp19.screen.manager_gift;


import com.demo.architect.data.model.offline.BackgroundDialModel;
import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.CurrentGiftModel;
import com.demo.architect.data.model.offline.DetailCurrentGiftModel;
import com.demo.architect.data.model.offline.GiftModel;
import com.demo.sp19.app.base.BasePresenter;
import com.demo.sp19.app.base.BaseView;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface ManagerGiftContract {
    interface View extends BaseView<Presenter> {
        void showListGiftChange(LinkedHashMap<GiftModel, DetailCurrentGiftModel> list);
    }

    interface Presenter extends BasePresenter {
        void getListGiftChange();
    }
}
