package com.demo.sp19.screen.login;


import com.demo.sp19.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {LoginModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);

}
