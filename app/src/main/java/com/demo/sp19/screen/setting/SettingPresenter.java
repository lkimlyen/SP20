package com.demo.sp19.screen.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.ChangePassUsecase;
import com.demo.architect.domain.GetCurrentBrandSetUsecase;
import com.demo.architect.domain.GetUpdateAppUsecase;
import com.demo.architect.domain.LogoutUsecase;
import com.demo.architect.domain.UpdateChangeSetUsecase;
import com.demo.architect.utils.view.FileUtils;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.manager.UserManager;

import java.io.File;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class SettingPresenter implements SettingContract.Presenter {

    private final String TAG = SettingPresenter.class.getName();
    private final SettingContract.View view;
    private final ChangePassUsecase changePassUsecase;
    private final UpdateChangeSetUsecase updateChangeSetUsecase;
    private final GetUpdateAppUsecase getUpdateAppUsecase;
    private final GetCurrentBrandSetUsecase getCurrentBrandSetUsecase;
    private final LogoutUsecase logoutUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    SettingPresenter(@NonNull SettingContract.View view, ChangePassUsecase changePassUsecase, UpdateChangeSetUsecase updateChangeSetUsecase, GetUpdateAppUsecase getUpdateAppUsecase, GetCurrentBrandSetUsecase getCurrentBrandSetUsecase, LogoutUsecase logoutUsecase) {
        this.view = view;
        this.changePassUsecase = changePassUsecase;
        this.updateChangeSetUsecase = updateChangeSetUsecase;
        this.getUpdateAppUsecase = getUpdateAppUsecase;
        this.getCurrentBrandSetUsecase = getCurrentBrandSetUsecase;
        this.logoutUsecase = logoutUsecase;
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
    public void backupData() {
        view.showProgressBar();
        UserEntity user = UserManager.getInstance().getUser();
        File mDownloadDir = new File(Environment.getExternalStorageDirectory(), "/Data/" + "SP19_" + UserManager.getInstance().getUser().getOutlet().getOutletId()
                + "_" + UserManager.getInstance().getUser().getProjectId() + ".realm");
        String dataPath = FileUtils.exportRealmFile(mDownloadDir.getPath());
        if (!dataPath.equals("")) {
            view.uploadFile(dataPath, user);
        } else {
            view.showError(CoreApplication.getInstance().getString(R.string.text_backup_fail));
            view.hideProgressBar();
        }
    }

    @Override
    public void changePassword(String passOld, String passNew) {
        view.showProgressBar();
        UserEntity userEntity = UserManager.getInstance().getUser();
        changePassUsecase.executeIO(new ChangePassUsecase.RequestValue(userEntity.getTeamOutletId(), passOld, passNew),
                new BaseUseCase.UseCaseCallback<ChangePassUsecase.ResponseValue, ChangePassUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(ChangePassUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        view.showSuccess(CoreApplication.getInstance().getString(R.string.text_change_password_success));
                    }

                    @Override
                    public void onError(ChangePassUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        view.showError(errorResponse.getDescription());
                    }
                });
    }

    @Override
    public String getVersion() {
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
        return version;
    }


    @Override
    public void updateApp() {
        view.showProgressBar();
        UserEntity userEntity = UserManager.getInstance().getUser();
        getUpdateAppUsecase.executeIO(new GetUpdateAppUsecase.RequestValue(userEntity.getProjectId()),
                new BaseUseCase.UseCaseCallback<GetUpdateAppUsecase.ResponseValue,
                        GetUpdateAppUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetUpdateAppUsecase.ResponseValue successResponse) {

                        String fileName = successResponse.getLink().substring(successResponse.getLink().lastIndexOf('/') + 1);
                        File mDownloadDir = CoreApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        if (!mDownloadDir.exists()) {
                            mDownloadDir.mkdirs();
                        }
                        AndroidNetworking.download(successResponse.getLink(), mDownloadDir.getPath(), fileName)
                                .build()
                                .startDownload(new DownloadListener() {
                                    @Override
                                    public void onDownloadComplete() {
                                        view.hideProgressBar();
                                        File file = new File(mDownloadDir.getPath(), fileName);
                                        if (!file.exists()) {
                                            file.mkdirs();
                                        }

                                        view.installApp(file.getPath());
                                    }

                                    @Override
                                    public void onError(ANError error) {
                                        // handle error
                                        view.hideProgressBar();
                                        view.showError(error.getErrorDetail());
                                    }
                                });
                    }

                    @Override
                    public void onError(GetUpdateAppUsecase.ErrorValue errorResponse) {
                        view.showError(errorResponse.getDescription());
                        view.hideProgressBar();
                    }
                });
    }

    private boolean check = false;



    @Override
    public void clearImage() {
        view.showProgressBar();
        localRepository.clearImage().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                view.hideProgressBar();
                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_delete_image_success));
            }
        });
    }

    @Override
    public void counNumberWaitingUpload(String version) {
        UserEntity userEntity = UserManager.getInstance().getUser();
        localRepository.countNumberWaitingUpload(userEntity.getTeamOutletId(), userEntity.getOutlet().getOutletId())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        if (integer == 0) {
                            view.resetAllData(userEntity,version);
                        } else {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_please_upload_all_data_to_server));
                        }
                    }
                });
    }

    @Override
    public void logout() {
        view.showProgressBar();
        logoutUsecase.executeIO(new LogoutUsecase.RequestValue(UserManager.getInstance().getUser().getOutlet().getOutletId()), new BaseUseCase.UseCaseCallback
                <LogoutUsecase.ResponseValue, LogoutUsecase.ErrorValue>() {
            @Override
            public void onSuccess(LogoutUsecase.ResponseValue successResponse) {
                view.hideProgressBar();
                //Save user entity to shared preferences
                view.goToLogin();
            }

            @Override
            public void onError(LogoutUsecase.ErrorValue errorResponse) {
                if(errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))){
                    view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                }else {
                    view.showError(errorResponse.getDescription());
                }
                view.hideProgressBar();
            }
        });
    }

}
