package com.demo.sp19.screen.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.demo.architect.data.model.UserEntity;
import com.demo.sp19.R;
import com.demo.sp19.app.base.BaseFragment;
import com.demo.sp19.manager.UserManager;
import com.demo.sp19.screen.confirm_set.ConfirmSetActivity;
import com.demo.sp19.screen.dashboard.DashboardFragment;
import com.demo.sp19.screen.emergency_report.EmergencyReportActivity;
import com.demo.sp19.screen.posm.POSMActivity;
import com.demo.sp19.screen.request_gift.RequestGiftActivity;
import com.demo.sp19.screen.setting.SettingActivity;
import com.demo.sp19.screen.take_off_volumn.TakeOffVolumnActivity;
import com.demo.sp19.util.Precondition;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class HomeFragment extends BaseFragment implements HomeContract.View {
    private final String TAG = HomeFragment.class.getName();
    private HomeContract.Presenter mPresenter;
    // public FunctionAdapter functionAdapter;
    public boolean isClick = true;
    private int countUpload = 0;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.tv_total)
    TextView tvTotal;

    @BindView(R.id.ll_button)
    LinearLayout lLButton;
    @BindView(R.id.bt_retry)
    Button btRetry;
    @BindView(R.id.number_progress_bar)
    NumberProgressBar numberProgressBar;

    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.rv_logo)
    RecyclerView rvLogo;
    @BindView(R.id.tv_project_name)
    TextView tvProjectName;

    @BindView(R.id.tv_outlet_name)
    TextView tvOutletName;

    @BindView(R.id.tv_outlet_address)
    TextView tvOutletAddress;

    @BindView(R.id.tv_team_name)
    TextView tvTeamName;

    @BindView(R.id.tv_number_upload)
    TextView tvNumberUpload;
    private int progress;
    private CountDownTimer timer;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mPresenter.getInfoUser();
        timer = new CountDownTimer(9000, 1000) {
            public void onTick(long millisUntilFinished) {
                progress = progress + 10;
                numberProgressBar.setProgress(progress);
                if (progress >= 90) {
                    cancel();
                }
            }

            public void onFinish() {

            }
        };

    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        this.mPresenter = Precondition.checkNotNull(presenter);
    }

    @Override
    public void showProgressBar() {
        showProgressDialog();
    }

    @Override
    public void hideProgressBar() {
        hideProgressDialog();
    }

    @Override
    public void showError(String message) {
        startDialogNoti(message, SweetAlertDialog.ERROR_TYPE);
    }

    @Override
    public void showSuccess(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.stop();
    }


    private void startDialogNoti(String content, int type) {
        Activity activity = getActivity();
        if (activity != null) {
            new SweetAlertDialog(activity, type)
                    .setTitleText(getString(R.string.text_sweet_dialog_title))
                    .setContentText(content)
                    .setConfirmText(getString(R.string.text_ok))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void showListLogo(List<Integer> list) {

    }

    @Override
    public void showCountNumberWaiting(int count) {
        //functionAdapter.updateCount(count);
        countUpload = count;
        if (count > 0){

            tvNumberUpload.setText(String.valueOf(count));
            tvNumberUpload.setVisibility(View.VISIBLE);
        }else {

            tvNumberUpload.setVisibility(View.GONE);
        }
        tvTotal.setText(String.format(getString(R.string.text_total_upload), count));
    }


    @Override
    public void showDialogDownload(int progress, int positionDownload) {
        //functionAdapter.setClickable(false);
        isClick = false;
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof DashboardFragment) {
            ((DashboardFragment) parentFragment).showView();
        }
        rlRoot.setVisibility(View.VISIBLE);
        tvProgress.setText(String.valueOf(positionDownload));
        numberProgressBar.setProgress(progress);
        this.progress = progress;
        if (progress <= 10) {
            timer.start();
        }

    }

    @Override
    public void hideDialogDownload() {
        lLButton.setVisibility(View.GONE);
        rlRoot.setVisibility(View.GONE);
        // functionAdapter.setClickable(true);
        isClick = true;
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof DashboardFragment) {
            ((DashboardFragment) parentFragment).hideView();
        }
    }

    @Override
    public void updateDialogDownload(int progressTask, int progress) {
        tvProgress.setText(String.valueOf(progressTask));
        numberProgressBar.setProgress(progress);
        this.progress = progress;
        if (progress == 100) {
            timer.cancel();
        } else {
            timer.start();
        }
    }

    @Override
    public void showButtonRetry() {
        timer.cancel();
        lLButton.setVisibility(View.VISIBLE);
        btRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lLButton.setVisibility(View.GONE);
                mPresenter.uploadAllData();
            }
        });
    }

    @Override
    public void showInfoUser(UserEntity userEntity) {
        tvProjectName.setText(userEntity.getProjectName());
        tvOutletAddress.setText(String.format(getString(R.string.text_outlet_address), userEntity.getOutlet().getAddress()));
        tvOutletName.setText(String.format(getString(R.string.text_outlet), userEntity.getOutlet().getOutletName()));
        tvTeamName.setText(String.format(getString(R.string.text_account_login), userEntity.getName()));
    }

    @OnClick(R.id.bt_cancel)
    public void cancel() {
        hideDialogDownload();
        mPresenter.countNumberUpload();
    }

    @OnClick(R.id.rv_off_take_volume)
    public void goToOffTakeVolume() {
        if (isClick) {
            TakeOffVolumnActivity.start(getContext());
        }
    }

    @OnClick(R.id.rv_report_alert)
    public void goToReportAlert() {
        if (isClick) {
            EmergencyReportActivity.start(getContext());
        }
    }

    @OnClick(R.id.rv_posm_hvb)
    public void goToPOSM() {
        if (isClick) {
            POSMActivity.start(getContext());
        }
    }

    @OnClick(R.id.rv_request_gift)
    public void goToRequestGift() {
        if (isClick) {
            if (UserManager.getInstance().getUser().getOutlet().isPromotion()) {
                RequestGiftActivity.start(getContext());

            } else {
                startDialogNoti(getString(R.string.text_function_not_in_outlet), SweetAlertDialog.WARNING_TYPE);
            }
        }
    }

    @OnClick(R.id.rv_confirm_gift)
    public void goToConfirmGift() {
        if (isClick) {
            if (UserManager.getInstance().getUser().getOutlet().isPromotion()) {
                ConfirmSetActivity.start(getContext());

            } else {
                startDialogNoti(getString(R.string.text_function_not_in_outlet), SweetAlertDialog.WARNING_TYPE);
            }
        }
    }

    @OnClick(R.id.rv_upload)
    public void uploadAllData() {
        if (isClick) {
            if (countUpload == 0) {
                showError(getString(R.string.text_no_data_upload));
                return;
            }
            mPresenter.uploadAllData();
        }
    }

    @OnClick(R.id.rv_setting)
    public void goToSetting() {
        if (isClick) {
            SettingActivity.start(getActivity());
        }
    }

}
