package com.demo.sp19.screen.rotation_mega;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.model.offline.CustomerGiftMegaModel;
import com.demo.architect.data.model.offline.CustomerImageModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.CustomerProductModel;
import com.demo.architect.data.model.offline.ImageModel;
import com.demo.architect.data.model.offline.TimeRotationModel;
import com.demo.architect.data.model.offline.TotalRotationModel;
import com.demo.architect.domain.AddCustomerGiftMegaUsecase;
import com.demo.architect.domain.AddCustomerImageUsecase;
import com.demo.architect.domain.AddCustomerProductUsecase;
import com.demo.architect.domain.AddCustomerUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.UploadImageUsecase;
import com.demo.architect.utils.view.FileUtils;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.manager.UserManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class RotationMegaPresenter implements RotationMegaContract.Presenter {

    private final String TAG = RotationMegaPresenter.class.getName();
    private final RotationMegaContract.View view;
    private final AddCustomerUsecase addCustomerUsecase;
    private final UploadImageUsecase uploadImageUsecase;
    private final AddCustomerProductUsecase addCustomerProductUsecase;
    private final AddCustomerImageUsecase addCustomerImageUsecase;
    private final AddCustomerGiftMegaUsecase addCustomerGiftMegaUsecase;

    @Inject
    LocalRepository localRepository;

    @Inject
    RotationMegaPresenter(@NonNull RotationMegaContract.View view, AddCustomerUsecase addCustomerUsecase, UploadImageUsecase uploadImageUsecase, AddCustomerProductUsecase addCustomerProductUsecase, AddCustomerImageUsecase addCustomerImageUsecase, AddCustomerGiftMegaUsecase addCustomerGiftMegaUsecase) {
        this.view = view;
        this.addCustomerUsecase = addCustomerUsecase;
        this.uploadImageUsecase = uploadImageUsecase;
        this.addCustomerProductUsecase = addCustomerProductUsecase;
        this.addCustomerImageUsecase = addCustomerImageUsecase;
        this.addCustomerGiftMegaUsecase = addCustomerGiftMegaUsecase;
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
    public void getListGift() {
        localRepository.getListGiftMegaByDate().subscribe(new Action1<TimeRotationModel>() {
            @Override
            public void call(TimeRotationModel timeRotationModel) {
                if (timeRotationModel != null) {
                    view.showListGift(timeRotationModel);
                }
            }
        });
    }

    @Override
    public void addImageRotaion(int id, String path) {
        localRepository.addImageRotaion(id, path).subscribe();
    }

    @Override
    public void getInfoCusById(int customerId) {
        localRepository.getCustomerById(customerId).subscribe(new Action1<LinkedHashMap<CustomerModel, TotalRotationModel>>() {
            @Override
            public void call(LinkedHashMap<CustomerModel, TotalRotationModel> customerModel) {
                for (Map.Entry<CustomerModel, TotalRotationModel> map : customerModel.entrySet()) {
                    view.showInfoCus(map.getKey(), map.getValue());
                    break;
                }
            }
        });
    }

    @Override
    public void saveTotalRotaion(int cusId, int totalRotation, boolean lucky) {

        if (lucky){
            view.showProgressBar();
            localRepository.getCusLuckyUploadServer(cusId).subscribe(new Action1<LinkedHashMap<CustomerModel, List<ImageModel>>>() {
                @Override
                public void call(LinkedHashMap<CustomerModel, List<ImageModel>> customerModelListLinkedHashMap) {
                    for (Map.Entry<CustomerModel, List<ImageModel>> map : customerModelListLinkedHashMap.entrySet()) {
                        uploadCustomerLucky(map.getKey(), map.getValue(), totalRotation);
                    }
                }
            });
        }else {
            view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_success));
        }



    }

    @Override
    public void saveInfoCusLucky(int cusId, int giftId) {
        CustomerGiftMegaModel customerGiftMegaModel = new CustomerGiftMegaModel(cusId, UserManager.getInstance().getUser().getOutlet().getOutletId(),
                giftId, UserManager.getInstance().getUser().getTeamOutletId());
        localRepository.saveInfoCusLucky(customerGiftMegaModel).subscribe(new Action1<String>() {
            @Override
            public void call(String string) {
                // view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_success));
            }
        });
    }

    @Override
    public void updateNumberRotation(int customerId) {
        localRepository.updateNumberTunedRotation(customerId).subscribe(new Action1<String>() {
            @Override
            public void call(String string) {
                // view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_success));
            }
        });
    }


    public void uploadCustomerLucky(CustomerModel customerModel, List<ImageModel> list, int totalRotation) {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        String json = gson.toJson(customerModel);
        addCustomerUsecase.executeIO(new AddCustomerUsecase.RequestValue(json),
                new BaseUseCase.UseCaseCallback<AddCustomerUsecase.ResponseValue,
                        AddCustomerUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(AddCustomerUsecase.ResponseValue successResponse) {
                        localRepository.addCustomerServerId(customerModel.getId(), successResponse.getId()).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                positionImage = 0;
                                if (list.size() > 0) {

                                    uploadImage(customerModel, list, totalRotation);
                                } else {
                                    localRepository.getListProductByCustomer(customerModel.getId()).subscribe(new Action1<List<CustomerProductModel>>() {
                                        @Override
                                        public void call(List<CustomerProductModel> list) {
                                            addCustomerProduct(customerModel, list, totalRotation);
                                        }
                                    });
                                }
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
                        view.hideProgressBar();

                    }
                });
    }

    private int positionImage = 0;

    public void uploadImage(CustomerModel customerModel, List<ImageModel> imageList, int totalRotation) {
        ImageModel imageModel = imageList.get(positionImage);
        Bitmap bitmap = BitmapFactory.decodeFile(imageModel.getPath());
        bitmap = FileUtils.getResizedBitmap(bitmap, 800);
        File file = FileUtils.bitmapToFile(bitmap, imageModel.getPath());
        uploadImageUsecase.executeIO(new UploadImageUsecase.RequestValue(file, imageModel.getImageCode(),
                        imageModel.getCreatedBy(), imageModel.getDateCreate(), imageModel.getLatitude(), imageModel.getLongitude(),
                        imageModel.getImageType(), imageModel.getFileName()),
                new BaseUseCase.UseCaseCallback<UploadImageUsecase.ResponseValue, UploadImageUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(UploadImageUsecase.ResponseValue successResponse) {
                        localRepository.addCustomerImageServerId(customerModel.getId(),
                                imageModel.getId(), successResponse.getId()).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                if (positionImage < imageList.size() - 1) {
                                    positionImage++;
                                    uploadImage(customerModel, imageList, totalRotation);
                                } else {
                                    localRepository.getListProductByCustomer(customerModel.getId()).subscribe(new Action1<List<CustomerProductModel>>() {
                                        @Override
                                        public void call(List<CustomerProductModel> list) {
                                            addCustomerProduct(customerModel, list, totalRotation);
                                        }
                                    });

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
                    }
                });

    }

    public void addCustomerProduct(CustomerModel customerModel, List<CustomerProductModel> list, int totalRotation) {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        String json = gson.toJson(list);
        addCustomerProductUsecase.executeIO(new AddCustomerProductUsecase.RequestValue(json),
                new BaseUseCase.UseCaseCallback<AddCustomerProductUsecase.ResponseValue,
                        AddCustomerProductUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(AddCustomerProductUsecase.ResponseValue successResponse) {
                        localRepository.getListImageByCustomer(customerModel.getId()).subscribe(new Action1<List<CustomerImageModel>>() {
                            @Override
                            public void call(List<CustomerImageModel> list) {
                                addCustomerImage(customerModel, list, totalRotation);
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

                        view.hideProgressBar();
                    }
                });
    }

    public void addCustomerImage(CustomerModel customerModel, List<CustomerImageModel> list, int totalRotation) {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        String json = gson.toJson(list);
        addCustomerImageUsecase.executeIO(new AddCustomerImageUsecase.RequestValue(json),
                new BaseUseCase.UseCaseCallback<AddCustomerImageUsecase.ResponseValue,
                        AddCustomerImageUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(AddCustomerImageUsecase.ResponseValue successResponse) {
                        localRepository.getCustomerGiftMega(customerModel.getId()).subscribe(new Action1<CustomerGiftMegaModel>() {
                            @Override
                            public void call(CustomerGiftMegaModel customerGiftMegaModel) {
                                addCustomerGiftMega(customerGiftMegaModel, totalRotation);
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
                        view.hideProgressBar();
                    }
                });
    }

    public void addCustomerGiftMega(CustomerGiftMegaModel customerGiftMegaModel, int totalRotation) {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        String json = gson.toJson(customerGiftMegaModel);
        addCustomerGiftMegaUsecase.executeIO(new AddCustomerGiftMegaUsecase.RequestValue(json),
                new BaseUseCase.UseCaseCallback<AddCustomerGiftMegaUsecase.ResponseValue,
                        AddCustomerGiftMegaUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(AddCustomerGiftMegaUsecase.ResponseValue successResponse) {
                        localRepository.uploadStatusCustomerGiftMega().subscribe(new Action1<String>() {
                            @Override
                            public void call(String string) {
                                view.hideProgressBar();
                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_save_success));
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
