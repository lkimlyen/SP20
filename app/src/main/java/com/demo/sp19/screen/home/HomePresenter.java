package com.demo.sp19.screen.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.CurrentBrandModel;
import com.demo.architect.data.model.offline.CurrentGiftModel;
import com.demo.architect.data.model.offline.CustomerGiftMegaModel;
import com.demo.architect.data.model.offline.CustomerGiftModel;
import com.demo.architect.data.model.offline.CustomerImageModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.CustomerProductModel;
import com.demo.architect.data.model.offline.ImageModel;
import com.demo.architect.data.model.offline.NoteStockModel;
import com.demo.architect.data.model.offline.POSMReportModel;
import com.demo.architect.data.model.offline.StockModel;
import com.demo.architect.data.model.offline.TakeOffVolumnModel;
import com.demo.architect.data.model.offline.TotalRotationModel;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.domain.AddBrandSetUsedUsecase;
import com.demo.architect.domain.AddCustomerGiftMegaUsecase;
import com.demo.architect.domain.AddCustomerGiftUsecase;
import com.demo.architect.domain.AddCustomerImageUsecase;
import com.demo.architect.domain.AddCustomerProductUsecase;
import com.demo.architect.domain.AddCustomerTotalDialMegaUsecase;
import com.demo.architect.domain.AddCustomerUsecase;
import com.demo.architect.domain.AddPOSMUsecase;
import com.demo.architect.domain.AddTakeOffVolumnUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.UpdateCurrentGiftUsecase;
import com.demo.architect.domain.UpdateStockUsecase;
import com.demo.architect.domain.UpdateTypeUsecase;
import com.demo.architect.domain.UploadImageUsecase;
import com.demo.architect.utils.view.FileUtils;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.manager.ResetDataManager;
import com.demo.sp19.manager.UserManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class HomePresenter implements com.demo.sp19.screen.home.HomeContract.Presenter {

    private final String TAG = HomePresenter.class.getName();
    private final com.demo.sp19.screen.home.HomeContract.View view;
    private final UpdateStockUsecase updateStockUsecase;
    private final AddTakeOffVolumnUsecase addTakeOffVolumnUsecase;
    private final AddCustomerUsecase addCustomerUsecase;
    private final UploadImageUsecase uploadImageUsecase;
    private final AddCustomerProductUsecase addCustomerProductUsecase;
    private final AddCustomerGiftUsecase addCustomerGiftUsecase;
    private final AddCustomerImageUsecase addCustomerImageUsecase;
    private final UpdateCurrentGiftUsecase updateCurrentGiftUsecase;
    private final AddBrandSetUsedUsecase addBrandSetUsedUsecase;
    private final AddPOSMUsecase addPOSMUsecase;
    private final UpdateTypeUsecase updateTypeUsecase;
    private final AddCustomerTotalDialMegaUsecase addCustomerTotalDialMegaUsecase;
    private int countUploaded = 0;
    private AddCustomerGiftMegaUsecase addCustomerGiftMegaUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    HomePresenter(@NonNull com.demo.sp19.screen.home.HomeContract.View view, UpdateStockUsecase updateStockUsecase,
                  AddTakeOffVolumnUsecase addTakeOffVolumnUsecase, AddCustomerUsecase addCustomerUsecase,
                  UploadImageUsecase uploadImageUsecase, AddCustomerProductUsecase addCustomerProductUsecase,
                  AddCustomerGiftUsecase addCustomerGiftUsecase, AddCustomerImageUsecase addCustomerImageUsecase,
                  UpdateCurrentGiftUsecase updateCurrentGiftUsecase, AddBrandSetUsedUsecase addBrandSetUsedUsecase, AddPOSMUsecase addPOSMUsecase, UpdateTypeUsecase updateTypeUsecase, AddCustomerTotalDialMegaUsecase addCustomerTotalDialMegaUsecase) {
        this.view = view;
        this.updateStockUsecase = updateStockUsecase;
        this.addTakeOffVolumnUsecase = addTakeOffVolumnUsecase;
        this.addCustomerUsecase = addCustomerUsecase;
        this.uploadImageUsecase = uploadImageUsecase;
        this.addCustomerProductUsecase = addCustomerProductUsecase;
        this.addCustomerGiftUsecase = addCustomerGiftUsecase;
        this.addCustomerImageUsecase = addCustomerImageUsecase;
        this.updateCurrentGiftUsecase = updateCurrentGiftUsecase;
        this.addBrandSetUsedUsecase = addBrandSetUsedUsecase;
        this.addPOSMUsecase = addPOSMUsecase;
        this.updateTypeUsecase = updateTypeUsecase;
        this.addCustomerTotalDialMegaUsecase = addCustomerTotalDialMegaUsecase;
    }

    @Inject
    public void setupPresenter() {
        view.setPresenter(this);
    }


    @Override
    public void start() {
        //getListLogo();
        countNumberUpload();
        Log.d(TAG, TAG + ".start() called");
    }

    @Override
    public void stop() {
        Log.d(TAG, TAG + ".stop() called");
    }


    @Override
    public void getListLogo() {
        List<Integer> list = new ArrayList<>();
        view.showListLogo(list);
    }

    @Override
    public void countNumberUpload() {
        UserEntity userEntity = UserManager.getInstance().getUser();
        localRepository.countNumberWaitingUpload(userEntity.getTeamOutletId(), userEntity.getOutlet().getOutletId())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        view.showCountNumberWaiting(integer);
                    }
                });
    }

    @Override
    public void uploadAllData() {
        UserEntity userEntity = UserManager.getInstance().getUser();
        localRepository.getListCustomerWaitingUpload(userEntity.getOutlet().getOutletId()).subscribe(new Action1<LinkedHashMap<CustomerModel, List<ImageModel>>>() {
            @Override
            public void call(LinkedHashMap<CustomerModel, List<ImageModel>> customerModelListLinkedHashMap) {
                countUploaded = 0;
                view.showDialogDownload(0, countUploaded);
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
                                view.hideDialogDownload();
                            }

                        }
                    });
                }

            }
        });

    }

    @Override
    public void getInfoUser() {

        view.showInfoUser(UserManager.getInstance().getUser());
    }

    private int positionUpload = 0;

    public void uploadAllData(List<String> keys, LinkedHashMap<String, String> list, int teamId, int outletId, boolean first) {
        if (first) {
            countUploaded = 0;
            view.showDialogDownload(0, countUploaded);
        }
        String key = keys.get(positionUpload);
        String json = (String) list.get(key);
        if (key.equals(StockModel.class.getName())) {
            uploadStock(json, teamId, outletId, keys, list);
        } else if (key.equals(NoteStockModel.class.getName())) {
            uploadNoteStock(json, teamId, outletId, keys, list);

        } else if (key.equals(TakeOffVolumnModel.class.getName())) {
            uploadTakeOffVolumn(json, teamId, outletId, keys, list);
        } else if (key.equals(CustomerProductModel.class.getName())) {
            addCustomerProduct(json, teamId, outletId, keys, list);
        } else if (key.equals(CustomerGiftModel.class.getName())) {
            addCustomerGift(json, teamId, outletId, keys, list);
        } else if (key.equals(CustomerImageModel.class.getName())) {
            addCustomerImage(json, teamId, outletId, keys, list);
        } else if (key.equals(CurrentGiftModel.class.getName())) {
            uploadCurrentGift(json, teamId, outletId, keys, list);
        } else if (key.equals(CurrentBrandModel.class.getName())) {
            uploadCurrentBrand(json, teamId, outletId, keys, list);
        } else if (key.equals(POSMReportModel.class.getName())) {
            uploadPOSMReport(json, teamId, outletId, keys, list);
        } else if (key.equals(TotalRotationModel.class.getName())) {
            addCustomerTotalRotaioNMega(json, teamId, outletId, keys, list);
        }else if (key.equals(CustomerGiftMegaModel.class.getName())){
            addCustomerGiftMega(json, teamId, outletId, keys, list);
        }
    }

    private void uploadNoteStock(String json, int teamId, int outletId, List<String> keys, LinkedHashMap<String, String> list) {
        updateTypeUsecase.executeIO(new UpdateTypeUsecase.RequestValue(json),
                new BaseUseCase.UseCaseCallback<UpdateTypeUsecase.ResponseValue,
                        UpdateTypeUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(UpdateTypeUsecase.ResponseValue successResponse) {

                        localRepository.updateStatusNoteStock(outletId).subscribe(new Action1<String>() {
                            @Override
                            public void call(String size) {
                                countUploaded += 1;
                                view.updateDialogDownload(countUploaded, 100);
                                if (positionUpload < list.size() - 1) {
                                    positionUpload++;
                                    uploadAllData(keys, list, teamId, outletId, true);
                                } else {
                                    view.updateDialogDownload(countUploaded, 100);
                                    view.hideDialogDownload();
                                    countNumberUpload();
                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.upload_data_success));

                                    ResetDataManager.getInstance().setResetData(0);
                                }

                            }
                        });

                    }

                    @Override
                    public void onError(UpdateTypeUsecase.ErrorValue errorResponse) {
                        view.showError(errorResponse.getDescription());
                        view.showButtonRetry();
                    }
                });
    }

    private void uploadPOSMReport(String json, int teamId, int outletId, List<String> keys, LinkedHashMap<String, String> list) {
        addPOSMUsecase.executeIO(new AddPOSMUsecase.RequestValue(json),
                new BaseUseCase.UseCaseCallback<AddPOSMUsecase.ResponseValue,
                        AddPOSMUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(AddPOSMUsecase.ResponseValue successResponse) {
                        localRepository.updateStatusPOSM(outletId).subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                countUploaded += integer;
                                view.updateDialogDownload(countUploaded, 100);
                                if (positionUpload < list.size() - 1) {
                                    positionUpload++;
                                    uploadAllData(keys, list, teamId, outletId, true);
                                } else {
                                    view.updateDialogDownload(countUploaded, 100);
                                    view.hideDialogDownload();
                                    countNumberUpload();
                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.upload_data_success));

                                    ResetDataManager.getInstance().setResetData(0);
                                }
                            }
                        });


                    }

                    @Override
                    public void onError(AddPOSMUsecase.ErrorValue errorResponse) {
                        view.showError(errorResponse.getDescription());
                        view.showButtonRetry();
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
                                countUploaded += integer;
                                view.updateDialogDownload(countUploaded, 100);
                                if (positionUpload < list.size() - 1) {
                                    positionUpload++;
                                    uploadAllData(keys, list, teamId, outletId, true);
                                } else {
                                    view.updateDialogDownload(countUploaded, 100);
                                    view.hideDialogDownload();
                                    countNumberUpload();
                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.upload_data_success));

                                    ResetDataManager.getInstance().setResetData(0);
                                }
                            }
                        });


                    }

                    @Override
                    public void onError(UpdateCurrentGiftUsecase.ErrorValue errorResponse) {
                        view.showError(errorResponse.getDescription());
                        view.showButtonRetry();
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
                                countUploaded += integer;
                                view.updateDialogDownload(countUploaded, 100);
                                if (positionUpload < list.size() - 1) {
                                    positionUpload++;
                                    uploadAllData(keys, list, teamId, outletId, true);
                                } else {
                                    view.updateDialogDownload(countUploaded, 100);
                                    view.hideDialogDownload();
                                    countNumberUpload();
                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.upload_data_success));
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
                        view.showButtonRetry();
                    }
                });
    }

    public void uploadStock(String json, int teamId, int outletId, List<String> keys, LinkedHashMap<String, String> list) {
        updateStockUsecase.executeIO(new UpdateStockUsecase.RequestValue(json),
                new BaseUseCase.UseCaseCallback<UpdateStockUsecase.ResponseValue,
                        UpdateStockUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(UpdateStockUsecase.ResponseValue successResponse) {

                        localRepository.updateStatusStock(outletId).subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer size) {
                                countUploaded += size;
                                view.updateDialogDownload(countUploaded, 100);
                                if (positionUpload < list.size() - 1) {
                                    positionUpload++;
                                    uploadAllData(keys, list, teamId, outletId, true);
                                } else {
                                    view.updateDialogDownload(countUploaded, 100);
                                    view.hideDialogDownload();
                                    countNumberUpload();
                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.upload_data_success));
                                }

                            }
                        });

                    }

                    @Override
                    public void onError(UpdateStockUsecase.ErrorValue errorResponse) {
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                        view.showButtonRetry();
                    }
                });
    }

    public void uploadTakeOffVolumn(String json, int teamId, int outletId, List<String> keys, LinkedHashMap<String, String> list) {

        addTakeOffVolumnUsecase.executeIO(new AddTakeOffVolumnUsecase.RequestValue(json),
                new BaseUseCase.UseCaseCallback<AddTakeOffVolumnUsecase.ResponseValue,
                        AddTakeOffVolumnUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(AddTakeOffVolumnUsecase.ResponseValue successResponse) {

                        localRepository.updateStatusTakeOffVolumn(teamId, outletId).subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer size) {
                                countUploaded += size;
                                view.updateDialogDownload(countUploaded, 100);
                                if (positionUpload < list.size() - 1) {
                                    positionUpload++;
                                    uploadAllData(keys, list, teamId, outletId, true);
                                } else {
                                    view.updateDialogDownload(countUploaded, 100);
                                    view.hideDialogDownload();
                                    countNumberUpload();
                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.upload_data_success));
                                }
                            }
                        });

                    }

                    @Override
                    public void onError(AddTakeOffVolumnUsecase.ErrorValue errorResponse) {
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                        view.showButtonRetry();
                    }
                });
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
                                    view.updateDialogDownload(countUploaded, 100);
                                    view.hideDialogDownload();
                                    countNumberUpload();
                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.upload_data_success));
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
                        view.showButtonRetry();

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
                                    view.updateDialogDownload(countUploaded, 100);
                                    view.hideDialogDownload();
                                    countNumberUpload();
                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.upload_data_success));
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
                        view.showButtonRetry();
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
                                    view.updateDialogDownload(countUploaded, 100);
                                    view.hideDialogDownload();
                                    countNumberUpload();
                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.upload_data_success));
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
                        view.showButtonRetry();
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

                                                                view.hideDialogDownload();
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
                            }
                            view.showButtonRetry();
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

                                        view.hideDialogDownload();
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
        if (bitmap != null){
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
                            view.showButtonRetry();
                        }
                    });

        }else {
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

    public void addCustomerTotalRotaioNMega(String json, int teamId, int outletId, List<String> keys, LinkedHashMap<String, String> list) {

        addCustomerTotalDialMegaUsecase.executeIO(new AddCustomerTotalDialMegaUsecase.RequestValue(json),
                new BaseUseCase.UseCaseCallback<AddCustomerTotalDialMegaUsecase.ResponseValue,
                        AddCustomerTotalDialMegaUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(AddCustomerTotalDialMegaUsecase.ResponseValue successResponse) {

                        localRepository.updateStatusTotalRotation().subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer s) {
                                countUploaded += s;
                                view.updateDialogDownload(countUploaded, 100);
                                if (positionUpload < list.size() - 1) {
                                    positionUpload++;
                                    uploadAllData(keys, list, teamId, outletId, true);
                                } else {
                                    view.updateDialogDownload(countUploaded, 100);
                                    view.hideDialogDownload();
                                    countNumberUpload();
                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.upload_data_success));

                                    ResetDataManager.getInstance().setResetData(0);
                                }
                            }
                        });

                    }

                    @Override
                    public void onError(AddCustomerTotalDialMegaUsecase.ErrorValue errorResponse) {
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                        view.showButtonRetry();

                    }
                });
    }

    public void addCustomerGiftMega(String json, int teamId, int outletId, List<String> keys, LinkedHashMap<String, String> list) {
        addCustomerGiftMegaUsecase.executeIO(new AddCustomerGiftMegaUsecase.RequestValue(json),
                new BaseUseCase.UseCaseCallback<AddCustomerGiftMegaUsecase.ResponseValue,
                        AddCustomerGiftMegaUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(AddCustomerGiftMegaUsecase.ResponseValue successResponse) {
                        localRepository.uploadStatusCustomerGiftMega().subscribe(new Action1<String>() {
                            @Override
                            public void call(String string) {
                                countUploaded += 1;
                                view.updateDialogDownload(countUploaded, 100);
                                if (positionUpload < list.size() - 1) {
                                    positionUpload++;
                                    uploadAllData(keys, list, teamId, outletId, true);
                                } else {
                                    view.updateDialogDownload(countUploaded, 100);
                                    view.hideDialogDownload();
                                    countNumberUpload();
                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.upload_data_success));
                                    ResetDataManager.getInstance().setResetData(0);
                                }
                            }
                        });

                    }

                    @Override
                    public void onError(AddCustomerGiftMegaUsecase.ErrorValue errorResponse) {
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                        view.hideProgressBar();
                    }
                });
    }
}
