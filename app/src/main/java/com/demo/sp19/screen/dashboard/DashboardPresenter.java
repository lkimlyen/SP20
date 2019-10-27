package com.demo.sp19.screen.dashboard;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.BackgroundDialEntity;
import com.demo.architect.data.model.GiftEntity;
import com.demo.architect.data.model.OutletEntiy;
import com.demo.architect.data.model.POSMEntity;
import com.demo.architect.data.model.ProductEntity;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.GiftMegaModel;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetBackgroundUsecase;
import com.demo.architect.domain.GetBrandSetDetailUsecase;
import com.demo.architect.domain.GetBrandSetUsecase;
import com.demo.architect.domain.GetBrandUsecase;
import com.demo.architect.domain.GetCurrentBrandSetUsecase;
import com.demo.architect.domain.GetDataLuckyMegaUsecase;
import com.demo.architect.domain.GetEmergencyUsecase;
import com.demo.architect.domain.GetGiftUsecase;
import com.demo.architect.domain.GetOutletBrandUsecase;
import com.demo.architect.domain.GetPOSMUsecase;
import com.demo.architect.domain.GetProductGiftUsecase;
import com.demo.architect.domain.GetProductUsecase;
import com.demo.architect.domain.GetVersionUsecase;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.manager.OutletBrandManager;
import com.demo.sp19.manager.OutletDownloadManager;
import com.demo.sp19.manager.UserManager;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class DashboardPresenter implements DashboardContract.Presenter {
    private final String TAG = DashboardPresenter.class.getName();
    private final DashboardContract.View view;
    private final GetProductUsecase getProductUsecase;
    private final GetBrandSetDetailUsecase getBrandSetDetailUsecase;
    private final GetBrandUsecase getBrandUsecase;
    private final GetBrandSetUsecase getBrandSetUsecase;
    private final GetGiftUsecase getGiftUsecase;
    private final GetOutletBrandUsecase getOutletBrandUsecase;
    private final GetBackgroundUsecase getBackgroundUsecase;
    private final GetCurrentBrandSetUsecase getCurrentBrandSetUsecase;
    private final GetProductGiftUsecase getProductGiftUsecase;
    private final GetEmergencyUsecase getEmergencyUsecase;
    private final GetPOSMUsecase getPOSMUsecase;
    private final GetVersionUsecase getVersionUsecase;
    private final GetDataLuckyMegaUsecase getDataLuckyMegaUsecase;

    private int positionTask = 0;
    @Inject
    LocalRepository localRepository;

    @Inject
    DashboardPresenter(@NonNull DashboardContract.View view, GetProductUsecase getProductUsecase, GetBrandSetDetailUsecase getBrandSetDetailUsecase, GetBrandUsecase getBrandUsecase, GetBrandSetUsecase getBrandSetUsecase, GetGiftUsecase getGiftUsecase, GetOutletBrandUsecase getOutletBrandUsecase, GetBackgroundUsecase getBackgroundUsecase, GetCurrentBrandSetUsecase getCurrentBrandSetUsecase, GetProductGiftUsecase getProductGiftUsecase, GetEmergencyUsecase getEmergencyUsecase, GetPOSMUsecase getPOSMUsecase, GetVersionUsecase getVersionUsecase, GetDataLuckyMegaUsecase getDataLuckyMegaUsecase) {
        this.view = view;
        this.getProductUsecase = getProductUsecase;
        this.getBrandSetDetailUsecase = getBrandSetDetailUsecase;
        this.getBrandUsecase = getBrandUsecase;
        this.getBrandSetUsecase = getBrandSetUsecase;
        this.getGiftUsecase = getGiftUsecase;
        this.getOutletBrandUsecase = getOutletBrandUsecase;
        this.getBackgroundUsecase = getBackgroundUsecase;
        this.getCurrentBrandSetUsecase = getCurrentBrandSetUsecase;
        this.getProductGiftUsecase = getProductGiftUsecase;
        this.getEmergencyUsecase = getEmergencyUsecase;
        this.getPOSMUsecase = getPOSMUsecase;
        this.getVersionUsecase = getVersionUsecase;
        this.getDataLuckyMegaUsecase = getDataLuckyMegaUsecase;

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
    public void getListProduct() {
        UserEntity userEntity = UserManager.getInstance().getUser();
        positionTask = 1;
        int totalTask = userEntity.getOutlet().isLuckyMega() ? 12 : 11;
        view.showDialogDownload(totalTask, 0, 1);
        getProductUsecase.executeIO(new GetProductUsecase.RequestValue(userEntity.getProjectId(), userEntity.getOutlet().getOutletId()),
                new BaseUseCase.UseCaseCallback<GetProductUsecase.ResponseValue,
                        GetProductUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetProductUsecase.ResponseValue successResponse) {
                        List<ProductEntity> list = successResponse.getList();
                        //xóa hết product đã được lưu trong database trc đó
                        localRepository.deleteProduct().subscribe(new Action1<String>() {
                            @Override
                            public void call(String strings) {
                                positonProduct = 0;
                                downloadImageProduct(list, userEntity.getProjectId());
                            }
                        });

                    }

                    @Override
                    public void onError(GetProductUsecase.ErrorValue errorResponse) {
                        view.showButtonRetry(positionTask);
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                    }
                });
    }

    @Override
    public void downloadFromServer() {
        PackageManager manager = CoreApplication.getInstance().getPackageManager();
        PackageInfo info;
        String version = "";
        try {
            info = manager.getPackageInfo(
                    CoreApplication.getInstance().getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        final String sVersion = version;
        UserEntity userEntity = UserManager.getInstance().getUser();
        //tải version lên server sau khi cập nhật app
        if (!SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).getVersionAppProject().equals(sVersion)) {
            view.showProgressBar();
            getVersionUsecase.executeIO(new GetVersionUsecase.RequestValue(UserManager.getInstance().getUser().getOutlet().getOutletId(),
                    version), new BaseUseCase.UseCaseCallback<GetVersionUsecase.ResponseValue, GetVersionUsecase.ErrorValue>() {
                @Override
                public void onSuccess(GetVersionUsecase.ResponseValue successResponse) {
                    view.hideProgressBar();
                    SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushVersionAppProject(sVersion);
                    if (OutletDownloadManager.getInstance().checkDownload(userEntity.getOutlet().getOutletId(), userEntity.getProjectId())) {
                        getListProduct();
                    }
                }

                @Override
                public void onError(GetVersionUsecase.ErrorValue errorResponse) {
                    view.hideProgressBar();
                    if (OutletDownloadManager.getInstance().checkDownload(userEntity.getOutlet().getOutletId(), userEntity.getProjectId())) {
                        getListProduct();
                    }

                }
            });
        } else {
            //check xem đã download dữ liệu về chưa
            if (OutletDownloadManager.getInstance().checkDownload(userEntity.getOutlet().getOutletId(), userEntity.getProjectId())) {
                getListProduct();
            }
        }


    }

    @Override
    public void downloadTask(int positionTask) {

        UserEntity userEntity = UserManager.getInstance().getUser();
        switch (positionTask) {
            case 1:
                //lấy ds sản phẩm
                getListProduct();
                break;
            case 2:
                //lấy ds quà
                getGift(userEntity.getProjectId());
                break;
            case 3:
                //lấy danh sách nhãn hiệu
                getBrand(userEntity.getProjectId());
                break;
            case 4:
//các set quà cửa 1 brand
                getBrandSet(userEntity.getProjectId());
                break;
            case 5:
                //số lượng quà trong 1 set
                getBrandSetDetail(userEntity.getProjectId());
                break;
            case 6:
                //brand được chạy tại outlet
                getOutletBrand();
                break;
            case 7:
                //lấy background của vòng quay theo từng brand
                getBackgroundDial();
                break;
            case 8:
                //số lượng set còn lại của outlet
                getCurrentBrandSet(userEntity.getProjectId());
                break;
            case 9://lấy ds quà theo sản phẩm
                getProductGift(userEntity.getProjectId());
                break;
            case 10:
                //lấy lý do khẩn cấp
                getReason(userEntity.getProjectId());
                break;

            case 11:
                //lấy ds posm
                getPOSM();
                break;

            case 12:
                getListGiftForMega();
                break;

        }
    }

    private int positonProduct = 0;

    //download hình ảnh của từng sản phẩm
    public void downloadImageProduct(List<ProductEntity> list, int projectId) {
        ProductEntity productEntity = list.get(positonProduct);
        String url = productEntity.getFilePath();
        UserEntity userEntity = UserManager.getInstance().getUser();
        String fileName = userEntity.getOutlet() + "_" + userEntity.getProjectId() + "_" + url.substring(url.lastIndexOf('/') + 1);

        File mDownloadDir = CoreApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!mDownloadDir.exists()) {
            mDownloadDir.mkdirs();
        }
        AndroidNetworking.download(url, mDownloadDir.getPath(), fileName)
                .doNotCacheResponse()
                .build()
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        // sau khi tải hình xong thì lưu product vào database
                        File file = new File(mDownloadDir.getPath(), fileName);
//                        if (!file.exists()) {
//                            file.mkdirs();
//                        }
                        localRepository.addProductModel(productEntity, file.getPath()).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                //kiểm tra vị trí download của product
                                if (positonProduct < list.size() - 1) {
                                    positonProduct++;
                                    downloadImageProduct(list, projectId);
                                } else {
                                    view.updateDialogDownload(positionTask, 100);
                                    getGift(projectId);
                                }
                            }
                        });

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        view.showButtonRetry(positionTask);
                        if (error.getErrorDetail().contains(CoreApplication.getInstance().getString(R.string.text_connection_error))) {

                            view.showError(CoreApplication.getInstance().getString(R.string.text_connection_fail));
                        } else {

                            view.showError(error.getErrorDetail());
                        }
                    }
                });
    }

    public void getBrandSetDetail(int projectId) {
        positionTask = 5;
        view.updateDialogDownload(positionTask, 0);
        getBrandSetDetailUsecase.executeIO(new GetBrandSetDetailUsecase.RequestValue(projectId),
                new BaseUseCase.UseCaseCallback<GetBrandSetDetailUsecase.ResponseValue,
                        GetBrandSetDetailUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetBrandSetDetailUsecase.ResponseValue successResponse) {
                        localRepository.addBrandSetDetail(successResponse.getList()).subscribe(
                                new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        view.updateDialogDownload(positionTask, 100);
                                        getOutletBrand();
                                    }
                                }
                        );
                    }

                    @Override
                    public void onError(GetBrandSetDetailUsecase.ErrorValue errorResponse) {
                        view.showButtonRetry(positionTask);
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                    }
                });
    }

    public void getBrand(int projectId) {
        positionTask = 3;
        view.updateDialogDownload(positionTask, 0);
        getBrandUsecase.executeIO(new GetBrandUsecase.RequestValue(projectId),
                new BaseUseCase.UseCaseCallback<GetBrandUsecase.ResponseValue,
                        GetBrandUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetBrandUsecase.ResponseValue successResponse) {
                        localRepository.addBrand(successResponse.getList()).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                view.updateDialogDownload(positionTask, 100);
                                getBrandSet(projectId);
                            }
                        });

                    }

                    @Override
                    public void onError(GetBrandUsecase.ErrorValue errorResponse) {
                        view.showButtonRetry(positionTask);
                        view.showError(errorResponse.getDescription());
                    }
                });
    }

    public void getBrandSet(int projectId) {
        positionTask = 4;
        view.updateDialogDownload(positionTask, 0);
        getBrandSetUsecase.executeIO(new GetBrandSetUsecase.RequestValue(projectId),
                new BaseUseCase.UseCaseCallback<GetBrandSetUsecase.ResponseValue,
                        GetBrandSetUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetBrandSetUsecase.ResponseValue successResponse) {
                        localRepository.addBrandSet(successResponse.getList()).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                view.updateDialogDownload(positionTask, 100);
                                getBrandSetDetail(projectId);
                            }
                        });

                    }

                    @Override
                    public void onError(GetBrandSetUsecase.ErrorValue errorResponse) {
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                    }
                });
    }


    public void getGift(int projectId) {
        positionTask = 2;
        view.updateDialogDownload(positionTask, 0);

        getGiftUsecase.executeIO(new GetGiftUsecase.RequestValue(projectId),
                new BaseUseCase.UseCaseCallback<GetGiftUsecase.ResponseValue,
                        GetGiftUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetGiftUsecase.ResponseValue successResponse) {
                        List<GiftEntity> list = successResponse.getList();
                        localRepository.deleteGift().subscribe(new Action1<String>() {
                            @Override
                            public void call(String strings) {
                                positonGift = 0;
                                downloadImageGift(list, projectId);
                            }
                        });
                    }

                    @Override
                    public void onError(GetGiftUsecase.ErrorValue errorResponse) {
                        view.showButtonRetry(positionTask);
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                    }
                });
    }

    private int positonGift = 0;

    public void downloadImageGift(List<GiftEntity> list, int projectId) {
        GiftEntity giftEntity = list.get(positonGift);
        String url = giftEntity.getFilePath();
        UserEntity userEntity = UserManager.getInstance().getUser();
        String fileName = userEntity.getOutlet() + "_" + userEntity.getProjectId() + "_" + url.substring(url.lastIndexOf('/') + 1);
        File mDownloadDir = CoreApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!mDownloadDir.exists()) {
            mDownloadDir.mkdirs();
        }
        AndroidNetworking.download(url, mDownloadDir.getPath(), fileName)
                .doNotCacheResponse()
                .build()
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        File file = new File(mDownloadDir.getPath(), fileName);
//                        if (!file.exists()) {
//                            file.mkdirs();
//                        }
                        localRepository.addGiftModel(giftEntity, file.getPath()).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                if (positonGift < list.size() - 1) {
                                    positonGift++;
                                    downloadImageGift(list, projectId);
                                } else {
                                    view.updateDialogDownload(positionTask, 100);
                                    getBrand(projectId);
                                }
                            }
                        });

                    }

                    @Override
                    public void onError(ANError error) {
                        view.showButtonRetry(positionTask);
                        if (error.getErrorDetail().contains(CoreApplication.getInstance().getString(R.string.text_connection_error))) {

                            view.showError(CoreApplication.getInstance().getString(R.string.text_connection_fail));
                        } else {

                            view.showError(error.getErrorDetail());
                        }
                    }
                });
    }

    public void getOutletBrand() {
        positionTask = 6;
        view.updateDialogDownload(positionTask, 0);

        OutletEntiy outletEntiy = UserManager.getInstance().getUser().getOutlet();
        getOutletBrandUsecase.executeIO(new GetOutletBrandUsecase.RequestValue(outletEntiy.getOutletId()),
                new BaseUseCase.UseCaseCallback<GetOutletBrandUsecase.ResponseValue, GetOutletBrandUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetOutletBrandUsecase.ResponseValue successResponse) {
                        view.updateDialogDownload(positionTask, 100);
                        // lấy ds brand chạy của các outlet  từ sharepreference thông qua class manager
                        LinkedHashMap<Integer, List<Integer>> listLinkedHashMap = OutletBrandManager.getInstance().getAllOutletBrandList();
                        if (listLinkedHashMap == null) {
                            listLinkedHashMap = new LinkedHashMap<>();
                        }

                        listLinkedHashMap.put(outletEntiy.getOutletId(), successResponse.getList());
                        // lưu ds brand chạy của các outlet  vào sharepreference thông qua class manager
                        OutletBrandManager.getInstance().setOutletBrandList(listLinkedHashMap);
                        getBackgroundDial();
                    }

                    @Override
                    public void onError(GetOutletBrandUsecase.ErrorValue errorResponse) {
                        view.showButtonRetry(positionTask);
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                    }
                });
    }

    public void getBackgroundDial() {
        positionTask = 7;
        view.updateDialogDownload(positionTask, 0);
        UserEntity userEntity = UserManager.getInstance().getUser();
        getBackgroundUsecase.executeIO(new GetBackgroundUsecase.RequestValue(userEntity.getProjectId()),
                new BaseUseCase.UseCaseCallback<GetBackgroundUsecase.ResponseValue, GetBackgroundUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetBackgroundUsecase.ResponseValue successResponse) {
                        localRepository.deleteAllBackgroundDial().subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                positionBGDial = 0;
                                downloadBackgroundDial(successResponse.getList(), userEntity.getProjectId());
                            }
                        });


                    }

                    @Override
                    public void onError(GetBackgroundUsecase.ErrorValue errorResponse) {
                        view.showButtonRetry(positionTask);
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                    }
                });
    }

    private int positionBGDial = 0;

    public void downloadBackgroundDial(List<BackgroundDialEntity> list, int projectId) {
        BackgroundDialEntity backgroundDialEntity = list.get(positionBGDial);
        String urlBGArrow = backgroundDialEntity.getBgArrow();
        UserEntity userEntity = UserManager.getInstance().getUser();
        String fileNameArrow = userEntity.getOutlet() + "_" + userEntity.getProjectId() + "_" + urlBGArrow.substring(urlBGArrow.lastIndexOf('/') + 1);
        File mDownloadDir = CoreApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!mDownloadDir.exists()) {
            mDownloadDir.mkdirs();
        }
        AndroidNetworking.download(urlBGArrow, mDownloadDir.getPath(), fileNameArrow)
                .doNotCacheResponse()
                .build()
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        File fileArrow = new File(mDownloadDir.getPath(), fileNameArrow);
                        String urlBGButton = backgroundDialEntity.getBgButton();
                        String fileNameButton = userEntity.getOutlet() + "_" + userEntity.getProjectId() + "_" + urlBGButton.substring(urlBGButton.lastIndexOf('/') + 1);

                        AndroidNetworking.download(urlBGButton, mDownloadDir.getPath(), fileNameButton)
                                .doNotCacheResponse()
                                .build()
                                .startDownload(new DownloadListener() {
                                    @Override
                                    public void onDownloadComplete() {
                                        File fileButton = new File(mDownloadDir.getPath(), fileNameButton);
                                        String urlBGLayout = backgroundDialEntity.getBgLayout();
                                        String fileNameLayout = userEntity.getOutlet() + "_" + userEntity.getProjectId() + "_" + urlBGLayout.substring(urlBGLayout.lastIndexOf('/') + 1);

                                        AndroidNetworking.download(urlBGLayout, mDownloadDir.getPath(), fileNameLayout)
                                                .doNotCacheResponse()
                                                .build()
                                                .startDownload(new DownloadListener() {
                                                    @Override
                                                    public void onDownloadComplete() {
                                                        File fileLayout = new File(mDownloadDir.getPath(), fileNameLayout);

                                                        String urlBGCircle = backgroundDialEntity.getBgCircle();
                                                        String fileNameCircle = userEntity.getOutlet() + "_" + userEntity.getProjectId() + "_" + urlBGCircle.substring(urlBGCircle.lastIndexOf('/') + 1);

                                                        AndroidNetworking.download(urlBGCircle, mDownloadDir.getPath(), fileNameCircle)
                                                                .doNotCacheResponse()
                                                                .build()
                                                                .startDownload(new DownloadListener() {
                                                                    @Override
                                                                    public void onDownloadComplete() {
                                                                        File fileCircle = new File(mDownloadDir.getPath(), fileNameCircle);
                                                                        localRepository.addBackgroundDial(backgroundDialEntity,
                                                                                fileArrow.getPath(), fileButton.getPath(), fileLayout.getPath(), fileCircle.getPath()).subscribe(new Action1<String>() {
                                                                            @Override
                                                                            public void call(String s) {
                                                                                if (positionBGDial < list.size() - 1) {
                                                                                    positionBGDial++;
                                                                                    downloadBackgroundDial(list, projectId);
                                                                                } else {
                                                                                    view.updateDialogDownload(positionTask, 100);
                                                                                    SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).pushProjectDown(projectId);
                                                                                    getCurrentBrandSet(projectId);
                                                                                }
                                                                            }
                                                                        });
                                                                    }

                                                                    @Override
                                                                    public void onError(ANError error) {
                                                                        view.showButtonRetry(positionTask);
                                                                        if (error.getErrorDetail().contains(CoreApplication.getInstance().getString(R.string.text_connection_error))) {

                                                                            view.showError(CoreApplication.getInstance().getString(R.string.text_connection_fail));
                                                                        } else {

                                                                            view.showError(error.getErrorDetail());
                                                                        }
                                                                    }
                                                                });
                                                    }

                                                    @Override
                                                    public void onError(ANError error) {
                                                        // handle error
                                                        view.showButtonRetry(positionTask);
                                                        if (error.getErrorDetail().contains(CoreApplication.getInstance().getString(R.string.text_connection_error))) {

                                                            view.showError(CoreApplication.getInstance().getString(R.string.text_connection_fail));
                                                        } else {

                                                            view.showError(error.getErrorDetail());
                                                        }
                                                    }
                                                });

                                    }

                                    @Override
                                    public void onError(ANError error) {
                                        // handle error
                                        view.showButtonRetry(positionTask);
                                        if (error.getErrorDetail().contains(CoreApplication.getInstance().getString(R.string.text_connection_error))) {

                                            view.showError(CoreApplication.getInstance().getString(R.string.text_connection_fail));
                                        } else {

                                            view.showError(error.getErrorDetail());
                                        }
                                    }
                                });

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        view.showButtonRetry(positionTask);
                        if (error.getErrorDetail().contains(CoreApplication.getInstance().getString(R.string.text_connection_error))) {

                            view.showError(CoreApplication.getInstance().getString(R.string.text_connection_fail));
                        } else {

                            view.showError(error.getErrorDetail());
                        }
                    }
                });
    }

    public void getCurrentBrandSet(int projectId) {
        UserEntity userEntity = UserManager.getInstance().getUser();
        positionTask = 8;
        view.updateDialogDownload(positionTask, 0);
        getCurrentBrandSetUsecase.executeIO(new GetCurrentBrandSetUsecase.RequestValue(userEntity.getOutlet().getOutletId()),
                new BaseUseCase.UseCaseCallback<GetCurrentBrandSetUsecase.ResponseValue,
                        GetCurrentBrandSetUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetCurrentBrandSetUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        localRepository.addCurrentSetBrand(successResponse.getList(), userEntity.getOutlet().getOutletId(),
                                userEntity.getTeamOutletId()).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                view.updateDialogDownload(positionTask, 100);
                                getProductGift(projectId);
                            }
                        });
                    }

                    @Override
                    public void onError(GetCurrentBrandSetUsecase.ErrorValue errorResponse) {
                        view.showButtonRetry(positionTask);
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                    }
                });
    }

    public void getProductGift(int projectId) {
        positionTask = 9;
        view.updateDialogDownload(positionTask, 0);
        getProductGiftUsecase.executeIO(new GetProductGiftUsecase.RequestValue(projectId),
                new BaseUseCase.UseCaseCallback<GetProductGiftUsecase.ResponseValue,
                        GetProductGiftUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetProductGiftUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        localRepository.addProductGift(successResponse.getList()).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {

                                view.updateDialogDownload(positionTask, 100);
                                getReason(projectId);
                            }
                        });
                    }

                    @Override
                    public void onError(GetProductGiftUsecase.ErrorValue errorResponse) {
                        view.showButtonRetry(positionTask);
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                    }
                });
    }

    public void getReason(int projectId) {
        positionTask = 10;
        view.updateDialogDownload(positionTask, 0);
        getEmergencyUsecase.executeIO(new GetEmergencyUsecase.RequestValue(projectId),
                new BaseUseCase.UseCaseCallback<GetEmergencyUsecase.ResponseValue,
                        GetEmergencyUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetEmergencyUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        localRepository.addReason(successResponse.getList()).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                view.updateDialogDownload(positionTask, 100);
                                getPOSM();
                            }
                        });
                    }

                    @Override
                    public void onError(GetEmergencyUsecase.ErrorValue errorResponse) {
                        view.showButtonRetry(positionTask);
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                    }
                });
    }

    public void getPOSM() {
        positionTask = 11;
        UserEntity userEntity = UserManager.getInstance().getUser();
        view.updateDialogDownload(positionTask, 0);
        getPOSMUsecase.executeIO(new GetPOSMUsecase.RequestValue(userEntity.getOutlet().getOutletId()),
                new BaseUseCase.UseCaseCallback<GetPOSMUsecase.ResponseValue,
                        GetPOSMUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetPOSMUsecase.ResponseValue successResponse) {
                        List<POSMEntity> list = successResponse.getList();
                        localRepository.deletePOSM().subscribe(new Action1<String>() {
                            @Override
                            public void call(String strings) {
                                positonPOSM = 0;
                                if (list.size() > 0) {
                                    downloadImagePOSM(list);
                                } else {
                                    view.updateDialogDownload(positionTask, 100);
                                    view.hideDialogDownload();
                                    OutletDownloadManager.getInstance().addOutletDownloadEntity(
                                            UserManager.getInstance().getUser().getOutlet().getOutletId(),
                                            UserManager.getInstance().getUser().getProjectId());
                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_download_success));
                                }

                            }
                        });
                    }

                    @Override
                    public void onError(GetPOSMUsecase.ErrorValue errorResponse) {
                        view.showButtonRetry(positionTask);
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                    }
                });
    }

    private int positonPOSM = 0;

    public void downloadImagePOSM(List<POSMEntity> list) {
        POSMEntity posmEntity = list.get(positonPOSM);
        String url = posmEntity.getPosmAvatar();
        UserEntity userEntity = UserManager.getInstance().getUser();
        String fileName = userEntity.getOutlet() + "_" + userEntity.getProjectId() + "_" + url.substring(url.lastIndexOf('/') + 1);
        File mDownloadDir = CoreApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!mDownloadDir.exists()) {
            mDownloadDir.mkdirs();
        }
        AndroidNetworking.download(url, mDownloadDir.getPath(), fileName)
                .doNotCacheResponse()
                .build()
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        File file = new File(mDownloadDir.getPath(), fileName);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        localRepository.addPOSMModel(posmEntity, file.getPath()).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                if (positonPOSM < list.size() - 1) {
                                    positonPOSM++;
                                    downloadImagePOSM(list);
                                } else {
                                    if (userEntity.getOutlet().isLuckyMega()) {
                                        getListGiftForMega();
                                    } else {
                                        view.updateDialogDownload(positionTask, 100);
                                        view.hideDialogDownload();
                                        OutletDownloadManager.getInstance().addOutletDownloadEntity(
                                                UserManager.getInstance().getUser().getOutlet().getOutletId(),
                                                UserManager.getInstance().getUser().getProjectId());
                                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_download_success));
                                    }

                                }
                            }
                        });

                    }

                    @Override
                    public void onError(ANError error) {
                        view.showButtonRetry(positionTask);
                        if (error.getErrorDetail().contains(CoreApplication.getInstance().getString(R.string.text_connection_error))) {

                            view.showError(CoreApplication.getInstance().getString(R.string.text_connection_fail));
                        } else {

                            view.showError(error.getErrorDetail());
                        }
                    }
                });
    }

    public void getListGiftForMega() {
        positionTask = 12;
        view.updateDialogDownload(positionTask, 0);
        getDataLuckyMegaUsecase.executeIO(new GetDataLuckyMegaUsecase.RequestValue(),
                new BaseUseCase.UseCaseCallback<GetDataLuckyMegaUsecase.ResponseValue,
                        GetDataLuckyMegaUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetDataLuckyMegaUsecase.ResponseValue successResponse) {
                        localRepository.deleteGiftMega().subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                localRepository.addGiftMegaAndTime(successResponse.getList()).subscribe(new Action1<List<GiftMegaModel>>() {
                                    @Override
                                    public void call(List<GiftMegaModel> giftMegaModels) {
                                        positionGiftMega = 0;
                                        downloadImageGiftMega(giftMegaModels);
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onError(GetDataLuckyMegaUsecase.ErrorValue errorResponse) {
                        view.showButtonRetry(positionTask);
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                    }
                });

    }

    private int positionGiftMega = 0;

    public void downloadImageGiftMega(List<GiftMegaModel> list) {
        GiftMegaModel giftMegaModel = list.get(positionGiftMega);
        String url = giftMegaModel.getFilePath();
        UserEntity userEntity = UserManager.getInstance().getUser();
        String fileName = userEntity.getOutlet() + "_" + userEntity.getProjectId() + "_" + url.substring(url.lastIndexOf('/') + 1);
        File mDownloadDir = CoreApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!mDownloadDir.exists()) {
            mDownloadDir.mkdirs();
        }
        AndroidNetworking.download(url, mDownloadDir.getPath(), fileName)
                .doNotCacheResponse()
                .build()
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        File file = new File(mDownloadDir.getPath(), fileName);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        localRepository.updateFilePathGiftMega(giftMegaModel.getId(), file.getPath()).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                if (positionGiftMega < list.size() - 1) {
                                    positionGiftMega++;
                                    downloadImageGiftMega(list);
                                } else {
                                    view.updateDialogDownload(positionTask, 100);
                                    view.hideDialogDownload();
                                    OutletDownloadManager.getInstance().addOutletDownloadEntity(
                                            UserManager.getInstance().getUser().getOutlet().getOutletId(),
                                            UserManager.getInstance().getUser().getProjectId());
                                    view.showSuccess(CoreApplication.getInstance().getString(R.string.text_download_success));
                                }
                            }
                        });

                    }

                    @Override
                    public void onError(ANError error) {
                        view.showButtonRetry(positionTask);
                        if (error.getErrorDetail().contains(CoreApplication.getInstance().getString(R.string.text_connection_error))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_connection_fail));
                        } else {

                            view.showError(error.getErrorDetail());
                        }
                    }
                });
    }
}
