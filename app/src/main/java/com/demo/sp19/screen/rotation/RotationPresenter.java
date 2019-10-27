package com.demo.sp19.screen.rotation;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.model.ChooseSetEntitiy;
import com.demo.architect.data.model.CurrentGift;
import com.demo.architect.data.model.OutletEntiy;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.BackgroundDialModel;
import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.BrandSetDetailModel;
import com.demo.architect.data.model.offline.CurrentBrandModel;
import com.demo.architect.data.model.offline.CurrentGiftModel;
import com.demo.architect.data.model.offline.CustomerGiftModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.GiftModel;
import com.demo.architect.data.model.offline.ProductGiftModel;
import com.demo.architect.data.model.offline.TimeRotationModel;
import com.demo.architect.data.model.offline.TotalChangeGiftModel;
import com.demo.architect.data.model.offline.TotalRotationBrandModel;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.UpdateChangeSetUsecase;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.manager.ChooseSetManager;
import com.demo.sp19.manager.CurrentBrandSetManager;
import com.demo.sp19.manager.CurrentGiftManager;
import com.demo.sp19.manager.UserManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class RotationPresenter implements RotationContract.Presenter {

    private final String TAG = RotationPresenter.class.getName();
    private final RotationContract.View view;
    private final UpdateChangeSetUsecase updateChangeSetUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    RotationPresenter(@NonNull RotationContract.View view, UpdateChangeSetUsecase updateChangeSetUsecase) {
        this.view = view;
        this.updateChangeSetUsecase = updateChangeSetUsecase;
    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        Log.d(TAG, TAG + ".start() called");
    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void getListBackGround(int brandId) {
        OutletEntiy outletEntiy = UserManager.getInstance().getUser().getOutlet();
        //lấy background vòng quay theo brandid
        localRepository.getBackgroundDialByBrandId(brandId).subscribe(new Action1<BackgroundDialModel>() {
            @Override
            public void call(BackgroundDialModel backgroundDialModel) {
                view.showBackgroundDial(backgroundDialModel);
                //lấy ds quà theo brandid và set hiện tại outlet chạy
                localRepository.getListGiftByBrandIdAndSetId(outletEntiy.getOutletId(), brandId).subscribe(new Action1<List<GiftModel>>() {
                    @Override
                    public void call(List<GiftModel> giftModels) {
                        view.showListGift(giftModels, !TextUtils.isEmpty(backgroundDialModel.getRotation()));
                    }
                });

            }
        });

        //lấy số lượng quà còn lại hiện tại theo brandid
        localRepository.getListCurrentGift(outletEntiy.getOutletId(), brandId).subscribe(new Action1<List<CurrentGiftModel>>() {
            @Override
            public void call(List<CurrentGiftModel> currentGiftModels) {
                List<CurrentGift> list = new ArrayList<>();
                for (CurrentGiftModel currentGiftModel : currentGiftModels) {
                    CurrentGift currentGift = new CurrentGift(currentGiftModel.getGiftID(), currentGiftModel.getNumberTotal(), currentGiftModel.getNumberUsed());
                    list.add(currentGift);
                }
                CurrentGiftManager.getInstance().setCurrentGift(list, false);
            }
        });

        //lấy brand hiện tại của outlet
        localRepository.getListCurrentBrandSet(outletEntiy.getOutletId(), brandId).subscribe(new Action1<CurrentBrandModel>() {
            @Override
            public void call(CurrentBrandModel currentBrandModel) {
                CurrentBrandSetManager.getInstance().setCurrentBrandModel(currentBrandModel);
            }
        });

        //lấy ds quà trong set brand hiện tại
        localRepository.getListBrandSetDetail(brandId).subscribe(new Action1<List<BrandSetDetailModel>>() {
            @Override
            public void call(List<BrandSetDetailModel> list) {
                view.showBrandSetDetail(list);
            }
        });

    }

    @Override
    public void getListGiftByProduct(List<Integer> idProductList) {
        //lấy ds quà theo product id
        localRepository.getListGiftByProduct(idProductList).subscribe(new Action1<LinkedHashMap<BrandModel, List<ProductGiftModel>>>() {
            @Override
            public void call(LinkedHashMap<BrandModel, List<ProductGiftModel>> brandModelListLinkedHashMap) {
                view.showListGiftProduct(brandModelListLinkedHashMap);
            }
        });
    }


    @Override
    public void addRotationToBackground(int brandId, String path) {
        //lưu vòng quay theo brand
        localRepository.addRotationToBackground(brandId, path).subscribe();
    }


    private int positionBrand = 0;

    //thay đổi set quà
    public void changeBrandGift(int customerId, List<ChooseSetEntitiy> chooseSetEntitiys, LinkedHashMap<Integer, Boolean> stateBrandSetList, LinkedHashMap<GiftModel, Integer> giftList) {

        //lấy set quà đổi theo thứ tự positionBrand
        ChooseSetEntitiy chooseSetEntitiy = chooseSetEntitiys.get(positionBrand);
        UserEntity user = UserManager.getInstance().getUser();
        //kiểm tra list stateBrandSetList có null  hay không và đã qua vòng mới chưa
        if (stateBrandSetList.get(chooseSetEntitiy.getBrandId()) != null && stateBrandSetList.get(chooseSetEntitiy.getBrandId())) {
            //chạy hàm cập nhật đổi set của server
            updateChangeSetUsecase.executeIO(new UpdateChangeSetUsecase.RequestValue(user.getOutlet().getOutletId(),
                            chooseSetEntitiy.getRequimentChangeSetID()),
                    new BaseUseCase.UseCaseCallback<UpdateChangeSetUsecase.ResponseValue,
                            UpdateChangeSetUsecase.ErrorValue>() {
                        @Override
                        public void onSuccess(UpdateChangeSetUsecase.ResponseValue successResponse) {
                            //cập nhật lại brand set cho outlet và current gift
                            localRepository.updateBrandSetCurrent(user.getOutlet().getOutletId(), chooseSetEntitiy.getBrandSetId(), chooseSetEntitiy.getNumberUsed())
                                    .subscribe(new Action1<String>() {
                                        @Override
                                        public void call(String string) {
                                            //update lại ds đổi set và lưu lại sharePreference
                                            ChooseSetManager.getInstance().updateSetGift(chooseSetEntitiy);
                                            //view.showSuccess(CoreApplication.getInstance().getString(R.string.text_update_set_gift_success));

                                            if (positionBrand < chooseSetEntitiys.size() - 1) {
                                                positionBrand++;
                                                changeBrandGift(customerId, chooseSetEntitiys, stateBrandSetList, giftList);
                                            } else {

                                                view.hideProgressBar();
                                                if (giftList.size() > 0) {
                                                    saveGift(customerId, giftList);
                                                } else {
                                                    goToMega(customerId);
                                                }
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onError(UpdateChangeSetUsecase.ErrorValue errorResponse) {
                            if (positionBrand < chooseSetEntitiys.size() - 1) {
                                positionBrand++;
                                changeBrandGift(customerId, chooseSetEntitiys, stateBrandSetList, giftList);
                            } else {
                                view.hideProgressBar();
                                if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                                    view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                                } else {
                                    view.showError(errorResponse.getDescription());
                                }
                            }
                        }
                    });

        } else {
            //kiểm tra điều kiện đổi set theo brand id và brand set id
            localRepository.checkConditionChangeBrand(chooseSetEntitiy.getBrandId(), chooseSetEntitiy.getBrandSetId()).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean aBoolean) {
                    if (aBoolean) {
                        updateChangeSetUsecase.executeIO(new UpdateChangeSetUsecase.RequestValue(user.getOutlet().getOutletId(),
                                        chooseSetEntitiy.getRequimentChangeSetID()),
                                new BaseUseCase.UseCaseCallback<UpdateChangeSetUsecase.ResponseValue,
                                        UpdateChangeSetUsecase.ErrorValue>() {
                                    @Override
                                    public void onSuccess(UpdateChangeSetUsecase.ResponseValue successResponse) {

                                        localRepository.updateBrandSetCurrent(user.getOutlet().getOutletId(), chooseSetEntitiy.getBrandSetId(), chooseSetEntitiy.getNumberUsed())
                                                .subscribe(new Action1<String>() {
                                                    @Override
                                                    public void call(String string) {
                                                        ChooseSetManager.getInstance().updateSetGift(chooseSetEntitiy);
                                                        //view.showSuccess(CoreApplication.getInstance().getString(R.string.text_update_set_gift_success));
                                                        if (positionBrand < chooseSetEntitiys.size() - 1) {
                                                            positionBrand++;
                                                            changeBrandGift(customerId, chooseSetEntitiys, stateBrandSetList, giftList);
                                                        } else {
                                                            view.hideProgressBar();
                                                            if (giftList.size() > 0) {

                                                                saveGift(customerId, giftList);
                                                            } else {
                                                                goToMega(customerId);
                                                            }
                                                        }
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onError(UpdateChangeSetUsecase.ErrorValue errorResponse) {

                                        if (positionBrand < chooseSetEntitiys.size() - 1) {
                                            positionBrand++;
                                            changeBrandGift(customerId, chooseSetEntitiys, stateBrandSetList, giftList);
                                        } else {
                                            view.hideProgressBar();
                                            if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                                                view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                                            } else {
                                                view.showError(errorResponse.getDescription());
                                            }
                                        }
                                    }
                                });
                    } else {
                        if (positionBrand < chooseSetEntitiys.size() - 1) {
                            positionBrand++;
                            changeBrandGift(customerId, chooseSetEntitiys, stateBrandSetList, giftList);
                        } else {

                            view.hideProgressBar();
                            if (giftList.size() > 0) {
//lưu thông tin quà của customer
                                saveGift(customerId, giftList);
                            } else {
                                goToMega(customerId);
                            }
                        }
                    }
                }
            });
        }
    }


    @Override
    public void getInfoCustomerById(int customerId) {
        //lấy thông tin customer theo id
        localRepository.getInfoCustomerById(customerId).subscribe(new Action1<CustomerModel>() {
            @Override
            public void call(CustomerModel customerModel) {
                //lấy tổng số vòng quay còn lại của brand theo customerid
                localRepository.getListTotalRotationBrand(customerId).subscribe(new Action1<List<TotalRotationBrandModel>>() {
                    @Override
                    public void call(List<TotalRotationBrandModel> list) {
                        //lấy tổng số quà đổi còn lại của brand theo customerid

                        localRepository.getListProductChooseGift(customerId).subscribe(new Action1<List<TotalChangeGiftModel>>() {
                            @Override
                            public void call(List<TotalChangeGiftModel> totalChangeGiftModels) {
                                view.showInfoCustomerAndListTotalBrand(customerModel, list, totalChangeGiftModels);
                            }
                        });
                    }
                });


            }
        });
    }

    @Override
    public void updateNumberTurnAndSaveGift(int customerId, int id, int giftId, int number, List<CurrentGift> currentGiftList,
                                            CurrentBrandModel currentBrandModel, boolean finish) {
        CustomerGiftModel customerGiftModel = new CustomerGiftModel(customerId, UserManager.getInstance().getUser().getOutlet().getOutletId(),
                giftId, number, UserManager.getInstance().getUser().getTeamOutletId());
        //cập nhật lại sô vòng quay và lưu quà
        localRepository.updateNumberTurnedAndSaveGift(
                id, customerGiftModel, currentGiftList, currentBrandModel, finish).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

            }
        });
    }


    @Override
    public void saveGift(int customerId, LinkedHashMap<GiftModel, Integer> listChooseGift) {
        UserEntity user = UserManager.getInstance().getUser();
        List<CustomerGiftModel> giftModelList = new ArrayList<>();
        if (listChooseGift.size() > 0) {
            for (Map.Entry<GiftModel, Integer> map : listChooseGift.entrySet()) {
                CustomerGiftModel giftModel = new CustomerGiftModel(customerId, user.getOutlet().getOutletId(), map.getKey().getId(),
                        map.getValue(), user.getTeamOutletId());
                giftModelList.add(giftModel);
            }
        }
        localRepository.addCustomerGiftModel(giftModelList, customerId).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                goToMega(customerId);
            }
        });
    }

    @Override
    public void confirmChangeSet(int customerId, LinkedHashMap<Integer, Boolean> changeBrandSetList, LinkedHashMap<GiftModel, Integer> listGift) {
        List<ChooseSetEntitiy> list = ChooseSetManager.getInstance().getListChooseSet();
        if (list != null && list.size() > 0) {
            view.showProgressBar();
            positionBrand = 0;
            changeBrandGift(customerId, list, changeBrandSetList, listGift);
        } else {
            if (listGift.size() > 0) {
                saveGift(customerId, listGift);
            } else {
                goToMega(customerId);
            }
        }
    }

    @Override
    public void goToMega(int customerId) {
        if (UserManager.getInstance().getUser().getOutlet().isLuckyMega()) {
            localRepository.getListGiftMegaByDate().subscribe(new Action1<TimeRotationModel>() {
                @Override
                public void call(TimeRotationModel timeRotationModel) {
                    if (timeRotationModel != null) {
                        view.goToRotationMega(CoreApplication.getInstance().getString(R.string.text_save_success), customerId);
                    } else {
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_success));
                    }
                }
            });
        } else {

            view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_success));
        }
    }

    private List<CustomerGiftModel> customerGiftModels = new ArrayList<>();

    @Override
    public List<CustomerGiftModel> getGiftByCustomer(int customerId) {
        //lấy ds quà đã quay của customer
        localRepository.getGiftByCustomerId(customerId).subscribe(new Action1<List<CustomerGiftModel>>() {
            @Override
            public void call(List<CustomerGiftModel> list) {
                customerGiftModels = list;
            }
        });

        return customerGiftModels;
    }
}