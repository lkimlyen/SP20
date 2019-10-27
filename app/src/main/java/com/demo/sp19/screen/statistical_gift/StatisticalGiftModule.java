package com.demo.sp19.screen.statistical_gift;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class StatisticalGiftModule {
    private final StatisticalGiftContract.View NotificationContractView;

    public StatisticalGiftModule(StatisticalGiftContract.View NotificationContractView) {
        this.NotificationContractView = NotificationContractView;
    }

    @Provides
    @NonNull
    StatisticalGiftContract.View provideNotificationContractView() {
        return this.NotificationContractView;
    }
}

