package com.demo.sp19.screen.setting;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class SettingModule {
    private final SettingContract.View NotificationContractView;

    public SettingModule(SettingContract.View NotificationContractView) {
        this.NotificationContractView = NotificationContractView;
    }

    @Provides
    @NonNull
    SettingContract.View provideNotificationContractView() {
        return this.NotificationContractView;
    }
}

