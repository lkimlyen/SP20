package com.demo.sp19.screen.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.demo.architect.data.helper.SharedPreferenceHelper;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.app.base.BaseFragment;
import com.demo.sp19.screen.gift.GiftFragment;
import com.demo.sp19.screen.gift.GiftModule;
import com.demo.sp19.screen.gift.GiftPresenter;
import com.demo.sp19.screen.home.HomeFragment;
import com.demo.sp19.screen.home.HomeModule;
import com.demo.sp19.screen.home.HomePresenter;
import com.demo.sp19.screen.notification.NotificationFragment;
import com.demo.sp19.screen.notification.NotificationModule;
import com.demo.sp19.screen.notification.NotificationPresenter;
import com.demo.sp19.screen.stock.StockFragment;
import com.demo.sp19.screen.stock.StockModule;
import com.demo.sp19.screen.stock.StockPresenter;
import com.demo.sp19.screen.timekeeping.TimekeepingFragment;
import com.demo.sp19.screen.timekeeping.TimekeepingModule;
import com.demo.sp19.screen.timekeeping.TimekeepingPresenter;
import com.demo.sp19.util.Precondition;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

/**
 * Created by MSI on 26/11/2017.
 */

public class DashboardFragment extends BaseFragment implements DashboardContract.View {
    private final String TAG = DashboardFragment.class.getName();
    private DashboardContract.Presenter mPresenter;

    public static boolean positionHomeFragment;
    private boolean isClick = true;

    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.bt_retry)
    Button btRetry;
    @BindView(R.id.number_progress_bar)
    NumberProgressBar numberProgressBar;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;

    @BindView(R.id.tv_total_task)
    TextView tvTotalTask;

    @BindView(R.id.tv_warning)
    TextView tvWarning;
    @Inject
    HomePresenter homePresenter;

    @Inject
    NotificationPresenter notificationPresenter;

    @Inject
    StockPresenter stockPresenter;

    @Inject
    DashboardPresenter dashboardPresenter;

    @Inject
    TimekeepingPresenter timekeepingPresenter;

    @Inject
    GiftPresenter giftPresenter;
    HomeFragment homeFragment;
    NotificationFragment notificationFragment;
    StockFragment stockFragment;
    TimekeepingFragment timekeepingFragment;
    GiftFragment giftFragment;
    private int progress;
    private CountDownTimer timer;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (giftFragment != null) {
            giftFragment.onActivityResult(requestCode, resultCode, data);
        }
        if (timekeepingFragment != null) {
            timekeepingFragment.onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == 320) {
                mPresenter.getListProduct();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        AndroidNetworking.initialize(getContext().getApplicationContext());
        ButterKnife.bind(this, view);
        configFragments();
        initView();
        return view;
    }

    private void initView() {
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
        mPresenter.downloadFromServer();
    }

    private void configFragments() {
        boolean noFragmentAdded = false;
        Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (currentFragment != null) {
            if (currentFragment instanceof HomeFragment) {
                homeFragment = (HomeFragment) currentFragment;
            } else if (currentFragment instanceof NotificationFragment) {
                notificationFragment = (NotificationFragment) currentFragment;
            } else if (currentFragment instanceof StockFragment) {
                stockFragment = (StockFragment) currentFragment;
            } else if (currentFragment instanceof TimekeepingFragment) {
                timekeepingFragment = (TimekeepingFragment) currentFragment;
            } else if (currentFragment instanceof GiftFragment) {
                giftFragment = (GiftFragment) currentFragment;
            }
        } else {
            noFragmentAdded = true;
        }

        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
        }

        if (notificationFragment == null) {
            notificationFragment = NotificationFragment.newInstance();
        }

        if (stockFragment == null) {
            stockFragment = StockFragment.newInstance();
        }

        if (timekeepingFragment == null) {
            timekeepingFragment = TimekeepingFragment.newInstance();
        }

        if (giftFragment == null) {
            giftFragment = GiftFragment.newInstance();
        }


        CoreApplication.getInstance().getApplicationComponent().plus(
                new HomeModule(homeFragment),
                new NotificationModule(notificationFragment),
                new StockModule(stockFragment),
                new TimekeepingModule(timekeepingFragment),
                new GiftModule(giftFragment),
                new DashboardModule(this)).inject(this);

        if (noFragmentAdded) {
            addFragment(homeFragment, homeFragment.getClass().toString(), R.id.fragmentContainer);
            positionHomeFragment = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (timekeepingFragment != null) {
            timekeepingFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void setPresenter(DashboardContract.Presenter presenter) {
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
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText(getString(R.string.text_notification))
                .setContentText(message)
                .setConfirmText(getString(R.string.text_ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void showSuccess(String message) {
        startDialogNoti(message, SweetAlertDialog.SUCCESS_TYPE);
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
    public void onResume() {
        super.onResume();
        showNotificationWarning();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.stop();
    }

    @OnClick(R.id.iv_home)
    public void goHome() {
        if (isClick) {
            positionHomeFragment = true;
            replaceFragment(homeFragment, homeFragment.getClass().toString(), R.id.fragmentContainer);
        }
    }

    @OnClick(R.id.iv_noti)
    public void goNoti() {
        if (isClick) {
            positionHomeFragment = false;
            replaceFragment(notificationFragment, notificationFragment.getClass().toString(), R.id.fragmentContainer);
        }
    }


    @OnClick(R.id.iv_oos)
    public void goStock() {
        if (isClick) {
            positionHomeFragment = false;
            replaceFragment(stockFragment, stockFragment.getClass().toString(), R.id.fragmentContainer);
        }
    }

    @OnClick(R.id.iv_attendance)
    public void goAttendance() {
        if (isClick) {
            positionHomeFragment = false;
            replaceFragment(timekeepingFragment, timekeepingFragment.getClass().toString(), R.id.fragmentContainer);
        }
    }

    @OnClick(R.id.iv_gift)
    public void goGift() {
        if (isClick) {
            if (com.demo.sp19.manager.UserManager.getInstance().getUser().getOutlet().isPromotion()) {
                positionHomeFragment = false;
                replaceFragment(giftFragment, giftFragment.getClass().toString(), R.id.fragmentContainer);
            } else {
                startDialogNoti(getString(R.string.text_function_not_in_outlet), SweetAlertDialog.WARNING_TYPE);
            }
        }

    }


    @Override
    public void showDialogDownload(int totalTask, int progress, int positionDownload) {
        isClick = false;
        if (homeFragment != null) {

            homeFragment.isClick = false;
        }
        tvTotalTask.setText("/" + String.valueOf(totalTask));
        rlRoot.setVisibility(View.VISIBLE);
        tvProgress.setText(String.valueOf(positionDownload));
        numberProgressBar.setProgress(progress);
        this.progress = progress;
        if (progress <= 10) {
            timer.start();
        }

    }


    public void showNotificationWarning() {
        if (SharedPreferenceHelper.getInstance(getContext()).getNotificationNew()) {
            tvWarning.setVisibility(View.VISIBLE);
        } else {
            tvWarning.setVisibility(GONE);
        }
    }

    public void goneNotificationWarning() {
        tvWarning.setVisibility(GONE);
    }

    @Override
    public void hideDialogDownload() {
        isClick = true;
        if (homeFragment != null) {
            homeFragment.isClick = true;
        }
        rlRoot.setVisibility(GONE);
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
    public void showButtonRetry(int positionTask) {
        timer.cancel();
        btRetry.setVisibility(View.VISIBLE);
        btRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btRetry.setVisibility(GONE);
                mPresenter.downloadTask(positionTask);
            }
        });
    }

    public void showView() {
        isClick = false;
    }


    public void hideView() {
        isClick = true;
    }

    private boolean doubleBackToExitPressedOnce = false;

    public void back() {
        if (isClick) {
            if (positionHomeFragment) {
                if (!doubleBackToExitPressedOnce) {
                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(getContext(), "Nhấn back thêm 1 lần để thoát", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);
                } else {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(getString(R.string.text_notification))
                            .setContentText(getString(R.string.text_close_app))
                            .setConfirmText(getString(R.string.text_yes))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    getActivity().finishAffinity();
                                    System.exit(0);
                                }
                            })

                            .setCancelText(getString(R.string.text_no))
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();

                }
            } else {
                goHome();
            }
        }

    }


}
