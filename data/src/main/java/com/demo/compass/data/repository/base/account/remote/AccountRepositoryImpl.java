package com.demo.compass.data.repository.base.account.remote;

import com.demo.compass.data.model.BaseResponse;
import com.demo.compass.data.model.UserEntity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import retrofit2.Call;

//import javax.inject.Singleton;

/**
 * Created by uyminhduc on 10/16/16.
 */
//@Singleton
public class AccountRepositoryImpl implements AccountRepository {

    private final static String TAG = AccountRepositoryImpl.class.getName();

    private AccountApiInterface mRemoteApiInterface;

    public AccountRepositoryImpl(AccountApiInterface mAccountApiInterface) {
        this.mRemoteApiInterface = mAccountApiInterface;
    }

    private void handleUserLoginResponse(Call<BaseResponse<UserEntity>> call, ObservableEmitter<BaseResponse<UserEntity>> emitter) {
        try {
            BaseResponse<UserEntity> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }
    private void handleBaseResponse(Call<BaseResponse> call, ObservableEmitter<BaseResponse> emitter) {
        try {
            BaseResponse response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }

    private void handleAttendanceResponse(Call<BaseResponse<Integer>> call, ObservableEmitter<BaseResponse<Integer>> emitter) {
        try {
            BaseResponse<Integer> response = call.execute().body();

            if (!emitter.isDisposed()){
                if (response != null) {
                    emitter.onNext(response);
                } else {
                    emitter.onError(new Exception("Network Error!"));
                }
                emitter.onComplete();
            }


        } catch (Exception e) {
            if (!emitter.isDisposed()){
                emitter.onError(e);
                emitter.onComplete();
            }


        }
    }


    @Override
    public Observable<BaseResponse<UserEntity>> login(final String appCode, final String userType,
                                                      final String username, final String password,
                                                      final String deviceToken, final  String deviceId) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse<UserEntity>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<UserEntity>> emitter) throws Exception {
                handleUserLoginResponse(mRemoteApiInterface.login(appCode, userType, username, password,deviceToken, deviceId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse> changePass(final String appCode, final int teamOutletId, final String passOld, final String passNew) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.changePass(appCode, teamOutletId,passOld,passNew), emitter);
            }
        });

    }

    @Override
    public Observable<BaseResponse<Integer>> attendanceTracking(final String appCode, final String sessionCode,
                                                             final int teamId,  final String type,
                                                             final double latitude, final double longitude, final int number,
                                                             final String dateTime) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<Integer>> emitter) throws Exception {
                handleAttendanceResponse(mRemoteApiInterface.attendanceTracking(appCode,
                        sessionCode, teamId, type, latitude, longitude, number,
                        dateTime), emitter);
            }
        });

    }

    @Override
    public Observable<BaseResponse<Integer>> addImageForAttenadence(final String appCode, final int attendanceId, final int imageId) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse<Integer>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse<Integer>> emitter) throws Exception {
                handleAttendanceResponse(mRemoteApiInterface.addImageForAttenadence(appCode, attendanceId, imageId), emitter);
            }
        });
    }

    @Override
    public Observable<BaseResponse> logout(final String appCode, final int outletId) {
        return Observable.create(new ObservableOnSubscribe<BaseResponse>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponse> emitter) throws Exception {
                handleBaseResponse(mRemoteApiInterface.logout(appCode, outletId), emitter);
            }
        });
    }
}


