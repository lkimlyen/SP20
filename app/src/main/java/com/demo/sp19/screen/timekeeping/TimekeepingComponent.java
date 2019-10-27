package com.demo.sp19.screen.timekeeping;


import com.demo.sp19.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {TimekeepingModule.class})
public interface TimekeepingComponent {
    void inject(TimekeepingActivity activity);

}
