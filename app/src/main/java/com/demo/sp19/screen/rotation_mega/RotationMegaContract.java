package com.demo.sp19.screen.rotation_mega;


import com.demo.architect.data.model.CurrentGift;
import com.demo.architect.data.model.offline.BackgroundDialModel;
import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.BrandSetDetailModel;
import com.demo.architect.data.model.offline.CurrentBrandModel;
import com.demo.architect.data.model.offline.CustomerGiftMegaModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.GiftModel;
import com.demo.architect.data.model.offline.ProductGiftModel;
import com.demo.architect.data.model.offline.TimeRotationModel;
import com.demo.architect.data.model.offline.TotalRotationModel;
import com.demo.sp19.app.base.BasePresenter;
import com.demo.sp19.app.base.BaseView;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface RotationMegaContract {
    interface View extends BaseView<Presenter> {

        void showListGift(TimeRotationModel timeRotationModel);

        void showInfoCus(CustomerModel customerModel, TotalRotationModel totalRotation);
    }

    interface Presenter extends BasePresenter {

        void getListGift();

        void addImageRotaion(int id, String path);

        void getInfoCusById(int customerId);

        void saveTotalRotaion(int cusId, int totalRotation, boolean lucky);

        void saveInfoCusLucky(int cusId, int giftId);

        void updateNumberRotation(int customerId);

    }
}
