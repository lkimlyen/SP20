package com.demo.sp19.screen.emergency_report;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.offline.EmergencyModel;
import com.demo.architect.data.model.offline.ReasonModel;
import com.demo.sp19.R;
import com.demo.sp19.adapter.EmergencyReportAdapter;
import com.demo.sp19.adapter.ReasonAdapter;
import com.demo.sp19.app.base.BaseFragment;
import com.demo.sp19.util.Precondition;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.RealmResults;

/**
 * Created by MSI on 26/11/2017.
 */

public class EmergencyReportFragment extends BaseFragment implements EmergencyReportContract.View {
    private final String TAG = EmergencyReportFragment.class.getName();
    private EmergencyReportContract.Presenter mPresenter;

    public EmergencyReportFragment() {
        // Required empty public constructor
    }

    private EmergencyReportAdapter emergencyReportAdapter;
    private ReasonAdapter adapter;
    @BindView(R.id.rv_reason)
    RecyclerView rvReason;

    @BindView(R.id.et_employee_id)
    EditText etEmployeeId;

    @BindView(R.id.bt_begin)
    Button btBegin;

    @BindView(R.id.bt_end)
    Button btEnd;

    @BindView(R.id.lv_report)
    ListView lvReport;

    public static EmergencyReportFragment newInstance() {
        EmergencyReportFragment fragment = new EmergencyReportFragment();
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
        View view = inflater.inflate(R.layout.fragment_emergency_report, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvReason.setLayoutManager(gridLayoutManager);
        mPresenter.getListReason();
        mPresenter.getEmergencyNotFinished();
    }

    @Override
    public void setPresenter(EmergencyReportContract.Presenter presenter) {
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
        startDialogNoti(message, SweetAlertDialog.SUCCESS_TYPE);
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
    public void showListReason(List<ReasonModel> list) {
        adapter = new ReasonAdapter(list, getContext());
        rvReason.setAdapter(adapter);
    }

    @Override
    public void showEmergency(RealmResults<EmergencyModel> list) {
        if (list.size() > 0) {
            emergencyReportAdapter = new EmergencyReportAdapter(list, new EmergencyReportAdapter.OnEndListener() {
                @Override
                public void onEnd(int id) {
                    mPresenter.completeEmergency(id);
                }
            });
            lvReport.setAdapter(emergencyReportAdapter);
        }
    }

    @OnClick(R.id.bt_begin)
    public void begin() {
        if (TextUtils.isEmpty(etEmployeeId.getText().toString())) {
            showError(getString(R.string.text_employee_id_null));
            return;
        }
        if (adapter.getReasonSelected() == null) {
            showError(getString(R.string.text_reason_null));
            return;
        }
        mPresenter.addEmergency(etEmployeeId.getText().toString(), adapter.getReasonSelected().getId());
    }
}
