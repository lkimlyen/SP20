package com.demo.sp19.screen.confirm_set;

import com.demo.sp19.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {ConfirmSetModule.class})
public interface ConfirmSetComponent {
    void inject(ConfirmSetActivity activity);

}
