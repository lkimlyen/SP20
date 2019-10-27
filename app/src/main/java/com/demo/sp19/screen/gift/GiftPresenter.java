package com.demo.sp19.screen.gift;

import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.CustomerGiftModel;
import com.demo.architect.data.model.offline.CustomerImageModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.CustomerProductModel;
import com.demo.architect.data.model.offline.ImageModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.model.offline.TotalChangeGiftModel;
import com.demo.architect.data.model.offline.TotalRotationBrandModel;
import com.demo.architect.domain.GetCurrentBrandSetUsecase;
import com.demo.architect.domain.UpdateChangeSetUsecase;
import com.demo.architect.utils.view.ConvertUtils;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.manager.BrandManager;
import com.demo.sp19.manager.OutletBrandManager;
import com.demo.sp19.manager.UserManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class GiftPresenter implements GiftContract.Presenter {

    private final String TAG = GiftPresenter.class.getName();
    private final GiftContract.View view;
    private final UpdateChangeSetUsecase updateChangeSetUsecase;
    private final GetCurrentBrandSetUsecase getCurrentBrandSetUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    GiftPresenter(@NonNull GiftContract.View view, UpdateChangeSetUsecase updateChangeSetUsecase, GetCurrentBrandSetUsecase getCurrentBrandSetUsecase) {
        this.view = view;
        this.updateChangeSetUsecase = updateChangeSetUsecase;
        this.getCurrentBrandSetUsecase = getCurrentBrandSetUsecase;
    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");
        // changeSetGift();
    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void getListProductChangeGift() {
        UserEntity userEntity = UserManager.getInstance().getUser();
        List<Integer> idList = OutletBrandManager.getInstance().getOutletBrandList(userEntity.getOutlet().getOutletId());
        //lấy ds product theo brand id
        localRepository.getListProductByBrandId(idList).subscribe(new Action1<List<ProductModel>>() {
            @Override
            public void call(List<ProductModel> productModels) {
                view.showListProductChangeGift(productModels);
            }
        });

        //lấy ds brand đang chạy
        localRepository.getListBrandById(idList).subscribe(new Action1<List<BrandModel>>() {
            @Override
            public void call(List<BrandModel> list) {
                BrandManager.getInstance().setBrandList(list);
            }
        });
    }


    @Override
    public BrandModel getBrandModelByBrandId(int brandId) {
        return BrandManager.getInstance().getBrandModelByBrandId(brandId);
    }

    @Override
    public void checkOrderCode(String orderCode) {
        //check mã hóa đơn có tồn tại trong ngày chưa
        localRepository.checkOrderCode(orderCode).subscribe(new Action1<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>>() {
            @Override
            public void call(LinkedHashMap<CustomerModel, List<CustomerGiftModel>> customerModelListLinkedHashMap) {
                view.showCustomerCode(customerModelListLinkedHashMap);
            }
        });
    }

    @Override
    public void checkCustomerPhone(String phone) {
        List<Integer> idList = OutletBrandManager.getInstance().getOutletBrandList(UserManager.getInstance().getUser().getOutlet().getOutletId());
        //check sl đổi quà theo sđt
        localRepository.checkCustomerPhone(phone, idList).subscribe(new Action1<LinkedHashMap<CustomerModel, List<CustomerGiftModel>>>() {
            @Override
            public void call(LinkedHashMap<CustomerModel, List<CustomerGiftModel>> customerModelListLinkedHashMap) {

                //lấy số quà của khách hàng theo brand
                localRepository.getListNumberGiftWithBrand(phone, idList).subscribe(new Action1<LinkedHashMap<BrandModel, Integer>>() {
                    @Override
                    public void call(LinkedHashMap<BrandModel, Integer> brandModelIntegerLinkedHashMap) {
                        view.showCustomerPhone(customerModelListLinkedHashMap, brandModelIntegerLinkedHashMap);
                    }
                });

            }
        });

        //lấy thông tin customer theo sđt
        localRepository.getInfoCusByPhone(phone).subscribe(new Action1<CustomerModel>() {
            @Override
            public void call(CustomerModel customerModel) {
                if (customerModel != null) {
                    view.showNameAndCusCode(customerModel);
                }
            }
        });
    }


    @Override
    public void saveInfoCustomer(String orderCode, String cusCode, String cusName, String phone, String address, String note,
                                 List<String> imagePath, LinkedHashMap<ProductModel, Integer> numberProductList,
                                 LinkedHashMap<Integer, Integer> brandList, LinkedHashMap<ProductModel, Integer> productChooseNumberList, int totalRotaion) {
        UserEntity user = UserManager.getInstance().getUser();
        CustomerModel customerModel = new CustomerModel(user.getOutlet().getOutletId(), cusName, cusCode,
                phone, orderCode, address, note, user.getTeamOutletId());
        //lưu thông tin của customer
        localRepository.addCustomerModel(customerModel).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer id) {
                int customerId = id;
                List<CustomerProductModel> productModelList = new ArrayList<>();
                for (Map.Entry<ProductModel, Integer> map : numberProductList.entrySet()) {
                    CustomerProductModel customerProductModel = new CustomerProductModel(customerId, user.getOutlet().getOutletId(), map.getKey().getId(), map.getValue(), user.getTeamOutletId());
                    productModelList.add(customerProductModel);
                }
//lưu thông tin sản phẩm đổi quà
                localRepository.addCustomerProductModel(productModelList).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        List<ImageModel> imageModels = new ArrayList<>();
                        for (String path : imagePath) {
                            String nameFile = path.substring(path.lastIndexOf("/") + 1);
                            ImageModel imageModel = new ImageModel(0, 0, nameFile, path, 2, user.getTeamOutletId());
                            imageModels.add(imageModel);
                        }
//lưu hình hóa đơn
                        localRepository.addImageModel(imageModels).subscribe(new Action1<List<Integer>>() {
                            @Override
                            public void call(List<Integer> integers) {
                                List<CustomerImageModel> customerImageModelList = new ArrayList<>();
                                for (Integer imageId : integers) {
                                    CustomerImageModel customerImageModel = new CustomerImageModel(customerId, user.getOutlet().getOutletId(), imageId, user.getTeamOutletId());
                                    customerImageModelList.add(customerImageModel);
                                }

                                //lấy id hình hóa đơn lưu vào customer
                                localRepository.addCustomerImageModel(customerImageModelList).subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        Date dateStartSP = ConvertUtils.ConvertStringToShortDate("23/12/2018 00:00:00");
                                        Date dateCurrent = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());
                                        //kiểm tra xem outlet này có quay vòng quay mega ko
                                        if (user.getOutlet().isLuckyMega()) {
                                            //lưu tổng số vòng quay theo từng brand
                                            localRepository.saveTotalRotaion(customerId, totalRotaion, UserManager.getInstance().getUser().getTeamOutletId()).subscribe(new Action1<String>() {
                                                @Override
                                                public void call(String string) {
                                                    if (dateCurrent.before(dateStartSP)) {
                                                        view.goToRotationMega(customerId);
                                                    } else {
                                                        if (productChooseNumberList.size() > 0) {
                                                            saveNumberChooseGift(customerId, productChooseNumberList);
                                                        }
                                                        if (brandList.size() > 0) {
                                                            saveTotalBrandModel(customerId, brandList);
                                                        } else {
                                                            view.goToRotationSP(customerId);
                                                        }
                                                    }
                                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_success));
                                                }
                                            });
                                        } else {
                                            //lưu sl quà được đổi theo product
                                            if (productChooseNumberList.size() > 0) {
                                                saveNumberChooseGift(customerId, productChooseNumberList);
                                            }
                                            //lưu tổng số vòng quay theo từng brand
                                            if (brandList.size() > 0) {
                                                saveTotalBrandModel(customerId, brandList);
                                            } else {
                                                view.goToRotationSP(customerId);
                                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_success));
                                            }
                                        }

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private void saveNumberChooseGift(int customerId, LinkedHashMap<ProductModel, Integer> productChooseNumberList) {
        if (productChooseNumberList.size() > 0) {
            List<TotalChangeGiftModel> list = new ArrayList<>();

            for (Map.Entry<ProductModel, Integer> map : productChooseNumberList.entrySet()) {
                TotalChangeGiftModel totalChangeGiftModel = new TotalChangeGiftModel(customerId, map.getKey().getId(),
                        map.getKey().getBrandID(), map.getValue());
                list.add(totalChangeGiftModel);
            }
            localRepository.saveTotalProductChooseGift(list).subscribe();
        }
    }

    public void saveTotalBrandModel(int customerId, LinkedHashMap<Integer, Integer> brandList) {
        List<TotalRotationBrandModel> rotationBrandModelList = new ArrayList<>();
        List<Integer> list = new ArrayList<>(brandList.keySet());
        Collections.sort(list);
        for (Integer brandId : list) {
            TotalRotationBrandModel totalRotationBrandModel = new TotalRotationBrandModel(customerId, brandId, brandList.get(brandId));
            rotationBrandModelList.add(totalRotationBrandModel);
        }

        localRepository.saveTotalRotaionWithBrand(rotationBrandModelList).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.goToRotationSP(customerId);
            }
        });
    }

    @Override
    public void getInfoCusCrash(String orderCode, String phone, int customerId) {
        Date dateStartSP = ConvertUtils.ConvertStringToShortDate("23/12/2018 00:00:00");
        Date dateCurrent = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());
        boolean changeMega = true;
        boolean changeSP = true;
        if (UserManager.getInstance().getUser().getOutlet().isLuckyMega()) {
            if (dateCurrent.before(dateStartSP)) {
                changeSP = false;
            }
        } else {
            changeMega = false;
        }
        //lấy ds customer chưa hoàn thành lượt quay và đổi quà
        localRepository.getInfoCusNotFinished(orderCode, phone, UserManager.getInstance().getUser().getTeamOutletId(), changeMega, changeSP).subscribe(new Action1<CustomerModel>() {
            @Override
            public void call(CustomerModel customerModel) {
                if (customerModel != null) {
                    //lấy ds product đổi quà của customer
                    localRepository.getListProductByCustomerId(customerModel.getId()).subscribe(new Action1<List<CustomerProductModel>>() {
                        @Override
                        public void call(List<CustomerProductModel> customerProductModelProductModelLinkedHashMap) {
                            //lấy hình ảnh hóa đơn của cus
                            localRepository.getListImageByCustomerId(customerModel.getId()).subscribe(new Action1<List<ImageModel>>() {
                                @Override
                                public void call(List<ImageModel> customerImageModelImageModelLinkedHashMap) {
                                    view.showInfoCus(customerModel, customerProductModelProductModelLinkedHashMap, customerImageModelImageModelLinkedHashMap);
                                }
                            });
                        }
                    });
                } else {
                    if (customerId != -1) {
                        view.resetInfoCutomerWhenInput();
                    }
                }
            }
        });
    }

    @Override
    public void checkCondition(CustomerModel customerModel) {
        Date dateStartSP = ConvertUtils.ConvertStringToShortDate("23/12/2018 00:00:00");
        Date dateCurrent = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());
        if (UserManager.getInstance().getUser().getOutlet().isLuckyMega()) {

            if (dateCurrent.before(dateStartSP)) {
                view.goToRotationMega(customerModel.getId());
            } else {
                if (!customerModel.isFinishedSP()) {
                    view.goToRotationSP(customerModel.getId());
                } else {

                    view.goToRotationMega(customerModel.getId());
                }

            }
        } else {
            view.goToRotationSP(customerModel.getId());
        }
    }


}
