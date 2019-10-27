package com.demo.sp19.app.di.component;


import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.app.base.BaseActivity;
import com.demo.sp19.app.base.BaseFragment;
import com.demo.sp19.app.di.module.ApplicationModule;
import com.demo.sp19.app.di.module.NetModule;
import com.demo.sp19.app.di.module.RepositoryModule;
import com.demo.sp19.app.di.module.UseCaseModule;
import com.demo.sp19.screen.confirm_set.ConfirmSetComponent;
import com.demo.sp19.screen.confirm_set.ConfirmSetModule;
import com.demo.sp19.screen.dashboard.DashboardComponent;
import com.demo.sp19.screen.dashboard.DashboardModule;
import com.demo.sp19.screen.emergency_report.EmergencyReportComponent;
import com.demo.sp19.screen.emergency_report.EmergencyReportModule;
import com.demo.sp19.screen.gift.GiftModule;
import com.demo.sp19.screen.home.HomeModule;
import com.demo.sp19.screen.login.LoginComponent;
import com.demo.sp19.screen.login.LoginModule;
import com.demo.sp19.screen.manager_gift.ManagerGiftComponent;
import com.demo.sp19.screen.manager_gift.ManagerGiftModule;
import com.demo.sp19.screen.notification.NotificationComponent;
import com.demo.sp19.screen.notification.NotificationModule;
import com.demo.sp19.screen.posm.POSMComponent;
import com.demo.sp19.screen.posm.POSMModule;
import com.demo.sp19.screen.request_gift.RequestGiftComponent;
import com.demo.sp19.screen.request_gift.RequestGiftModule;
import com.demo.sp19.screen.rotation.RotationComponent;
import com.demo.sp19.screen.rotation.RotationModule;
import com.demo.sp19.screen.rotation_mega.RotationMegaComponent;
import com.demo.sp19.screen.rotation_mega.RotationMegaModule;
import com.demo.sp19.screen.setting.SettingComponent;
import com.demo.sp19.screen.setting.SettingModule;
import com.demo.sp19.screen.statistical_gift.StatisticalGiftComponent;
import com.demo.sp19.screen.statistical_gift.StatisticalGiftModule;
import com.demo.sp19.screen.stock.StockModule;
import com.demo.sp19.screen.take_off_volumn.TakeOffVolumnComponent;
import com.demo.sp19.screen.take_off_volumn.TakeOffVolumnModule;
import com.demo.sp19.screen.timekeeping.TimekeepingComponent;
import com.demo.sp19.screen.timekeeping.TimekeepingModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by uyminhduc on 12/16/16.
 */

@Singleton
@Component(modules = {ApplicationModule.class,
        NetModule.class,
        UseCaseModule.class,
        RepositoryModule.class})
public interface ApplicationComponent {

    void inject(CoreApplication application);

    void inject(BaseActivity activity);

    void inject(BaseFragment fragment);

    LoginComponent plus(LoginModule loginModule);

    DashboardComponent plus(HomeModule homeModule, NotificationModule notificationModule,
                            StockModule stockModule, TimekeepingModule timekeepingModule,
                            GiftModule module, DashboardModule dashboardModule);

    NotificationComponent plus(NotificationModule notificationModule);

    TimekeepingComponent plus(TimekeepingModule timekeepingModule);

    RotationComponent plus(RotationModule rotationModule);

    TakeOffVolumnComponent plus(TakeOffVolumnModule module);

    ManagerGiftComponent plus(ManagerGiftModule module);

    StatisticalGiftComponent plus(StatisticalGiftModule module);

    RequestGiftComponent plus(RequestGiftModule module);

    ConfirmSetComponent plus(ConfirmSetModule module);

    SettingComponent plus(SettingModule module);

    EmergencyReportComponent plus(EmergencyReportModule module);

    POSMComponent plus(POSMModule module);

    RotationMegaComponent plus(RotationMegaModule module);

}
