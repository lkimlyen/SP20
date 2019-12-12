package com.demo.sp19.screen.rotation_mega;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.architect.data.model.OutletEntiy;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.GiftMegaModel;
import com.demo.architect.data.model.offline.TimeRotationModel;
import com.demo.architect.data.model.offline.TotalRotationModel;
import com.demo.architect.utils.view.ConvertUtils;
import com.demo.architect.utils.view.FileUtils;
import com.demo.sp19.R;
import com.demo.sp19.app.CoreApplication;
import com.demo.sp19.app.base.BaseFragment;
import com.demo.sp19.constants.Constants;
import com.demo.sp19.dialogs.GiftDialog;
import com.demo.sp19.manager.MegaGiftManager;
import com.demo.sp19.manager.UserManager;
import com.demo.sp19.util.Precondition;
import com.demo.sp19.util.RotationMegaView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MSI on 26/11/2017.
 */

public class RotationMegaFragment extends BaseFragment implements RotationMegaContract.View {
    private final String TAG = RotationMegaFragment.class.getName();
    private RotationMegaContract.Presenter mPresenter;
    //private List<GiftModel> listGiftRotation = new ArrayList<>();
    private int customerId;
    private List<Integer> listPrize;
    private boolean isLucky = false;
    @BindView(R.id.textView1)
    TextView tv;
    @BindView(R.id.rv_dial)
    RotationMegaView rvDial;

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

    private int countClick;

    private int totalRotation;


    private float radius = 0;
    Animation hh;
    float gocdau = 0.0f;
    private boolean isRotation = false;
    int goccuoi = 0;
    private List<GiftMegaModel> mGiftList = new ArrayList<>();

    public RotationMegaFragment() {
        // Required empty public constructor
    }


    public static RotationMegaFragment newInstance() {
        RotationMegaFragment fragment = new RotationMegaFragment();
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
        View view = inflater.inflate(R.layout.fragment_rotation_mega, container, false);
        ButterKnife.bind(this, view);
        customerId = getActivity().getIntent().getIntExtra(Constants.CUSTOMER_ID, 0);
        initView();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.CUSTOMER_ID, customerId);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            customerId = savedInstanceState.getInt(Constants.CUSTOMER_ID);
        }
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

        mPresenter.getListGift();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_start_rotation);
        if (bitmap.getWidth() > (int) (radius * 2 / 3)) {
            bitmap = FileUtils.resizeImage(bitmap, (int) (radius * 2 / 3), (int) (radius * 2 / 3));
        }
        ivButton.setImageBitmap(bitmap);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams2.addRule(RelativeLayout.ABOVE, R.id.rl_vongxoay);
        layoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL);
        ivArrow.setLayoutParams(layoutParams2);
        Picasso.get().load(R.drawable.ic_arrow_red).into(ivArrow);
        mPresenter.getInfoCusById(customerId);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (countClick == 0) {
                showError(getString(R.string.text__depleted_plays));
                return;
            }

            if(mGiftList.size() == 0){
                Toast.makeText(getContext(), "Đang load quà, vui lòng đợi!", Toast.LENGTH_SHORT).show();
                return;
            }
            isRotation = true;
            ivButton.setOnClickListener(null);
            startAnimation();
        }
    };

    @Override
    public void setPresenter(RotationMegaContract.Presenter presenter) {
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
    public void showListGift(TimeRotationModel timeRotationModel) {
        if (TextUtils.isEmpty(timeRotationModel.getImageRotation())) {
            rvDial.setVisibility(View.VISIBLE);
            rvDial.setGiftModels(timeRotationModel.getGiftList());
            rvDial.post(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap1 = getBitmapFromView(rvDial);
                    File file3 = FileUtils.bitmapToFile(bitmap1, CoreApplication.getInstance().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + ConvertUtils.getCodeGenerationByTime() + ".png");
                    Picasso.get().load(file3).into(ivDial);
                    rvDial.setVisibility(View.GONE);
                    mPresenter.addImageRotaion(timeRotationModel.getId(), file3.getPath());

                }
            });
        } else {
            File file = new File(timeRotationModel.getImageRotation());
            Picasso.get().load(file).into(ivDial);
        }
        ivDial.setRotation(-90);
        ivDial.clearAnimation();
        gocdau = 0.0f;
        rlRotation.setVisibility(View.VISIBLE);
        rlChooseGift.setVisibility(View.GONE);
        mGiftList.clear();
        GiftMegaModel noLucky = null;
        for (GiftMegaModel giftMegaModel : timeRotationModel.getGiftList()) {
            if (!giftMegaModel.isGift()) {
                noLucky = giftMegaModel;
                break;
            }
        }

        for (GiftMegaModel giftMegaModel : timeRotationModel.getGiftList()) {
            if (giftMegaModel.isGift()) {
                mGiftList.add(giftMegaModel);
                mGiftList.add(noLucky);
            }
        }
    }

    @Override
    public void showInfoCus(CustomerModel customerModel, TotalRotationModel totalRotation) {
        tvCustomerName.setText(customerModel.getCustomerName());
        tvCustomerPhone.setText(customerModel.getCustomerPhone());
        this.totalRotation = totalRotation.getNumber() - totalRotation.getNumberTurned();
        countClick = this.totalRotation;
        tvCount.setText(String.format(getString(R.string.text_have_turns), this.totalRotation));
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


    private void startAnimation() {

        GiftMegaModel giftModel = null;
        OutletEntiy outletEntiy = UserManager.getInstance().getUser().getOutlet();
        if (MegaGiftManager.getInstance().getMegaGiftObject() == null) {
            for (GiftMegaModel giftMegaModel : mGiftList) {
                if (!giftMegaModel.isGift()) {
                    giftModel = giftMegaModel;
                    break;
                }
            }
        } else {
            listPrize = MegaGiftManager.getInstance().getListPrize(outletEntiy.getOutletId());
            if (listPrize != null && listPrize.size() > 0 && listPrize.get(0) != 0) {
                for (GiftMegaModel giftMegaModel : mGiftList) {
                    if (giftMegaModel.getId() == listPrize.get(0)) {
                        giftModel = giftMegaModel;
                        break;
                    }
                }
            } else {
                for (GiftMegaModel giftMegaModel : mGiftList) {
                    if (!giftMegaModel.isGift()) {
                        giftModel = giftMegaModel;
                        break;
                    }
                }

            }

        }

        final GiftMegaModel giftMegaModel = giftModel;

        int phan = mGiftList.indexOf(giftMegaModel);

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

                GiftDialog giftDialog = new GiftDialog();
                giftDialog.show(getActivity().getFragmentManager(), TAG);
                giftDialog.setPath(mGiftList.get(phan).getFilePath());
                giftDialog.setGift(giftMegaModel.isGift());

                if (giftMegaModel.isGift()) {
                    MegaGiftManager.getInstance().removeListPrize();
                    isLucky = true;
                    mPresenter.saveInfoCusLucky(customerId, giftMegaModel.getId());
                    tvGift.setText(1 + " " + giftMegaModel.getGiftName() );
                } else {
                    if (listPrize != null && listPrize.size() > 0) {
                        listPrize.remove(0);
                        MegaGiftManager.getInstance().updateListPrize(outletEntiy.getOutletId(), listPrize);
                    }
                }
                mPresenter.updateNumberRotation(customerId);
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

        mPresenter.saveTotalRotaion(customerId, totalRotation, isLucky);
    }
}