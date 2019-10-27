package com.demo.sp19.screen.setting;

import com.demo.sp19.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {SettingModule.class})
public interface SettingComponent {
    void inject(SettingActivity activity);

}
