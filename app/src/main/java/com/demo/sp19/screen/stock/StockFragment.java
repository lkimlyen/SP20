package com.demo.sp19.screen.stock;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.offline.NoteStockModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.data.model.offline.StockModel;
import com.demo.sp19.R;
import com.demo.sp19.adapter.StockAdapter;
import com.demo.sp19.app.base.BaseFragment;
import com.demo.sp19.screen.dashboard.DashboardFragment;
import com.demo.sp19.util.Precondition;

import java.util.Calendar;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class StockFragment extends BaseFragment implements StockContract.View {
    private final String TAG = StockFragment.class.getName();
    private StockContract.Presenter mPresenter;
    private StockAdapter adapter;
    private int typeSave;
    @BindView(R.id.rv_stock)
    RecyclerView rvStock;

    @BindView(R.id.rb_product_in_posm)
    RadioButton rbProductPosm;

    @BindView(R.id.rb_product_in_warehouse)
    RadioButton rbProductWarehouse;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    public StockFragment() {
        // Required empty public constructor
    }


    public static StockFragment newInstance() {
        StockFragment fragment = new StockFragment();
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
        View view = inflater.inflate(R.layout.fragment_stock, container, false);
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
        mPresenter.getListStock();
        mPresenter.getNoteStock();

        Calendar calendar1 = Calendar.getInstance();
        //lấy giờ hiện tại
        int hour = calendar1.get(Calendar.HOUR_OF_DAY);
        rbProductPosm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //nếu giờ > = 10h thì không cho cập nhật nữa
                if (hour >= 10) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getContext().getString(R.string.text_notification))
                            .setContentText(getContext().getString(R.string.text_close_oos))
                            .setConfirmText(getContext().getString(R.string.text_ok))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                    if (typeSave != -1) {
                        if (typeSave == 0) {
                            rbProductPosm.setChecked(false);
                            rbProductWarehouse.setChecked(true);
                        } else {

                            rbProductPosm.setChecked(true);
                            rbProductWarehouse.setChecked(false);
                        }
                    } else {
                        rbProductPosm.setChecked(false);
                        rbProductWarehouse.setChecked(true);
                    }
                }


            }
        });

        rbProductWarehouse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (hour >= 10) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getContext().getString(R.string.text_notification))
                            .setContentText(getContext().getString(R.string.text_close_oos))
                            .setConfirmText(getContext().getString(R.string.text_ok))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();
                    if (typeSave != -1) {
                        if (typeSave == 0) {
                            rbProductPosm.setChecked(false);
                            rbProductWarehouse.setChecked(true);
                        } else {

                            rbProductPosm.setChecked(true);
                            rbProductWarehouse.setChecked(false);
                        }
                    } else {
                        rbProductPosm.setChecked(false);
                        rbProductWarehouse.setChecked(true);
                    }
                }


            }
        });

    }

    @Override
    public void setPresenter(StockContract.Presenter presenter) {
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
                        Fragment parentFragment = getParentFragment();
                        if (parentFragment != null && parentFragment instanceof DashboardFragment) {
                            ((DashboardFragment) parentFragment).goHome();
                        }
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
    public void showListStock(LinkedHashMap<ProductModel, StockModel> list) {
        adapter = new StockAdapter(list, getContext());
        rvStock.setAdapter(adapter);
    }

    @Override
    public void showNoteStock(NoteStockModel noteStockModel) {
        if (noteStockModel != null) {
            typeSave = noteStockModel.getNumberType();
            switch (noteStockModel.getNumberType()) {
                case 0:
                    rbProductWarehouse.setChecked(true);
                    break;
                case 1:
                    rbProductPosm.setChecked(true);
                    break;
            }


        } else {
            rbProductWarehouse.setChecked(true);
            typeSave = -1;
        }
    }

    @OnClick(R.id.iv_save)
    public void save() {
        Calendar calendar1 = Calendar.getInstance();
        int hour = calendar1.get(Calendar.HOUR_OF_DAY);
        if (hour >= 10) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getContext().getString(R.string.text_notification))
                    .setContentText(getContext().getString(R.string.text_close_oos))
                    .setConfirmText(getContext().getString(R.string.text_ok))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .show();
            return;
        }
//kiểm tra stock đã nhập đầy đủ hay chưa
        if ((adapter.getListStockEdit().size() < adapter.getItemCount() && adapter.getListStockSave().size() == 0) || adapter.idRemoveList().size() > 0) {
            showError(getString(R.string.text_number_oos_not_empty));
            return;
        }
        int type;
        if (rbProductPosm.isChecked()) {
            type = 1;
        } else {
            type = 0;
        }
        //kiểm tra user chỉ chỉnh sửa note hay chỉnh sửa số liệu của stock
        if (adapter.getListStockEdit().size() > 0) {
            mPresenter.saveListStock(adapter.getListStockEdit(), typeSave != type ? type : -1);
        } else {
            if (typeSave != type) {
                mPresenter.saveNoteStock(type, false);
            }

        }

    }
}
