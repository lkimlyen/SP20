package com.demo.sp19.screen.posm;

import com.demo.sp19.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {POSMModule.class})
public interface POSMComponent {
    void inject(POSMActivity activity);

}
