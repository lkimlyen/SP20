package com.demo.compass.data.repository.base.upload.remote;


import com.demo.compass.data.model.BaseResponse;
import com.demo.compass.data.model.UploadEntity;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

//import javax.inject.Singleton;

/**
 * Created by uyminhduc on 10/16/16.
 */
//@Singleton
public class UploadRepositoryImpl implements UploadRepository {

    private final static String TAG = UploadRepositoryImpl.class.getName();

    private UploadApiInterface mRemoteApiInterface;

    public UploadRepositoryImpl(UploadApiInterface mUploadApiInterface) {
        this.mRemoteApiInterface = mUploadApiInterface;
    }
    private void handleUploadImageResponse(Call<UploadEntity> call, ObservableEmitter<UploadEntity> emitter) {
        try {
            UploadEntity response = call.execute().body();

            if (!emitter.isDisposed()) {
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleIntegerResponse(Call<BaseResponse<Integer>> call, ObservableEmitter<BaseResponse<Integer>> emitter) {
        try {
            BaseResponse<Integer> response = call.execute().body();

            if (!emitter.isDisposed()) {
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }



    @Override
    public Observable<BaseResponse<Integer>> uploadImage(final File file, final String appCode, final String sessionCode, final int userTeamID, final String dateTimeDevice, final double latitude, final double longitude, final int imageType, final String fileName) {

        return Observable.create(new ObservableOnSubscribe<BaseResponse<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<Integer>> emitter) throws Exception {
                handleIntegerResponse(mRemoteApiInterface.uploadImage(MultipartBody.Part
                                .createFormData("file", file.getName(),
                                        RequestBody.create(MultipartBody.FORM, file)),
                        RequestBody.create(MultipartBody.FORM, appCode),
                        RequestBody.create(MultipartBody.FORM, sessionCode),
                        RequestBody.create(MultipartBody.FORM, userTeamID + ""),
                        RequestBody.create(MultipartBody.FORM, dateTimeDevice),
                        RequestBody.create(MultipartBody.FORM, latitude + ""),
                        RequestBody.create(MultipartBody.FORM, longitude + ""),
                        RequestBody.create(MultipartBody.FORM, imageType + ""),
                        RequestBody.create(MultipartBody.FORM, fileName + "")), emitter);
            }
        });

    }
}


