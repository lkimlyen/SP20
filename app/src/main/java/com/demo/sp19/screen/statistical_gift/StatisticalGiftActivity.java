package com.demo.sp19.screen.statistical_gift;

import android.content.Context;
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

public class StatisticalGiftActivity extends BaseActivity {
    @Inject
    StatisticalGiftPresenter RotationPresenter;

    StatisticalGiftFragment fragment;

    public static void start(Context context) {
        Intent intent = new Intent(context, StatisticalGiftActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initFragment();

        // Create the presenter
        CoreApplication.getInstance().getApplicationComponent()
                .plus(new StatisticalGiftModule(fragment))
                .inject(this);
    }

    private void initFragment() {
        fragment = (StatisticalGiftFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = StatisticalGiftFragment.newInstance();
            addFragmentToBackStack(fragment, R.id.fragmentContainer);
        }
    }

    private void addFragmentToBackStack(StatisticalGiftFragment fragment, int frameId) {
        Precondition.checkNotNull(fragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.findViewById(android.R.id.content).findViewById(R.id.fragmentContainer).setPadding(0, 0, 0, 0);
    }
}
