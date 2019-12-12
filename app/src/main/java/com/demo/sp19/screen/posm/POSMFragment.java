package com.demo.sp19.screen.posm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.POSMModel;
import com.demo.architect.data.model.offline.POSMReportModel;
import com.demo.sp19.R;
import com.demo.sp19.app.base.BaseFragment;
import com.demo.sp19.util.Precondition;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class POSMFragment extends BaseFragment implements POSMContract.View {
    private final String TAG = POSMFragment.class.getName();
    private POSMContract.Presenter mPresenter;
    private int countPOSM = 0;
    //danh sách report posm lấy từ database
    private LinkedHashMap<POSMModel, POSMReportModel> masterList = new LinkedHashMap<>();
    //ds id report posm đã xóa
    private Set<Long> idRemoveList = new HashSet<>();

    public POSMFragment() {
        // Required empty public constructor
    }

    //ds report posm đã lưu vào database
    private List<POSMReportModel> posmReportSavedList = new ArrayList<>();
    //ds report posm đã lưu
    private LinkedHashMap<Integer, POSMReportModel> posmReportEditList = new LinkedHashMap<>();
    @BindView(R.id.ll_posm)
    LinearLayout llPosm;

    public static POSMFragment newInstance() {
        POSMFragment fragment = new POSMFragment();
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
        View view = inflater.inflate(R.layout.fragment_posm, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        mPresenter.getListPOSM();
    }

    @Override
    public void setPresenter(POSMContract.Presenter presenter) {
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
    public void showListPOSM(LinkedHashMap<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>> list) {
        llPosm.removeAllViews();
        countPOSM = 0;
        //sử dụng cách add lần lượt vào layout thay vì dùng recycleView để hiển thị theo brand
        for (Map.Entry<BrandModel, LinkedHashMap<POSMModel, POSMReportModel>> map : list.entrySet()) {
            masterList.putAll(map.getValue());
            addLayoutPOSM(map.getKey(), map.getValue());
        }
    }

    private void addLayoutPOSM(BrandModel brandModel, LinkedHashMap<POSMModel, POSMReportModel> linkedHashMap) {
        LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inf.inflate(R.layout.item_list_gift, null);
        TextView tvNameBrand = view.findViewById(R.id.tv_name);
        tvNameBrand.setText(brandModel.getBrandName());
        countPOSM += linkedHashMap.size();
        LinearLayout llGiftBrand = view.findViewById(R.id.ll_gift);

        LinearLayout llNumber = view.findViewById(R.id.ll_number);
        llNumber.setVisibility(View.GONE);
        llGiftBrand.removeAllViews();
        for (Map.Entry<POSMModel, POSMReportModel> map : linkedHashMap.entrySet()) {

            View v = inf.inflate(R.layout.item_gift_product, null);
            TextView tvName = v.findViewById(R.id.tv_name);
            EditText etNumber = v.findViewById(R.id.et_number);
            etNumber.setTag(map.getKey());
            if (map.getValue() != null) {
                posmReportSavedList.add(map.getValue());
                etNumber.setText(String.valueOf(map.getValue().getNumber()));
            }
            tvName.setText(map.getKey().getPOSMName());
            ImageView ivGift = v.findViewById(R.id.iv_gift);
            ivGift.setVisibility(View.VISIBLE);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            int min = Math.min(height, width);
            float radius = min / 2.5f;
            int handRadius = (int) (radius) / 3;
            LinearLayout.LayoutParams layoutParams;
            layoutParams = new LinearLayout.LayoutParams(handRadius, handRadius);
            ivGift.setLayoutParams(layoutParams);
            File file = new File(map.getKey().getImage());
            Picasso.get().load(file).into(ivGift);
            etNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    POSMModel posmModel = (POSMModel) etNumber.getTag();
                    UserEntity userEntity = com.demo.sp19.manager.UserManager.getInstance().getUser();
                    try {
                        int number = Integer.parseInt(s.toString());
                        POSMReportModel posmReportModel = new POSMReportModel(
                                userEntity.getOutlet().getOutletId(),
                                posmModel.getId(), number, userEntity.getTeamOutletId());
                        //xóa id đã add vào list remove
                        if (masterList.get(posmModel) != null) {
                            idRemoveList.remove(linkedHashMap.get(posmModel).getId());
                        }
                        //lưu report posm đã chỉnh sửa
                        posmReportEditList.put(posmModel.getId(), posmReportModel);


                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        //add id vào list remove
                        if (masterList.get(posmModel) != null) {
                            idRemoveList.add(linkedHashMap.get(posmModel).getId());
                        }
                        posmReportEditList.remove(posmModel.getId());
                    }


                }
            });
            llGiftBrand.addView(v);
        }
        llPosm.addView(view);
    }

    @OnClick(R.id.iv_save)
    public void save() {

        if (posmReportEditList.size() == 0 || (posmReportEditList.size() < countPOSM && posmReportSavedList.size() == 0) || idRemoveList.size() > 0) {
            showError(getString(R.string.text_number_posm_not_empty));
            return;
        }
        mPresenter.saveListPOSMReport(new ArrayList<>(posmReportEditList.values()));
    }

    @OnClick(R.id.iv_back)
    public void back() {
        getActivity().finish();
    }
}
