package com.demo.sp19.screen.dashboard;

import com.demo.sp19.app.di.ActivityScope;
import com.demo.sp19.screen.gift.GiftModule;
import com.demo.sp19.screen.home.HomeModule;
import com.demo.sp19.screen.notification.NotificationModule;
import com.demo.sp19.screen.stock.StockModule;
import com.demo.sp19.screen.timekeeping.TimekeepingModule;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {HomeModule.class, NotificationModule.class, StockModule.class,
        TimekeepingModule.class, GiftModule.class, DashboardModule.class})
public interface DashboardComponent {
    void inject(DashboardFragment fragment);

}
