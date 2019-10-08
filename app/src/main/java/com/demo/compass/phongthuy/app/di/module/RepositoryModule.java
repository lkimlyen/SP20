package com.demo.compass.phongthuy.app.di.module;



import com.demo.compass.phongthuy.app.CoreApplication;
import com.demo.compass.data.repository.base.account.remote.AccountRepository;
import com.demo.compass.data.repository.base.account.remote.AccountRepositoryImpl;
import com.demo.compass.data.repository.base.local.DatabaseRealm;
import com.demo.compass.data.repository.base.local.LocalRepository;
import com.demo.compass.data.repository.base.local.LocalRepositoryImpl;
import com.demo.compass.data.repository.base.notification.remote.NotificationRepository;
import com.demo.compass.data.repository.base.notification.remote.NotificationRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public LocalRepository provideMessageRepository() {
        return new LocalRepositoryImpl();
    }

    @Provides
    @Singleton
    public DatabaseRealm provideDatabaseRealm() {
        return new DatabaseRealm(CoreApplication.getInstance());
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


}
