package com.demo.sp19.screen.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.NotificationModel;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.GetNotificationUsecase;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.manager.UserManager;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class NotificationPresenter implements NotificationContract.Presenter {

    private final String TAG = NotificationPresenter.class.getName();
    private final NotificationContract.View view;
    private final GetNotificationUsecase getNotificationUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    NotificationPresenter(@NonNull NotificationContract.View view, GetNotificationUsecase getNotificationUsecase) {
        this.view = view;
        this.getNotificationUsecase = getNotificationUsecase;
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
    public void getNotification() {
        localRepository.findAllNoti().subscribe(new Action1<List<NotificationModel>>() {
            @Override
            public void call(List<NotificationModel> notificationModels) {
                view.showNotification(notificationModels);
            }
        });
    }

    @Override
    public void downloadNotification() {
        UserEntity userEntity = UserManager.getInstance().getUser();
        view.showProgressBar();
        getNotificationUsecase.executeIO(new GetNotificationUsecase.RequestValue(userEntity.getProjectId(), userEntity.getOutlet().getOutletType(), userEntity.getOutlet().getOutletId()),
                new BaseUseCase.UseCaseCallback<GetNotificationUsecase.ResponseValue,
                        GetNotificationUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(GetNotificationUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();

                        if (successResponse.getEntity().size() > 0){
                            localRepository.addNotification(successResponse.getEntity()).subscribe(new Action1<String>() {
                                @Override
                                public void call(String s) {
                                    //hide badge sl thông báo sau khi get thông báo mới về
                                    SharedPreferenceHelper.getInstance(CoreApplication.getInstance()).addNotificationNew(false);
                                    NotificationManager mNotificationManager = (NotificationManager) CoreApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
                                    mNotificationManager.cancel(321);
                                    getNotification();
                                }
                            });
                        }else {
                            getNotification();
                        }


                    }

                    @Override
                    public void onError(GetNotificationUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                        getNotification();
                    }
                });
    }
}
