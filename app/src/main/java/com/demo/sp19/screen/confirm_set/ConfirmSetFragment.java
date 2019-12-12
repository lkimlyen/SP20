package com.demo.sp19.screen.confirm_set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.demo.architect.data.model.ConfirmSetEntity;
import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.BrandSetModel;
import com.demo.sp19.R;
import com.demo.sp19.adapter.ConfirmSetAdapter;
import com.demo.sp19.app.base.BaseFragment;
import com.demo.sp19.util.Precondition;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MSI on 26/11/2017.
 */

public class ConfirmSetFragment extends BaseFragment implements ConfirmSetContract.View {
    private final String TAG = ConfirmSetFragment.class.getName();
    private ConfirmSetContract.Presenter mPresenter;
    private ConfirmSetAdapter adapter;
    @BindView(R.id.lv_request)
    ListView lvRequest;

    @BindView(R.id.srl_request)
    SwipeRefreshLayout srlRequest;

    @BindView(R.id.tv_not_data)
    TextView tvNoData;

    public ConfirmSetFragment() {
        // Required empty public constructor
    }


    public static ConfirmSetFragment newInstance() {
        ConfirmSetFragment fragment = new ConfirmSetFragment();
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
        View view = inflater.inflate(R.layout.fragment_confirm_set, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        srlRequest.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mPresenter.getListConfirmRequestGift(false);
            }
        });
        mPresenter.getListConfirmRequestGift(false);
    }

    @Override
    public void setPresenter(ConfirmSetContract.Presenter presenter) {
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
    public void showListConfirm(List<ConfirmSetEntity> confirmList, LinkedHashMap<Integer, LinkedHashMap<BrandModel, BrandSetModel>> brandSetList) {
        srlRequest.setRefreshing(false);
        adapter = new ConfirmSetAdapter(new ConfirmSetAdapter.OnConfirmListener() {
            @Override
            public void onConfirm(int id) {
                mPresenter.confirmSet(id);
            }
        }, confirmList, brandSetList);
        lvRequest.setAdapter(adapter);
        if (confirmList.size() > 0){

            tvNoData.setVisibility(View.GONE);
        }else {
            tvNoData.setVisibility(View.VISIBLE);
        }
    }
}
