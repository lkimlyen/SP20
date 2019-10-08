package com.demo.compass.data.repository.base.other.remote;

import com.demo.compass.data.model.BackgroundDialEntity;
import com.demo.compass.data.model.BaseListResponse;
import com.demo.compass.data.model.BaseResponse;
import com.demo.compass.data.model.POSMEntity;
import com.demo.compass.data.model.ReasonEntity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import retrofit2.Call;

//import javax.inject.Singleton;

/**
 * Created by uyminhduc on 10/16/16.
 */
//@Singleton
public class OtherRepositoryImpl implements OtherRepository {

    private final static String TAG = OtherRepositoryImpl.class.getName();

    private OtherApiInterface mRemoteApiInterface;

    public OtherRepositoryImpl(OtherApiInterface mOtherApiInterface) {
        this.mRemoteApiInterface = mOtherApiInterface;
    }


    private void handleStringResponse(Call<BaseResponse<String>> call, ObservableEmitter<BaseResponse<String>> emitter) {
        try {
            BaseResponse<String> response = call.execute().body();

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
    private void handleBaseResponse(Call<BaseResponse> call, ObservableEmitter<BaseResponse> emitter) {
        try {
            BaseResponse response = call.execute().body();

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

    private void handlePOSMResponse(Call<BaseListResponse<POSMEntity>> call, ObservableEmitter<BaseListResponse<POSMEntity>> emitter) {
        try {
            BaseListResponse<POSMEntity> response = call.execute().body();

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
    private void handleBackgroundResponse(Call<BaseListResponse<BackgroundDialEntity>> call, ObservableEmitter<BaseListResponse<BackgroundDialEntity>> emitter) {
        try {
            BaseListResponse<BackgroundDialEntity> response = call.execute().body();

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



    private void handleReasonResponse(Call<BaseListResponse<ReasonEntity>> call, ObservableEmitter<BaseListResponse<ReasonEntity>> emitter) {
        try {
            BaseListResponse<ReasonEntity> response = call.execute().body();

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
    public Observable<BaseResponse> updateStock(final String appCode, final String data) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.updateStock(appCode,data), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse> updateTypeStock(final String appCode, final String data) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.updateTypeStock(appCode,data), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse> addTakeOffVolume(final String appCode, final String data) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.addTakeOffVolume(appCode,data), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<BackgroundDialEntity>> getBackground(final String appCode, final int projectId) {
        return Observable.create(new ObservableOnSubscribe<BaseListResponse<BackgroundDialEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<BackgroundDialEntity>> emitter) throws Exception {
                handleBackgroundResponse(mRemoteApiInterface.getBackground(appCode,projectId), emitter);
            }
        });

    }

    @Override
    public Observable<BaseListResponse<ReasonEntity>> getEmergency(final int projectId) {
        return Observable.create(new ObservableOnSubscribe<BaseListResponse<ReasonEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<ReasonEntity>> emitter) throws Exception {
                handleReasonResponse(mRemoteApiInterface.getEmergency(projectId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse<String>> getUpdateVersion(final int projectId) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse<String>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<String>> emitter) throws Exception {
                handleStringResponse(mRemoteApiInterface.getUpdateVersion(projectId), emitter);
            }
        });

    }

    @Override
    public Observable<BaseResponse<Integer>> addProfileEmergency(final String appCode, final String code, final int teamOutletId, final String employeeId, final int reasonId, final int outletId, final String startDateTime,final int projectId) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<Integer>> emitter) throws Exception {
                handleIntegerResponse(mRemoteApiInterface.addProfileEmergency(appCode,
                        code,teamOutletId,employeeId, reasonId,outletId,startDateTime,projectId ), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse> completeProfileEmergency(final String appCode, final int emergencyId, final int teamOutletId) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.completeProfileEmergency(appCode,
                        emergencyId,teamOutletId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseListResponse<POSMEntity>> getPOSM(final int outletId) {
        return Observable.create(new ObservableOnSubscribe<BaseListResponse<POSMEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseListResponse<POSMEntity>> emitter) throws Exception {
                handlePOSMResponse(mRemoteApiInterface.getPOSM(outletId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse> addPOSM(final String appCode, final String data) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.addPOSM(appCode,data), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse> getVersion(final String appCode, final int outletId, final String version) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.getVersion(appCode,
                        outletId,version), emitter);
            }
        });
    }
}


