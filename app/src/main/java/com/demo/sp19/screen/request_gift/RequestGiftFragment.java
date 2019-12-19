package com.demo.sp19.screen.request_gift;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.BrandSetDetailModel;
import com.demo.architect.data.model.offline.BrandSetModel;
import com.demo.architect.data.model.offline.CurrentBrandModel;
import com.demo.sp19.R;
import com.demo.sp19.app.base.BaseFragment;
import com.demo.sp19.util.Precondition;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by MSI on 26/11/2017.
 */

public class RequestGiftFragment extends BaseFragment implements RequestGiftContract.View {
    private final String TAG = RequestGiftFragment.class.getName();
    private RequestGiftContract.Presenter mPresenter;
    private LinkedHashMap<Integer, Integer> requestList = new LinkedHashMap<>();
    @BindView(R.id.ll_gift)
    LinearLayout llGift;

    @BindView(R.id.tv_send)
    TextView tvSend;

    private Realm realm = Realm.getDefaultInstance();

    public RequestGiftFragment() {
        // Required empty public constructor
    }

    public static RequestGiftFragment newInstance() {
        RequestGiftFragment fragment = new RequestGiftFragment();
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
        View view = inflater.inflate(R.layout.fragment_request_gift, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        mPresenter.getListBrandSetDetailCurrent();
    }

    @Override
    public void setPresenter(RequestGiftContract.Presenter presenter) {
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
    public void showListBrandSetDetailCurrent(LinkedHashMap<Object, List<BrandSetDetailModel>> list) {
        llGift.removeAllViews();
        for (Map.Entry<Object, List<BrandSetDetailModel>> map : list.entrySet()) {
            LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inf.inflate(R.layout.item_gift_product, null);
            TextView tvName = v.findViewById(R.id.tv_name);
            EditText etNumber = v.findViewById(R.id.et_number);
            etNumber.setTag(map.getKey());

            if (map.getKey() instanceof BrandSetModel) {
                BrandModel brandModel = realm.where(BrandModel.class).equalTo("id", ((BrandSetModel) map.getKey()).getBrandID()).findFirst();
                tvName.setText(String.format(getString(R.string.text_set_gift), brandModel.getBrandName()));
            } else {

                tvName.setText(String.format(getString(R.string.text_set_gift), ((CurrentBrandModel) map.getKey()).getBrandModel().getBrandName()));
            }

            etNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    Object productGiftModel = etNumber.getTag();
                    int number = 0;
                    try {
                        number = Integer.parseInt(s.toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (number > 0) {
                        if (productGiftModel instanceof BrandSetModel)
                            requestList.put(((BrandSetModel)productGiftModel).getId(), number);
                        else   requestList.put(((CurrentBrandModel)productGiftModel).getBrandSetID(), number);

                    } else {
                        if (productGiftModel instanceof BrandSetModel){
                            if (requestList.get(((BrandSetModel)productGiftModel).getId()) != null) {
                                requestList.remove(((BrandSetModel)productGiftModel).getId());
                            }
                        }else {
                            if (requestList.get(((CurrentBrandModel)productGiftModel).getBrandSetID()) != null) {
                                requestList.remove(((CurrentBrandModel)productGiftModel).getBrandSetID());
                            }
                        }

                    }
                }
            });
            llGift.addView(v);
        }


        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestList.size() == 0) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getString(R.string.text_notification))
                            .setContentText(getString(R.string.text_number_set_null))
                            .setConfirmText(getString(R.string.text_ok))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            }).show();
                    return;
                }
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_notification))
                        .setContentText(getString(R.string.text_do_you_want_send_request))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                mPresenter.saveRequestGift(requestList);
                            }
                        }).setCancelText(getString(R.string.text_no))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        }).show();

            }
        });

    }

    @OnClick(R.id.iv_back)
    public void back() {
        getActivity().finish();
    }
}
