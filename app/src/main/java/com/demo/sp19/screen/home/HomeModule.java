package com.demo.sp19.screen.home;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class HomeModule {
    private final HomeContract.View HomeContractView;

    public HomeModule(HomeContract.View HomeContractView) {
        this.HomeContractView = HomeContractView;
    }

    @Provides
    @NonNull
    HomeContract.View provideHomeContractView() {
        return this.HomeContractView;
    }
}

