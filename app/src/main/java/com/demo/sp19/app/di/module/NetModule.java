package com.demo.sp19.app.di.module;

import android.app.Application;

import com.demo.architect.data.repository.base.account.remote.AccountApiInterface;
import com.demo.architect.data.repository.base.account.remote.AccountRepositoryImpl;
import com.demo.architect.data.repository.base.gift.remote.GiftApiInterface;
import com.demo.architect.data.repository.base.gift.remote.GiftRepositoryImpl;
import com.demo.architect.data.repository.base.notification.remote.NotificationApiInterface;
import com.demo.architect.data.repository.base.notification.remote.NotificationRepositoryImpl;
import com.demo.architect.data.repository.base.other.remote.OtherApiInterface;
import com.demo.architect.data.repository.base.other.remote.OtherRepositoryImpl;
import com.demo.architect.data.repository.base.product.remote.ProductApiInterface;
import com.demo.architect.data.repository.base.product.remote.ProductRepositoryImpl;
import com.demo.architect.data.repository.base.remote.RemoteApiInterface;
import com.demo.architect.data.repository.base.remote.RemoteRepositoryImpl;
import com.demo.architect.data.repository.base.upload.remote.UploadApiInterface;
import com.demo.architect.data.repository.base.upload.remote.UploadRepositoryImpl;
import com.demo.sp19.util.RetrofitJsonConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by uyminhduc on 12/16/16.
 */
@Module
public class NetModule {
    private String mBaseUrl;

    public NetModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    RxJavaCallAdapterFactory provideRxJavaCallAdapter() {
        return RxJavaCallAdapterFactory.create();
    }

    @Provides
    @Singleton
    RemoteRepositoryImpl provideRetrofit(OkHttpClient okHttpClient, Gson gson, RxJavaCallAdapterFactory rxAdapterFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addCallAdapterFactory(rxAdapterFactory)
                .addConverterFactory(RetrofitJsonConverter.create(gson))
                .client(okHttpClient)
                .build();
        return new RemoteRepositoryImpl(retrofit.create(RemoteApiInterface.class));
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttp() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(80, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();

    }
    @Provides
    @Singleton
    AccountRepositoryImpl provideAccountRetrofit(OkHttpClient okHttpClient, Gson gson, RxJavaCallAdapterFactory rxAdapterFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addCallAdapterFactory(rxAdapterFactory)
                .addConverterFactory(RetrofitJsonConverter.create(gson))
                .client(okHttpClient)
                .build();
        return new AccountRepositoryImpl(retrofit.create(AccountApiInterface.class));
    }

    @Provides
    @Singleton
    NotificationRepositoryImpl provideNotificationRetrofit(OkHttpClient okHttpClient, Gson gson, RxJavaCallAdapterFactory rxAdapterFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addCallAdapterFactory(rxAdapterFactory)
                .addConverterFactory(RetrofitJsonConverter.create(gson))
                .client(okHttpClient)
                .build();
        return new NotificationRepositoryImpl(retrofit.create(NotificationApiInterface.class));
    }

    @Provides
    @Singleton
    OtherRepositoryImpl provideOrderRetrofit(OkHttpClient okHttpClient, Gson gson, RxJavaCallAdapterFactory rxAdapterFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addCallAdapterFactory(rxAdapterFactory)
                .addConverterFactory(RetrofitJsonConverter.create(gson))
                .client(okHttpClient)
                .build();
        return new OtherRepositoryImpl(retrofit.create(OtherApiInterface.class));
    }
    @Provides
    @Singleton
    UploadRepositoryImpl provideUploadRetrofit(OkHttpClient okHttpClient, Gson gson, RxJavaCallAdapterFactory rxAdapterFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addCallAdapterFactory(rxAdapterFactory)
                .addConverterFactory(RetrofitJsonConverter.create(gson))
                .client(okHttpClient)
                .build();
        return new UploadRepositoryImpl(retrofit.create(UploadApiInterface.class));
    }

    @Provides
    @Singleton
    ProductRepositoryImpl provideProductRetrofit(OkHttpClient okHttpClient, Gson gson, RxJavaCallAdapterFactory rxAdapterFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addCallAdapterFactory(rxAdapterFactory)
                .addConverterFactory(RetrofitJsonConverter.create(gson))
                .client(okHttpClient)
                .build();
        return new ProductRepositoryImpl(retrofit.create(ProductApiInterface.class));
    }
    @Provides
    @Singleton
    GiftRepositoryImpl provideGiftRetrofit(OkHttpClient okHttpClient, Gson gson, RxJavaCallAdapterFactory rxAdapterFactory) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addCallAdapterFactory(rxAdapterFactory)
                .addConverterFactory(RetrofitJsonConverter.create(gson))
                .client(okHttpClient)
                .build();
        return new GiftRepositoryImpl(retrofit.create(GiftApiInterface.class));
    }
}

