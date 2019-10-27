package com.demo.sp19.screen.take_off_volumn;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.model.offline.TakeOffVolumnModel;
import com.demo.sp19.R;
import com.demo.sp19.adapter.TakeOffVolumnAdapter;
import com.demo.sp19.app.base.BaseFragment;
import com.demo.sp19.util.Precondition;

import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class TakeOffVolumnFragment extends BaseFragment implements TakeOffVolumnContract.View {
    private final String TAG = TakeOffVolumnFragment.class.getName();
    private TakeOffVolumnContract.Presenter mPresenter;
    private TakeOffVolumnAdapter adapter;
    @BindView(R.id.rv_stock)
    RecyclerView rvStock;
    public TakeOffVolumnFragment() {
        // Required empty public constructor
    }

    public static TakeOffVolumnFragment newInstance() {
        TakeOffVolumnFragment fragment = new TakeOffVolumnFragment();
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
        View view = inflater.inflate(R.layout.fragment_take_off_volumn, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        GridLayoutManager gridLayoutManager;
        Resources r = getResources();
        int px = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 150, r.getDisplayMetrics()));
        if (width / 3 > px) {
            gridLayoutManager = new GridLayoutManager(getContext(), 3);
        } else {
            gridLayoutManager = new GridLayoutManager(getContext(), 2);
        }

        rvStock.setLayoutManager(gridLayoutManager);
        mPresenter.getListTakeOffVolumn();

    }

    @Override
    public void setPresenter(TakeOffVolumnContract.Presenter presenter) {
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
        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(getString(R.string.text_sweet_dialog_title))
                .setContentText(message)
                .setConfirmText(getString(R.string.text_ok))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        getActivity().finish();
                    }
                })
                .show();
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
    public void showListTakeOffVolumn(LinkedHashMap<ProductModel, TakeOffVolumnModel> list) {
        adapter = new TakeOffVolumnAdapter(list, getContext());
        rvStock.setAdapter(adapter);
    }

    @OnClick(R.id.iv_save)
    public void save() {

        if (adapter.getListTakeOffVolumnEdit().size() == 0){
            return;
        }
        if ((adapter.getListTakeOffVolumnEdit().size() < adapter.getItemCount() && adapter.getListStockSave().size() == 0) || adapter.idRemoveList().size() > 0){
            showError(getString(R.string.text_number_not_empty));
            return;
        }
        mPresenter.saveListTakeOffVolumn(adapter.getListTakeOffVolumnEdit());
    }


    @OnClick(R.id.iv_back)
    public void back(){
        getActivity().finish();
    }
}
