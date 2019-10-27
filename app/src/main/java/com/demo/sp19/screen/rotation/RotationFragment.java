package com.demo.sp19.screen.rotation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.offline.BackgroundDialModel;
import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.BrandSetDetailModel;
import com.demo.architect.data.model.offline.CustomerGiftModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.GiftModel;
import com.demo.architect.data.model.offline.ProductGiftModel;
import com.demo.architect.data.model.offline.TotalChangeGiftModel;
import com.demo.architect.data.model.offline.TotalRotationBrandModel;
import com.demo.architect.utils.view.ConvertUtils;
import com.demo.architect.utils.view.FileUtils;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.app.base.BaseFragment;
import com.demo.sp19.constants.Constants;
import com.demo.sp19.dialogs.ConfirmGiftDialog;
import com.demo.sp19.dialogs.GiftDialog;
import com.demo.sp19.manager.CurrentBrandSetManager;
import com.demo.sp19.manager.CurrentGiftManager;
import com.demo.sp19.screen.rotation_mega.RotationMegaActivity;
import com.demo.sp19.util.Precondition;
import com.demo.sp19.util.RotationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class RotationFragment extends BaseFragment implements RotationContract.View {
    private final String TAG = RotationFragment.class.getName();
    private RotationContract.Presenter mPresenter;
    private List<GiftModel> listGiftRotation = new ArrayList<>();
    private List<TotalRotationBrandModel> totalBrandList = new ArrayList<>();
    private List<TotalChangeGiftModel> totalChangeGiftList = new ArrayList<>();
    private boolean isChooseGift = false;
    private int customerId;
    @BindView(R.id.textView1)
    TextView tv;
    @BindView(R.id.rv_dial)
    RotationView rvDial;

    @BindView(R.id.iv_dial)
    ImageView ivDial;

    @BindView(R.id.iv_button)
    ImageView ivButton;

    @BindView(R.id.ll_root)
    RelativeLayout lLRoot;

    @BindView(R.id.tv_count)
    TextView tvCount;

    @BindView(R.id.iv_arrow)
    ImageView ivArrow;

    @BindView(R.id.tv_customer_name)
    TextView tvCustomerName;

    @BindView(R.id.tv_customer_phone)
    TextView tvCustomerPhone;

    @BindView(R.id.tv_gift)
    TextView tvGift;

    @BindView(R.id.ll_gift)
    LinearLayout llGift;

    @BindView(R.id.rl_choose_gift)
    RelativeLayout rlChooseGift;

    @BindView(R.id.rl_rotation)
    RelativeLayout rlRotation;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    private int countClick;

    private LinkedHashMap<GiftModel, Integer> giftRotationList = new LinkedHashMap<>();

    //key: giftId, value: so luong
    private LinkedHashMap<GiftModel, Integer> giftProductChangeList = new LinkedHashMap<>();
    private List<EditText> editTextList = new ArrayList<>();
    private int positonBrand;
    private float radius = 0;
    Animation hh;
    float gocdau = 0.0f;
    private boolean isRotation = false;
    int goccuoi = 0;
    private List<GiftModel> mGiftList = new ArrayList<>();

    //key: brandId, value: co hoan thanh set hay khong
    private LinkedHashMap<Integer, Boolean> brandChangeSetList = new LinkedHashMap<>();

    public RotationFragment() {
        // Required empty public constructor
    }


    public static RotationFragment newInstance() {
        RotationFragment fragment = new RotationFragment();
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
        View view = inflater.inflate(R.layout.fragment_rotation, container, false);
        ButterKnife.bind(this, view);
        customerId = getActivity().getIntent().getIntExtra(Constants.CUSTOMER_ID, 0);
        initView();
        return view;
    }

    private void initView() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int min = Math.min(height, width);
        radius = min / 2.5f;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) ((min / 2) * 2), (int) ((min / 2) * 2));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        rvDial.setLayoutParams(layoutParams);
        ivDial.setLayoutParams(layoutParams);

        ivButton.setOnClickListener(onClickListener);
        mPresenter.getInfoCustomerById(customerId);
        // mPresenter.getInfoCusCrash(orderCode, phone);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (countClick == 0) {
                showError(getString(R.string.text__depleted_plays));
                return;
            }
            if (mGiftList.size() == 0) {
                Toast.makeText(getContext(), "Đang load quà, vui lòng đợi!", Toast.LENGTH_SHORT).show();
                return;
            }
            isRotation = true;
            ivButton.setOnClickListener(null);
            startAnimation();
        }
    };

    @Override
    public void setPresenter(RotationContract.Presenter presenter) {
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
        Intent returnIntent = new Intent();
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
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
    public void showListGift(List<GiftModel> giftModels, boolean rotation) {
        if (!rotation) {
            rvDial.setVisibility(View.VISIBLE);
            rvDial.setGiftModels(giftModels);
            rvDial.post(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap1 = getBitmapFromView(rvDial);
                    File file3 = FileUtils.bitmapToFile(bitmap1, CoreApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + ConvertUtils.getCodeGenerationByTime() + ".png");
                    Picasso.get().load(file3).into(ivDial);
                    rvDial.setVisibility(View.GONE);
                    mPresenter.addRotationToBackground(totalBrandList.get(positonBrand).getBrandId(), file3.getPath());

                }
            });
        }
        ivDial.setRotation(-90);
        ivDial.clearAnimation();
        gocdau = 0.0f;
        rlRotation.setVisibility(View.VISIBLE);
        rlChooseGift.setVisibility(View.GONE);
        mGiftList.clear();
        mGiftList.addAll(giftModels);
        mGiftList.addAll(giftModels);
        if (giftModels.size() < 3) {
            mGiftList.addAll(giftModels);
        }
    }

    private void addLayoutGift(BrandModel brandModel, List<ProductGiftModel> giftModels) {
        LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inf.inflate(R.layout.item_list_gift, null);
        TextView tvNameBrand = view.findViewById(R.id.tv_name);
        tvNameBrand.setText(brandModel.getBrandName());
        LinearLayout llGiftBrand = view.findViewById(R.id.ll_gift);

        TextView tvNumber = view.findViewById(R.id.tv_number);
        int total = 0;
        for (TotalChangeGiftModel totalChangeGiftModel : totalChangeGiftList) {
            if (totalChangeGiftModel.getBrandId() == brandModel.getId()) {
                total += totalChangeGiftModel.getNumberChange();
            }
        }
        tvNumber.setText(String.valueOf(total));
        llGiftBrand.removeAllViews();
        for (ProductGiftModel giftModel : giftModels) {
            View v = inf.inflate(R.layout.item_gift_product, null);
            TextView tvName = v.findViewById(R.id.tv_name);
            EditText etNumber = v.findViewById(R.id.et_number);
            etNumber.setTag(giftModel);
            tvName.setText(giftModel.getGiftModel().getGiftName());
            editTextList.add(etNumber);
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
            File file = new File(giftModel.getGiftModel().getFilePath());
            Picasso.get().load(file).into(ivGift);
            for (TotalChangeGiftModel totalChangeGiftModel : totalChangeGiftList) {
                if (giftModel.getProductID() == totalChangeGiftModel.getProductId()) {
                    etNumber.setText(String.valueOf(totalChangeGiftModel.getNumberChange()));
                    giftProductChangeList.put(giftModel.getGiftModel(), totalChangeGiftModel.getNumberChange());
                    break;
                }
            }

            etNumber.setEnabled(false);

            llGiftBrand.addView(v);
        }
        llGift.addView(view);
    }

    @Override
    public void showBackgroundDial(BackgroundDialModel backgroundDialModel) {
        Drawable drawable = Drawable.createFromPath(backgroundDialModel.getBGLayout());
        lLRoot.setBackground(drawable);
        if (TextUtils.isEmpty(backgroundDialModel.getRotation())) {
            rvDial.setBackgroundDialModel(backgroundDialModel);
        } else {
            File file = new File(backgroundDialModel.getRotation());
            Picasso.get().load(file).into(ivDial);
        }
        Bitmap bitmap = BitmapFactory.decodeFile(backgroundDialModel.getBGButton());
        if (bitmap.getWidth() > (int) (radius * 2 / 3)) {
            bitmap = FileUtils.resizeImage(bitmap, (int) (radius * 2 / 3), (int) (radius * 2 / 3));
        }
        ivButton.setImageBitmap(bitmap);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ABOVE, R.id.rl_vongxoay);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        ivArrow.setLayoutParams(layoutParams);
        File file = new File(backgroundDialModel.getBGArrow());
        Picasso.get().load(file).into(ivArrow);

    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(returnedBitmap);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }


    @Override
    public void showListGiftProduct(LinkedHashMap<BrandModel, List<ProductGiftModel>> list) {
        rlRotation.setVisibility(View.GONE);
        rlChooseGift.setVisibility(View.VISIBLE);
        llGift.removeAllViews();
        for (Map.Entry<BrandModel, List<ProductGiftModel>> map : list.entrySet()) {
            addLayoutGift(map.getKey(), map.getValue());
        }

    }

    @Override
    public void showInfoCus(LinkedHashMap<String, String> listInfo) {

    }

    @Override
    public void showBrandSetDetail(List<BrandSetDetailModel> list) {
        listGiftRotation.clear();
        for (BrandSetDetailModel brandSetDetailModel : list) {
            for (int i = 0; i < brandSetDetailModel.getNumber(); i++) {
                listGiftRotation.add(brandSetDetailModel.getGiftModel());
            }
        }
    }

    @Override
    public void showAllListGift(List<GiftModel> list) {

    }

    @Override
    public void goToRotationMega(String string, int customerId) {
        Toast.makeText(getContext(), string, Toast.LENGTH_SHORT).show();
        RotationMegaActivity.start(getActivity(), customerId, false);
        getActivity().finish();
    }

    @Override
    public void showInfoCustomerAndListTotalBrand(CustomerModel customerModel, List<TotalRotationBrandModel> list, List<TotalChangeGiftModel> totalChangeGiftModels) {
        tvCustomerName.setText(customerModel.getCustomerName());
        tvCustomerPhone.setText(customerModel.getCustomerPhone());
        totalBrandList = list;
        totalChangeGiftList = totalChangeGiftModels;
        if (list.size() > 0) {
            positonBrand = 0;
            countClick = list.get(positonBrand).getNumberTotal() - list.get(positonBrand).getNumberTurned();
            tvCount.setText(String.format(getString(R.string.text_have_turns), countClick));
            mPresenter.getListBackGround(list.get(positonBrand).getBrandId());
        } else {
            tvTitle.setText("Thông tin đổi quà");
            List<Integer> productIdList = new ArrayList<>();
            for (TotalChangeGiftModel totalChangeGiftModel : totalChangeGiftList) {
                productIdList.add(totalChangeGiftModel.getProductId());
            }
            isChooseGift = true;
            Collections.sort(productIdList);
            mPresenter.getListGiftByProduct(productIdList);
        }

    }


    private void startAnimation() {
        int qua = (int) (Math.random() * listGiftRotation.size());
        Log.d(TAG, new Gson().toJson(listGiftRotation.get(qua)));

        List<Integer> positionGiftList = new ArrayList<>();
        GiftModel giftModel = listGiftRotation.get(qua);
        for (int i = 0; i < mGiftList.size(); i++) {
            if (mGiftList.get(i).getId() == giftModel.getId()) {
                positionGiftList.add(i);
            }
        }
        int phan = positionGiftList.get((int) (Math.random() * positionGiftList.size()));
        int angle = 360 / mGiftList.size();
        goccuoi = phan * angle + (360 * 10) + 4;

        int gocdaugoc = goccuoi + angle - 8;
        goccuoi = (gocdaugoc) + (int) (Math.random() * (goccuoi - gocdaugoc + 1));

        hh = new RotateAnimation(gocdau, goccuoi,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        hh.setDuration(5000);
        hh.setFillAfter(true);
        if (rvDial.getVisibility() == View.VISIBLE) {
            rvDial.setAnimation(hh);
        } else {
            ivDial.setAnimation(hh);
        }

        tv.setText("Quay thử");
        // int phan = goccuoi % 360 / angle;
        if (!CurrentGiftManager.getInstance().checkCoditionGetGift(mGiftList.get(phan).getId())) {
            startAnimation();
            return;
        }
        hh.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                isRotation = false;
                countClick--;
                if (countClick == 0) {
                    tvCount.setText(getString(R.string.text_finish_turns));
                } else {
                    tvCount.setText(String.format(getString(R.string.text_have_turns), countClick));
                }

                ivButton.setOnClickListener(onClickListener);
                gocdau = goccuoi % 360;
                Log.d(TAG + "QuayThu", "Góc cuối: " + goccuoi + "Phần: " + phan);
                if (giftRotationList.get(mGiftList.get(phan)) != null) {
                    int number = giftRotationList.get(mGiftList.get(phan));
                    giftRotationList.put(mGiftList.get(phan), number + 1);
                } else {
                    giftRotationList.put(mGiftList.get(phan), 1);
                }
                CurrentGiftManager.getInstance().updateNumberGift(mGiftList.get(phan).getId());

                tvGift.setText(mGiftList.get(phan).getGiftName());
                GiftDialog giftDialog = new GiftDialog();
                giftDialog.show(getActivity().getFragmentManager(), TAG);
                giftDialog.setPath(mGiftList.get(phan).getFilePath());
                brandChangeSetList.put(totalBrandList.get(positonBrand).getBrandId(), CurrentGiftManager.getInstance().isChangeSet);
                mPresenter.updateNumberTurnAndSaveGift(customerId, totalBrandList.get(positonBrand).getId(),
                        mGiftList.get(phan).getId(), giftRotationList.get(mGiftList.get(phan)),
                        CurrentGiftManager.getInstance().getCurrentGiftList(), CurrentBrandSetManager.getInstance().getCurrentBrandModel(),
                        (positonBrand == totalBrandList.size() - 1 && countClick == 0 && totalChangeGiftList.size() == 0));
            }
        });
    }


    @OnClick(R.id.tv_next)
    public void next() {
        if (isRotation) {
            return;
        }
        if (countClick > 0) {
            startDialogNoti(getString(R.string.text_rest_rotation), SweetAlertDialog.WARNING_TYPE);
            return;
        }

        if (positonBrand < totalBrandList.size() - 1) {
            positonBrand++;
            countClick = totalBrandList.get(positonBrand).getNumberTotal() - totalBrandList.get(positonBrand).getNumberTurned();
            tvCount.setText(String.format(getString(R.string.text_have_turns), countClick));
            mPresenter.getListBackGround(totalBrandList.get(positonBrand).getBrandId());
        } else if (totalChangeGiftList.size() > 0 && !isChooseGift) {
            tvTitle.setText("Thông tin đổi quà");
            List<Integer> productIdList = new ArrayList<>();
            for (TotalChangeGiftModel totalChangeGiftModel : totalChangeGiftList) {
                productIdList.add(totalChangeGiftModel.getProductId());
            }
            Collections.sort(productIdList);
            isChooseGift = true;
            mPresenter.getListGiftByProduct(productIdList);
        } else {
            giftRotationList.clear();
            for (CustomerGiftModel customerGiftModel : mPresenter.getGiftByCustomer(customerId)) {
                giftRotationList.put(customerGiftModel.getGiftModel(), customerGiftModel.getNumberGift());
            }
            giftRotationList.putAll(giftProductChangeList);
            ConfirmGiftDialog dialog = new ConfirmGiftDialog();
            dialog.show(getActivity().getFragmentManager(), TAG);
            dialog.setGiftList(giftRotationList);
            dialog.setListener(new ConfirmGiftDialog.OnConfirmListener() {
                @Override
                public void onConfirm() {
                    if (brandChangeSetList.size() > 0) {
                        mPresenter.confirmChangeSet(customerId, brandChangeSetList, giftProductChangeList);
                    } else if (giftProductChangeList.size() > 0) {
                        mPresenter.saveGift(customerId, giftProductChangeList);
                    } else {
                        mPresenter.goToMega(customerId);
                    }
                }

            });


        }
    }

}