package com.demo.sp19.screen.notification;

import com.demo.sp19.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {NotificationModule.class})
public interface NotificationComponent {
    void inject(NotificationFragment fragment);

}
