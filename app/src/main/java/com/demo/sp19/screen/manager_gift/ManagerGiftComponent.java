package com.demo.sp19.screen.manager_gift;

import com.demo.sp19.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {ManagerGiftModule.class})
public interface ManagerGiftComponent {
    void inject(ManagerGiftActivity activity);

}
