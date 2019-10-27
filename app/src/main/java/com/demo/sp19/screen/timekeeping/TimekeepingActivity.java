package com.demo.sp19.screen.timekeeping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.app.base.BaseActivity;
import com.demo.sp19.util.Precondition;

import javax.inject.Inject;

/**
 * Created by MSI on 26/11/2017.
 */

public class TimekeepingActivity extends BaseActivity {
    public static int REQUEST_CODE = 456;
    @Inject
    TimekeepingPresenter TimekeepingPresenter;

    TimekeepingFragment fragment;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, TimekeepingActivity.class);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initFragment();

        // Create the presenter
        CoreApplication.getInstance().getApplicationComponent()
                .plus(new TimekeepingModule(fragment))
                .inject(this);
    }

    private void initFragment() {
        fragment = (TimekeepingFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = TimekeepingFragment.newInstance();
            addFragmentToBackStack(fragment, R.id.fragmentContainer);
        }
    }

    private void addFragmentToBackStack(TimekeepingFragment fragment, int frameId) {
        Precondition.checkNotNull(fragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
      }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment.onActivityResult(requestCode,resultCode, data);
    }
}
