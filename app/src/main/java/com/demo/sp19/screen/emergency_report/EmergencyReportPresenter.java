package com.demo.sp19.screen.emergency_report;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.helper.Constants;
import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.EmergencyModel;
import com.demo.architect.data.model.offline.ReasonModel;
import com.demo.architect.domain.AddProfileEmergencyUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.CompleteProfileEmergencyUsecase;
import com.demo.architect.utils.view.ConvertUtils;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.manager.UserManager;

import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;
import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class EmergencyReportPresenter implements EmergencyReportContract.Presenter {

    private final String TAG = EmergencyReportPresenter.class.getName();
    private final EmergencyReportContract.View view;
    private final AddProfileEmergencyUsecase addProfileEmergencyUsecase;
    private final CompleteProfileEmergencyUsecase completeProfileEmergencyUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    EmergencyReportPresenter(@NonNull EmergencyReportContract.View view, AddProfileEmergencyUsecase addProfileEmergencyUsecase, CompleteProfileEmergencyUsecase completeProfileEmergencyUsecase) {
        this.view = view;
        this.addProfileEmergencyUsecase = addProfileEmergencyUsecase;
        this.completeProfileEmergencyUsecase = completeProfileEmergencyUsecase;
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
    public void getListReason() {
        localRepository.getListReason().subscribe(new Action1<List<ReasonModel>>() {
            @Override
            public void call(List<ReasonModel> reasonModels) {
                view.showListReason(reasonModels);
            }
        });
    }

    @Override
    public void addEmergency(String employeeId, int reasonId) {
        view.showProgressBar();
        UserEntity userEntity = UserManager.getInstance().getUser();
        String sessionCode = ConvertUtils.getCodeGenerationByTime();
        String date = ConvertUtils.getDateTimeCurrent();
        //format lại mã nhân viên
        String[] codes = employeeId.split("");
        String employeeCode = "";
        for (String code : codes) {
            if (!TextUtils.isEmpty(code)){
                employeeCode += code.trim();
            }
        }

        employeeCode = employeeCode.trim().replace("_","-").toUpperCase();

        Log.d(TAG, "Mã nhân viên: " + employeeCode);

        addProfileEmergencyUsecase.executeIO(new AddProfileEmergencyUsecase.RequestValue(sessionCode,
                        userEntity.getTeamOutletId(), employeeCode, reasonId, userEntity.getOutlet().getOutletId(), date, userEntity.getProjectId()),
                new BaseUseCase.UseCaseCallback<AddProfileEmergencyUsecase.ResponseValue,
                        AddProfileEmergencyUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(AddProfileEmergencyUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        //tạo báo cáo khẩn cấp lưu xuống database
                        EmergencyModel emergencyModel = new EmergencyModel(successResponse.getId(),
                                sessionCode, reasonId, employeeId, userEntity.getTeamOutletId(), userEntity.getOutlet().getOutletId(),
                                date, Constants.NOT_FINISHED);
                        localRepository.addEmergency(emergencyModel).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_send_report_success));
                                getEmergencyNotFinished();
                            }
                        });
                    }

                    @Override
                    public void onError(AddProfileEmergencyUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                    }
                });
    }

    @Override
    public void getEmergencyNotFinished() {
        UserEntity userEntity = UserManager.getInstance().getUser();
        localRepository.getEmergencyNotFinished(userEntity.getTeamOutletId()).subscribe(new Action1<RealmResults<EmergencyModel>>() {
            @Override
            public void call(RealmResults<EmergencyModel> list) {
                view.showEmergency(list);
            }
        });
    }

    @Override
    public void completeEmergency(int emergencyId) {
        view.showProgressBar();
        UserEntity userEntity = UserManager.getInstance().getUser();
        //kết thúc báo cáo khẩn cấp
        completeProfileEmergencyUsecase.executeIO(new CompleteProfileEmergencyUsecase.RequestValue(emergencyId, userEntity.getTeamOutletId()),
                new BaseUseCase.UseCaseCallback<CompleteProfileEmergencyUsecase.ResponseValue,
                        CompleteProfileEmergencyUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(CompleteProfileEmergencyUsecase.ResponseValue successResponse) {
                        view.hideProgressBar();
                        localRepository.updateStateEmergency(emergencyId).subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_finish_success));
                                //lấy lại ds báo cáo khẩn cấp chưa hoàn thành
                                getEmergencyNotFinished();
                            }
                        });
                    }

                    @Override
                    public void onError(CompleteProfileEmergencyUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                    }
                });
    }
}
