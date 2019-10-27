package com.demo.sp19.screen.home;

import com.demo.sp19.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {HomeModule.class})
public interface HomeComponent {
    void inject(HomeFragment homeFragment);

}
