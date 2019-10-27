package com.demo.sp19.screen.emergency_report;

import com.demo.sp19.app.di.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by MSI on 26/11/2017.
 */

@ActivityScope
@Subcomponent(modules = {EmergencyReportModule.class})
public interface EmergencyReportComponent {
    void inject(EmergencyReportActivity activity);

}
