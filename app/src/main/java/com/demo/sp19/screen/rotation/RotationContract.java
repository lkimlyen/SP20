package com.demo.sp19.screen.rotation;


import com.demo.architect.data.model.CurrentGift;
import com.demo.architect.data.model.offline.BackgroundDialModel;
import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.BrandSetDetailModel;
import com.demo.architect.data.model.offline.CurrentBrandModel;
import com.demo.architect.data.model.offline.CustomerGiftModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.GiftModel;
import com.demo.architect.data.model.offline.ProductGiftModel;
import com.demo.architect.data.model.offline.TotalChangeGiftModel;
import com.demo.architect.data.model.offline.TotalRotationBrandModel;
import com.demo.architect.data.model.offline.TotalTopupModel;
import com.demo.sp19.app.base.BasePresenter;
import com.demo.sp19.app.base.BaseView;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by MSI on 26/11/2017.
 */

public interface RotationContract {
    interface View extends BaseView<Presenter> {

        void showListGift(List<GiftModel> giftModels, boolean rotation);

        void showBackgroundDial(BackgroundDialModel backgroundDialModel);

        void showListGiftProduct(LinkedHashMap<BrandModel, List<ProductGiftModel>> list);

        void showInfoCus(LinkedHashMap<String, String> listInfo);

        void showBrandSetDetail(List<BrandSetDetailModel> list);

        void showAllListGift(List<GiftModel> list);

        void goToRotationMega(String string, int customerId);

        void showInfoCustomerAndListTotalBrand(CustomerModel customerModel, List<TotalRotationBrandModel> list, List<TotalChangeGiftModel> totalChangeGiftModels, TotalTopupModel totalTopupModel);

        void showDialogTopUp();

        void sendTopupSuccessfully();
    }

    interface Presenter extends BasePresenter {
        //lấy ds background theo từng brand
        void getListBackGround(int brandId);

        //lấy ds quà theo product
        void getListGiftByProduct(List<Integer> idProductList);

        //lưu vòng quay theo từng brand
        void addRotationToBackground(int brandId, String path);

        //lấy thông tin khách hàng theo id
        void getInfoCustomerById(int customerId);

        //cập nhật số lượng quà đã quay và luwu thông tin quà cho customer
        void updateNumberTurnAndSaveGift(int customerId, int id, int giftId, int number, List<CurrentGift> currentGiftList, CurrentBrandModel currentBrandModel, boolean finish);

        //lưu thông tin quà đã nhận được của khách hàng
        void saveGift(int customerId, LinkedHashMap<GiftModel, Integer> listChooseGift);

        void confirmChangeSet(int customerId, LinkedHashMap<Integer, Boolean> changeBrandSetList, LinkedHashMap<GiftModel, Integer> listGift);

        //check điều kiện chuyển đến vòng quay mega
        void goToMega(int customerId);

        //lấy ds quà đã đổi của customer
        List<CustomerGiftModel> getGiftByCustomer(int customerId);

        void sendTopupCard(int customerId,String phone, String type);


    }
}
