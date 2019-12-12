package com.demo.sp19.screen.statistical_gift;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.offline.CustomerGiftModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.utils.view.ConvertUtils;
import com.demo.sp19.R;
import com.demo.sp19.adapter.StatisticalGiftAdapter;
import com.demo.sp19.app.base.BaseFragment;
import com.demo.sp19.util.Precondition;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class StatisticalGiftFragment extends BaseFragment implements StatisticalGiftContract.View {
    private final String TAG = StatisticalGiftFragment.class.getName();
    private StatisticalGiftContract.Presenter mPresenter;

    public StatisticalGiftFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.et_date)
    EditText etDate;

    @BindView(R.id.lv_report)
    RecyclerView rvReport;

    @BindView(R.id.tv_not_data)
    TextView tvNotData;

    StatisticalGiftAdapter adapter;

    public static StatisticalGiftFragment newInstance() {
        StatisticalGiftFragment fragment = new StatisticalGiftFragment();
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
        View view = inflater.inflate(R.layout.fragment_statistical_gift, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        etDate.setText(ConvertUtils.getDateTimeCurrentShort());
        mPresenter.getListCustomerGiftByDate(ConvertUtils.getDateTimeCurrentShort());
    }

    @Override
    public void setPresenter(StatisticalGiftContract.Presenter presenter) {
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

    }

    @Override
    public void showSuccess(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        getActivity().finish();
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
    public void showListCustomerGift(LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list) {

        adapter = new StatisticalGiftAdapter(list, getContext());
        rvReport.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rvReport.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        rvReport.setAdapter(adapter);
        if (list.size() == 0){
            tvNotData.setVisibility(View.VISIBLE);
        }else {
            tvNotData.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.et_date)
    public void chooseDate() {
        final Calendar currentDate = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = (dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth + "";
                String month = ((monthOfYear + 1) < 10) ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "";
                etDate.setText(date + "/" + month + "/" + year);
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        datePickerDialog.getDatePicker();
        datePickerDialog.show();
    }

    @OnClick(R.id.bt_search)
    public void search() {
        if (TextUtils.isEmpty(etDate.getText().toString())) {
            showError(getString(R.string.text_date_null));
            return;
        }
        mPresenter.getListCustomerGiftByDate(etDate.getText().toString());
    }
}
