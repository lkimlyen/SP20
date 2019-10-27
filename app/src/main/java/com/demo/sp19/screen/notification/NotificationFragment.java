package com.demo.sp19.screen.notification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.demo.architect.data.model.offline.NotificationModel;
import com.demo.sp19.R;
import com.demo.sp19.adapter.NotificationAdapter;
import com.demo.sp19.app.base.BaseFragment;
import com.demo.sp19.screen.dashboard.DashboardFragment;
import com.demo.sp19.util.Precondition;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class NotificationFragment extends BaseFragment implements NotificationContract.View {
    private final String TAG = NotificationFragment.class.getName();
    private NotificationContract.Presenter mPresenter;

    @BindView(R.id.srl_noti)
    SwipeRefreshLayout srlNoti;

    @BindView(R.id.rv_noti)
    RecyclerView rvNoti;

    @BindView(R.id.tv_not_data)
    TextView tvNotData;

    private NotificationAdapter adapter;

    public NotificationFragment() {
        // Required empty public constructor
    }


    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
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
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        srlNoti.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.downloadNotification();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvNoti.setLayoutManager(layoutManager);
        mPresenter.downloadNotification();

    }

    @Override
    public void setPresenter(NotificationContract.Presenter presenter) {
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
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
    public void showNotification(List<NotificationModel> list) {
        srlNoti.setRefreshing(false);
        adapter = new NotificationAdapter(list, getContext());
        rvNoti.setAdapter(adapter);
        if (list.size() > 0){
            tvNotData.setVisibility(View.GONE);
        }else {
            tvNotData.setVisibility(View.VISIBLE);
        }
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof DashboardFragment) {
            ((DashboardFragment) parentFragment).goneNotificationWarning();
        }
    }
}
