package com.demo.sp19.screen.rotation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

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
import com.demo.architect.data.model.offline.CustomerImageModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.CustomerProductModel;
import com.demo.architect.data.model.offline.GiftModel;
import com.demo.architect.data.model.offline.ImageModel;
import com.demo.architect.data.model.offline.ProductGiftModel;
import com.demo.architect.data.model.offline.TimeRotationModel;
import com.demo.architect.data.model.offline.TotalChangeGiftModel;
import com.demo.architect.data.model.offline.TotalRotationBrandModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.AddBrandSetUsedUsecase;
import com.demo.architect.domain.AddCustomerGiftUsecase;
import com.demo.architect.domain.AddCustomerImageUsecase;
import com.demo.architect.domain.AddCustomerProductUsecase;
import com.demo.architect.domain.AddCustomerUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.SendRequestGiftUsecase;
import com.demo.architect.domain.UpdateChangeSetUsecase;
import com.demo.architect.domain.UpdateCurrentGiftUsecase;
import com.demo.architect.domain.UploadImageUsecase;
import com.demo.architect.utils.view.FileUtils;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.manager.ChooseSetManager;
import com.demo.sp19.manager.CurrentBrandSetManager;
import com.demo.sp19.manager.CurrentGiftManager;
import com.demo.sp19.manager.UserManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
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
    private final AddCustomerUsecase addCustomerUsecase;
    private final UploadImageUsecase uploadImageUsecase;
    private final AddCustomerProductUsecase addCustomerProductUsecase;
    private final AddCustomerGiftUsecase addCustomerGiftUsecase;
    private final AddCustomerImageUsecase addCustomerImageUsecase;
    private final UpdateCurrentGiftUsecase updateCurrentGiftUsecase;
    private final AddBrandSetUsedUsecase addBrandSetUsedUsecase;
    private final SendRequestGiftUsecase sendRequestGiftUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    RotationPresenter(@NonNull RotationContract.View view, UpdateChangeSetUsecase updateChangeSetUsecase, AddCustomerUsecase addCustomerUsecase, UploadImageUsecase uploadImageUsecase, AddCustomerProductUsecase addCustomerProductUsecase, AddCustomerGiftUsecase addCustomerGiftUsecase, AddCustomerImageUsecase addCustomerImageUsecase, UpdateCurrentGiftUsecase updateCurrentGiftUsecase, AddBrandSetUsedUsecase addBrandSetUsedUsecase, SendRequestGiftUsecase sendRequestGiftUsecase) {
        this.view = view;
        this.updateChangeSetUsecase = updateChangeSetUsecase;
        this.addCustomerUsecase = addCustomerUsecase;
        this.uploadImageUsecase = uploadImageUsecase;
        this.addCustomerProductUsecase = addCustomerProductUsecase;
        this.addCustomerGiftUsecase = addCustomerGiftUsecase;
        this.addCustomerImageUsecase = addCustomerImageUsecase;
        this.updateCurrentGiftUsecase = updateCurrentGiftUsecase;
        this.addBrandSetUsedUsecase = addBrandSetUsedUsecase;
        this.sendRequestGiftUsecase = sendRequestGiftUsecase;
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
//                                                    localRepository.getInfoSendRequest().subscribe(new Action1<List<Integer>>() {
//                                                        @Override
//                                                        public void call(List<Integer> list) {
//                                                            if (list.size() > 0){
//                                                                integerList.clear();
//                                                                integerList.addAll(list);
//                                                                uploadData();
//                                                            }else {
//                                                                goToMega(customerId);
//                                                            }
//
//                                                        }
//                                                    });
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
//                                                                localRepository.getInfoSendRequest().subscribe(new Action1<List<Integer>>() {
//                                                                    @Override
//                                                                    public void call(List<Integer> list) {
//                                                                        if (list.size() > 0){
//                                                                            integerList.clear();
//                                                                            integerList.addAll(list);
//                                                                            uploadData();
//                                                                        }else {
//                                                                            goToMega(customerId);
//                                                                        }
//
//                                                                    }
//                                                                });
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
//                                localRepository.getInfoSendRequest().subscribe(new Action1<List<Integer>>() {
//                                    @Override
//                                    public void call(List<Integer> list) {
//                                        if (list.size() > 0){
//                                            integerList.clear();
//                                            integerList.addAll(list);
//                                            uploadData();
//                                        }else {
//                                            goToMega(customerId);
//                                        }
//
//                                    }
//                                });

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

private List<Integer> integerList = new ArrayList<Integer>();
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
        localRepository.addCustomerGiftModel(giftModelList, customerId).subscribe(new Action1<List<Integer>>() {
            @Override
            public void call(List<Integer> list) {
                // view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_success));
                //goToMega(customerId);
//                if (list.size() > 0) {
//                    integerList.clear();
//                    integerList.addAll(list);
//                    uploadData();
//                } else {
                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_success));
//                }

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
//                localRepository.getInfoSendRequest().subscribe(new Action1<List<Integer>>() {
//                    @Override
//                    public void call(List<Integer> list) {
//                        if (list.size() > 0){
//                            integerList.clear();
//                            integerList.addAll(list);
//                            uploadData();
//                        }else {
//                            goToMega(customerId);
//                        }
//
//                    }
//                });
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

    public void uploadData() {
        view.showProgressBar();
        UserEntity userEntity = UserManager.getInstance().getUser();
        localRepository.getListCustomerWaitingUpload(userEntity.getOutlet().getOutletId()).subscribe(new Action1<LinkedHashMap<CustomerModel, List<ImageModel>>>() {
            @Override
            public void call(LinkedHashMap<CustomerModel, List<ImageModel>> customerModelListLinkedHashMap) {

                if (customerModelListLinkedHashMap.size() > 0) {
                    List<CustomerModel> customerList = new ArrayList<>(customerModelListLinkedHashMap.keySet());
                    positionCustomer = 0;
                    uploadCustomerGift(customerList, customerModelListLinkedHashMap);
                } else {
                    localRepository.getListWaitingUpload(userEntity.getTeamOutletId(), userEntity.getOutlet().getOutletId()).subscribe(new Action1<LinkedHashMap<String, String>>() {
                        @Override
                        public void call(LinkedHashMap<String, String> list) {
                            if (list.size() > 0) {
                                List<String> keys = new ArrayList<>(list.keySet());
                                positionUpload = 0;
                                uploadAllData(keys, list, userEntity.getTeamOutletId(), userEntity.getOutlet().getOutletId(), true);
                            } else {
                                sendRequestGift();
                                view.hideProgressBar();
                            }

                        }
                    });
                }

            }
        });
    }


    private int positionCustomer = 0;

    public void uploadCustomerGift(List<CustomerModel> customerList, LinkedHashMap<CustomerModel, List<ImageModel>> list) {
        UserEntity userEntity = UserManager.getInstance().getUser();
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        CustomerModel customerModel = customerList.get(positionCustomer);
        String json = gson.toJson(customerModel);
        if (customerModel.getServerId() == -1) {
            addCustomerUsecase.executeIO(new AddCustomerUsecase.RequestValue(json),
                    new BaseUseCase.UseCaseCallback<AddCustomerUsecase.ResponseValue,
                            AddCustomerUsecase.ErrorValue>() {
                        @Override
                        public void onSuccess(AddCustomerUsecase.ResponseValue successResponse) {
                            localRepository.addCustomerServerId(customerModel.getId(), successResponse.getId()).subscribe(new Action1<String>() {
                                @Override
                                public void call(String s) {
                                    if (list.get(customerModel).size() > 0) {
                                        positionImage = 0;
                                        uploadImage(customerList, list, list.get(customerModel));
                                    } else {
                                        if (positionCustomer < customerList.size() - 1) {
                                            positionCustomer++;
                                            uploadCustomerGift(customerList, list);
                                        } else {
                                            localRepository.getListWaitingUpload(userEntity.getTeamOutletId(), userEntity.getOutlet().getOutletId()).subscribe(new Action1<LinkedHashMap<String, String>>() {
                                                @Override
                                                public void call(LinkedHashMap<String, String> list) {
                                                    if (list.size() > 0) {
                                                        List<String> keys = new ArrayList<>(list.keySet());
                                                        positionUpload = 0;
                                                        uploadAllData(keys, list, userEntity.getTeamOutletId(), userEntity.getOutlet().getOutletId(), false);
                                                    } else {
                                                        localRepository.updateStatusCustomer().subscribe(new Action1<String>() {
                                                            @Override
                                                            public void call(String s) {
sendRequestGift();
                                                                view.hideProgressBar();

                                                            }
                                                        });
                                                    }

                                                }
                                            });
                                        }
                                    }
                                    //  uploadImage();
                                }
                            });
                        }

                        @Override
                        public void onError(AddCustomerUsecase.ErrorValue errorResponse) {
                            if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                                view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                            } else {
                                view.showError(errorResponse.getDescription());
                            }view.showUploadRetry();
                            view.hideProgressBar();

                        }
                    });
        } else {

            if (list.get(customerModel).size() > 0) {
                positionImage = 0;
                uploadImage(customerList, list, list.get(customerModel));
            } else {
                if (positionCustomer < customerList.size() - 1) {
                    positionCustomer++;
                    uploadCustomerGift(customerList, list);
                } else {
                    localRepository.getListWaitingUpload(userEntity.getTeamOutletId(), userEntity.getOutlet().getOutletId()).subscribe(new Action1<LinkedHashMap<String, String>>() {
                        @Override
                        public void call(LinkedHashMap<String, String> list) {
                            if (list.size() > 0) {
                                List<String> keys = new ArrayList<>(list.keySet());
                                positionUpload = 0;
                                uploadAllData(keys, list, userEntity.getTeamOutletId(), userEntity.getOutlet().getOutletId(), false);
                            } else {
                                localRepository.updateStatusCustomer().subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
sendRequestGift();
                                        view.hideProgressBar();
                                    }
                                });
                            }

                        }
                    });
                }
            }
        }
    }

    private int positionImage = 0;

    public void uploadImage(List<CustomerModel> customerList, LinkedHashMap<CustomerModel, List<ImageModel>> list,
                            List<ImageModel> imageList) {
        UserEntity userEntity = UserManager.getInstance().getUser();
        ImageModel imageModel = imageList.get(positionImage);
        Bitmap bitmap = BitmapFactory.decodeFile(imageModel.getPath());
        if (bitmap != null) {
            bitmap = FileUtils.getResizedBitmap(bitmap, 800);
            File file = FileUtils.bitmapToFile(bitmap, imageModel.getPath());
            uploadImageUsecase.executeIO(new UploadImageUsecase.RequestValue(file, imageModel.getImageCode(),
                            imageModel.getCreatedBy(), imageModel.getDateCreate(), imageModel.getLatitude(), imageModel.getLongitude(),
                            imageModel.getImageType(), imageModel.getFileName()),
                    new BaseUseCase.UseCaseCallback<UploadImageUsecase.ResponseValue, UploadImageUsecase.ErrorValue>() {
                        @Override
                        public void onSuccess(UploadImageUsecase.ResponseValue successResponse) {
                            localRepository.addCustomerImageServerId(customerList.get(positionCustomer).getId(),
                                    imageModel.getId(), successResponse.getId()).subscribe(new Action1<String>() {
                                @Override
                                public void call(String s) {
                                    if (positionImage < imageList.size() - 1) {
                                        positionImage++;
                                        uploadImage(customerList, list, imageList);
                                    } else {
                                        if (positionCustomer < customerList.size() - 1) {
                                            positionCustomer++;
                                            uploadCustomerGift(customerList, list);
                                        } else {
                                            localRepository.getListWaitingUpload(userEntity.getTeamOutletId(), userEntity.getOutlet().getOutletId()).subscribe(new Action1<LinkedHashMap<String, String>>() {
                                                @Override
                                                public void call(LinkedHashMap<String, String> list) {
                                                    List<String> keys = new ArrayList<>(list.keySet());
                                                    positionUpload = 0;
                                                    uploadAllData(keys, list, userEntity.getTeamOutletId(), userEntity.getOutlet().getOutletId(), false);
                                                }
                                            });
                                        }

                                    }
                                }
                            });

                        }

                        @Override
                        public void onError(UploadImageUsecase.ErrorValue errorResponse) {
                            if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                                view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                            } else {
                                view.showError(errorResponse.getDescription());

                            }
                            view.hideProgressBar();

                        }
                    });

        } else {
            localRepository.updateStatusCustomerImageById(imageModel.getId()).subscribe(new Action1<String>() {
                @Override
                public void call(String s) {
                    if (positionImage < imageList.size() - 1) {
                        positionImage++;
                        uploadImage(customerList, list, imageList);
                    } else {
                        if (positionCustomer < customerList.size() - 1) {
                            positionCustomer++;
                            uploadCustomerGift(customerList, list);
                        } else {
                            localRepository.getListWaitingUpload(userEntity.getTeamOutletId(), userEntity.getOutlet().getOutletId()).subscribe(new Action1<LinkedHashMap<String, String>>() {
                                @Override
                                public void call(LinkedHashMap<String, String> list) {
                                    List<String> keys = new ArrayList<>(list.keySet());
                                    positionUpload = 0;
                                    uploadAllData(keys, list, userEntity.getTeamOutletId(), userEntity.getOutlet().getOutletId(), false);
                                }
                            });
                        }

                    }
                }
            });
        }

    }

    private int positionUpload = 0;

    public void uploadAllData(List<String> keys, LinkedHashMap<String, String> list, int teamId, int outletId, boolean first) {

        String key = keys.get(positionUpload);
        String json = (String) list.get(key);
        if (key.equals(CustomerProductModel.class.getName())) {
            addCustomerProduct(json, teamId, outletId, keys, list);
        } else if (key.equals(CustomerGiftModel.class.getName())) {
            addCustomerGift(json, teamId, outletId, keys, list);
        } else if (key.equals(CustomerImageModel.class.getName())) {
            addCustomerImage(json, teamId, outletId, keys, list);
        } else if (key.equals(CurrentGiftModel.class.getName())) {
            uploadCurrentGift(json, teamId, outletId, keys, list);
        } else if (key.equals(CurrentBrandModel.class.getName())) {
            uploadCurrentBrand(json, teamId, outletId, keys, list);
        } else {
            positionUpload = positionUpload + 1;
            uploadAllData(keys, list, teamId, outletId, false);
        }
    }

    public void addCustomerProduct(String json, int teamId, int outletId, List<String> keys, LinkedHashMap<String, String> list) {

        addCustomerProductUsecase.executeIO(new AddCustomerProductUsecase.RequestValue(json),
                new BaseUseCase.UseCaseCallback<AddCustomerProductUsecase.ResponseValue,
                        AddCustomerProductUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(AddCustomerProductUsecase.ResponseValue successResponse) {

                        localRepository.updateStatusCustomerProduct(outletId).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                if (positionUpload < list.size() - 1) {
                                    positionUpload++;
                                    uploadAllData(keys, list, teamId, outletId, false);
                                } else {

                                    //view.showSuccess(CoreApplication.getInstance().getString(R.string.upload_data_success));
                                    sendRequestGift();
                                }
                            }
                        });

                    }

                    @Override
                    public void onError(AddCustomerProductUsecase.ErrorValue errorResponse) {
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                        view.showUploadRetry();
                        view.hideProgressBar();
                    }
                });
    }

    public void addCustomerGift(String json, int teamId, int outletId, List<String> keys, LinkedHashMap<String, String> list) {
        addCustomerGiftUsecase.executeIO(new AddCustomerGiftUsecase.RequestValue(json),
                new BaseUseCase.UseCaseCallback<AddCustomerGiftUsecase.ResponseValue,
                        AddCustomerGiftUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(AddCustomerGiftUsecase.ResponseValue successResponse) {
                        localRepository.updateStatusCustomerGift(outletId).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                if (positionUpload < list.size() - 1) {
                                    positionUpload++;
                                    uploadAllData(keys, list, teamId, outletId, false);
                                } else {

                                   // view.showSuccess(CoreApplication.getInstance().getString(R.string.upload_data_success));
                                    sendRequestGift();
                                }
                            }
                        });

                    }

                    @Override
                    public void onError(AddCustomerGiftUsecase.ErrorValue errorResponse) {
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                        view.showUploadRetry();
                        view.hideProgressBar();
                    }
                });
    }

    public void addCustomerImage(String json, int teamId, int outletId, List<String> keys, LinkedHashMap<String, String> list) {
        addCustomerImageUsecase.executeIO(new AddCustomerImageUsecase.RequestValue(json),
                new BaseUseCase.UseCaseCallback<AddCustomerImageUsecase.ResponseValue,
                        AddCustomerImageUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(AddCustomerImageUsecase.ResponseValue successResponse) {
                        localRepository.updateStatusCustomerImage(outletId).subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer size) {

                                if (positionUpload < list.size() - 1) {
                                    positionUpload++;
                                    uploadAllData(keys, list, teamId, outletId, true);
                                } else {

                                   // view.showSuccess(CoreApplication.getInstance().getString(R.string.upload_data_success));
                                    sendRequestGift();
                                }
                            }
                        });

                    }

                    @Override
                    public void onError(AddCustomerImageUsecase.ErrorValue errorResponse) {
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());

                        }
                        view.showUploadRetry();
                        view.hideProgressBar();
                    }
                });
    }

    private void uploadCurrentGift(String json, int teamId, int outletId, List<String> keys, LinkedHashMap<String, String> list) {
        updateCurrentGiftUsecase.executeIO(new UpdateCurrentGiftUsecase.RequestValue(json),
                new BaseUseCase.UseCaseCallback<UpdateCurrentGiftUsecase.ResponseValue,
                        UpdateCurrentGiftUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(UpdateCurrentGiftUsecase.ResponseValue successResponse) {
                        localRepository.updateStatusCurrentGift(outletId).subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                if (positionUpload < list.size() - 1) {
                                    positionUpload++;
                                    uploadAllData(keys, list, teamId, outletId, true);
                                } else {
                                    //view.showSuccess(CoreApplication.getInstance().getString(R.string.upload_data_success));
                                    sendRequestGift();
                                }
                            }
                        });


                    }

                    @Override
                    public void onError(UpdateCurrentGiftUsecase.ErrorValue errorResponse) {
                        view.showError(errorResponse.getDescription());
                        view.showUploadRetry();
                        view.hideProgressBar();

                    }
                });
    }

    private void uploadCurrentBrand(String json, int teamId, int outletId, List<String> keys, LinkedHashMap<String, String> list) {
        addBrandSetUsedUsecase.executeIO(new AddBrandSetUsedUsecase.RequestValue(json),
                new BaseUseCase.UseCaseCallback<AddBrandSetUsedUsecase.ResponseValue,
                        AddBrandSetUsedUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(AddBrandSetUsedUsecase.ResponseValue successResponse) {
                        localRepository.updateStatusCurrentBrand(outletId).subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {

                                if (positionUpload < list.size() - 1) {
                                    positionUpload++;
                                    uploadAllData(keys, list, teamId, outletId, true);
                                } else {

                                  //  view.showSuccess(CoreApplication.getInstance().getString(R.string.upload_data_success));
                                    sendRequestGift();

                                }

                            }
                        });


                    }

                    @Override
                    public void onError(AddBrandSetUsedUsecase.ErrorValue errorResponse) {
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                        view.hideProgressBar();
                        view.showUploadRetry();
                    }
                });
    }

    private void sendRequestGift(){
        sendRequestGiftUsecase.executeIO(new SendRequestGiftUsecase.RequestValue(UserManager.getInstance().getUser().getUserId(), integerList),
                new BaseUseCase.UseCaseCallback<SendRequestGiftUsecase.ResponseValue,
                        SendRequestGiftUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(SendRequestGiftUsecase.ResponseValue successResponse) {
                      view.showSuccess("Gửi yêu cầu quà thành công");
                        view.hideProgressBar();
                    }

                    @Override
                    public void onError(SendRequestGiftUsecase.ErrorValue errorResponse) {
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                        view.hideProgressBar();
                        view.showUploadRetry();
                    }
                });
    }

}
