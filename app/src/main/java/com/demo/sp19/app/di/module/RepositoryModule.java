package com.demo.sp19.app.di.module;


import com.demo.architect.data.repository.base.account.remote.AccountRepository;
import com.demo.architect.data.repository.base.account.remote.AccountRepositoryImpl;
import com.demo.architect.data.repository.base.gift.remote.GiftRepository;
import com.demo.architect.data.repository.base.gift.remote.GiftRepositoryImpl;
import com.demo.architect.data.repository.base.local.DatabaseRealm;
import com.demo.architect.data.repository.base.local.LocalRepository;
import com.demo.architect.data.repository.base.local.LocalRepositoryImpl;
import com.demo.architect.data.repository.base.notification.remote.NotificationRepository;
import com.demo.architect.data.repository.base.notification.remote.NotificationRepositoryImpl;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;
import com.demo.architect.data.repository.base.other.remote.OtherRepositoryImpl;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;
import com.demo.architect.data.repository.base.product.remote.ProductRepositoryImpl;
import com.demo.architect.data.repository.base.remote.RemoteRepository;
import com.demo.architect.data.repository.base.remote.RemoteRepositoryImpl;
import com.demo.architect.data.repository.base.upload.remote.UploadRepository;
import com.demo.architect.data.repository.base.upload.remote.UploadRepositoryImpl;
import com.demo.sp19.app.CoreApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public LocalRepository provideMessageRepository() {
        return new LocalRepositoryImpl(CoreApplication.getInstance());
    }

    @Provides
    @Singleton
    public DatabaseRealm provideDatabaseRealm() {
        return new DatabaseRealm(CoreApplication.getInstance());
    }

    @Provides
    @Singleton
    RemoteRepository provideRemoteRepository(RemoteRepositoryImpl apiServiceImp) {
        return apiServiceImp;
    }
    @Provides
    @Singleton
    AccountRepository provideAccountRepository(AccountRepositoryImpl apiServiceImp) {
        return apiServiceImp;
    }

    @Provides
    @Singleton
    NotificationRepository provideNotificationRepository(NotificationRepositoryImpl apiServiceImp) {
        return apiServiceImp;
    }

    @Provides
    @Singleton
    OtherRepository provideOtherRepository(OtherRepositoryImpl apiServiceImp) {
        return apiServiceImp;
    }
    @Provides
    @Singleton
    UploadRepository provideUploadRepository(UploadRepositoryImpl apiServiceImp) {
        return apiServiceImp;
    }

    @Provides
    @Singleton
    ProductRepository provideProductRepository(ProductRepositoryImpl apiServiceImp) {
        return apiServiceImp;
    }

    @Provides
    @Singleton
    GiftRepository provideGiftRepository(GiftRepositoryImpl apiServiceImp) {
        return apiServiceImp;
    }
}
