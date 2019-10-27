package com.demo.sp19.screen.timekeeping;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.model.offline.AttendanceModel;
import com.demo.architect.domain.AddImageAttendanceUsecase;
import com.demo.architect.domain.AttendanceUsecase;
import com.demo.architect.domain.BaseUseCase;
import com.demo.architect.domain.UploadImageUsecase;
import com.demo.architect.utils.view.ConvertUtils;
import com.demo.architect.utils.view.FileUtils;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.constants.Constants;
import com.demo.sp19.manager.UserManager;

import java.io.File;
import java.util.Date;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by MSI on 26/11/2017.
 */

public class TimekeepingPresenter implements TimekeepingContract.Presenter {
    private final String TAG = TimekeepingPresenter.class.getName();
    private final TimekeepingContract.View view;
    private final AttendanceUsecase attendanceUsecase;
    private final UploadImageUsecase uploadImageUsecase;
    private final AddImageAttendanceUsecase addImageAttendanceUsecase;
    @Inject
    LocalRepository localRepository;

    @Inject
    TimekeepingPresenter(@NonNull TimekeepingContract.View view, AttendanceUsecase attendanceUsecase, UploadImageUsecase uploadImageUsecase, AddImageAttendanceUsecase addImageAttendanceUsecase) {
        this.view = view;
        this.attendanceUsecase = attendanceUsecase;
        this.uploadImageUsecase = uploadImageUsecase;
        this.addImageAttendanceUsecase = addImageAttendanceUsecase;
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

    private int attendanceId = 0;

    @Override
    public void attendanceTracking(double latitude, double longitude, int number, String type, boolean accept) {
        view.showProgressBar();
        int teamOutletId = UserManager.getInstance().getUser().getTeamOutletId();
        attendanceId = 0;
        Date dateCurrent = ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrentShort());
        String dateTime = ConvertUtils.getDateTimeCurrent();

        //kiểm tra xem loại chấm công của sp có phải check out không
        if (type.equals(Constants.CHECK_OUT) && !accept) {
            //kiem tra sp đã chấm công vào chưa
            localRepository.checkUserCheckIn(teamOutletId).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean aBoolean) {
                    if (aBoolean){
                        attendanceUsecase.executeIO(new AttendanceUsecase.RequestValue(ConvertUtils.getCodeGenerationByTime(),
                                        teamOutletId, type, latitude, longitude, number,
                                        dateTime),
                                new BaseUseCase.UseCaseCallback<AttendanceUsecase.ResponseValue,
                                        AttendanceUsecase.ErrorValue>() {
                                    @Override
                                    public void onSuccess(AttendanceUsecase.ResponseValue successResponse) {
                                        AttendanceModel attendanceModel = new AttendanceModel(successResponse.getId(),
                                                String.valueOf(number), latitude, longitude, type, dateCurrent, dateTime, teamOutletId);
                                        //lưu lại thông tin chấm công sau đó hiển thị camera để sp chụp hình chấm công
                                        localRepository.addAttendanceModel(attendanceModel).subscribe(new Action1<String>() {
                                            @Override
                                            public void call(String s) {
                                                view.hideProgressBar();
                                                view.updateUI();
                                            }
                                        });
                                        attendanceId = successResponse.getId();
                                    }

                                    @Override
                                    public void onError(AttendanceUsecase.ErrorValue errorResponse) {
                                        view.hideProgressBar();
                                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                                        } else {
                                            view.showError(errorResponse.getDescription());
                                        }
                                    }
                                });
                    }else {
                        view.hideProgressBar();
                        view.showDialogNotCheckIn();
                    }
                }
            });
        } else {
            attendanceUsecase.executeIO(new AttendanceUsecase.RequestValue(ConvertUtils.getCodeGenerationByTime(),
                            teamOutletId, type, latitude, longitude, number,
                            dateTime),
                    new BaseUseCase.UseCaseCallback<AttendanceUsecase.ResponseValue,
                            AttendanceUsecase.ErrorValue>() {
                        @Override
                        public void onSuccess(AttendanceUsecase.ResponseValue successResponse) {
                            AttendanceModel attendanceModel = new AttendanceModel(successResponse.getId(),
                                    String.valueOf(number), latitude, longitude, type, dateCurrent, dateTime, teamOutletId);
                            localRepository.addAttendanceModel(attendanceModel).subscribe(new Action1<String>() {
                                @Override
                                public void call(String s) {
                                    view.hideProgressBar();
                                    view.updateUI();
                                }
                            });
                            attendanceId = successResponse.getId();
                        }

                        @Override
                        public void onError(AttendanceUsecase.ErrorValue errorResponse) {
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

    @Override
    public void uploadImage(double latitude, double longitude, String path) {
        view.showProgressBar();
        int userTeamId = UserManager.getInstance().getUser().getTeamOutletId();
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        //resize lại hình ảnh
        bitmap = FileUtils.getResizedBitmap(bitmap, 500);
        File file = FileUtils.bitmapToFile(bitmap, path);
        String nameFile = path.substring(path.lastIndexOf("/") + 1);
        uploadImageUsecase.executeIO(new UploadImageUsecase.RequestValue(file, ConvertUtils.getCodeGenerationByTime(),
                        userTeamId, ConvertUtils.getDateTimeCurrent(), latitude, longitude, 1, nameFile),
                new BaseUseCase.UseCaseCallback<UploadImageUsecase.ResponseValue,
                        UploadImageUsecase.ErrorValue>() {
                    @Override
                    public void onSuccess(UploadImageUsecase.ResponseValue successResponse) {
                        final int serverId = successResponse.getId();
                        //sau khi upload hình xong lấy id image vs id chấm công tải lên server
                        addImageAttendanceUsecase.executeIO(new AddImageAttendanceUsecase.RequestValue(
                                        attendanceId, successResponse.getId()),
                                new BaseUseCase.UseCaseCallback<AddImageAttendanceUsecase.ResponseValue,
                                        AddImageAttendanceUsecase.ErrorValue>() {
                                    @Override
                                    public void onSuccess(AddImageAttendanceUsecase.ResponseValue successResponse) {
                                        view.hideProgressBar();
                                        //lưu thông tin hình chấm công
                                        localRepository.updateImageAttendance(attendanceId, serverId, file.getPath()).subscribe(new Action1<String>() {
                                            @Override
                                            public void call(String s) {
                                                view.showSuccess(CoreApplication.getInstance().getString(R.string.text_attendance_capture_success));
                                                view.backToHome();
                                            }
                                        });


                                    }

                                    @Override
                                    public void onError(AddImageAttendanceUsecase.ErrorValue errorResponse) {
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
                    public void onError(UploadImageUsecase.ErrorValue errorResponse) {
                        view.hideProgressBar();
                        if (errorResponse.getDescription().contains(CoreApplication.getInstance().getString(R.string.text_no_address_associated))) {
                            view.showError(CoreApplication.getInstance().getString(R.string.text_no_internet_connection));
                        } else {
                            view.showError(errorResponse.getDescription());
                        }
                    }
                }
        );
    }

}
