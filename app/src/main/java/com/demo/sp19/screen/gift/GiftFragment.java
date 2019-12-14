package com.demo.sp19.screen.gift;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.architect.data.model.UserEntity;
import com.demo.architect.data.model.offline.BrandModel;
import com.demo.architect.data.model.offline.CustomerGiftModel;
import com.demo.architect.data.model.offline.CustomerModel;
import com.demo.architect.data.model.offline.CustomerProductModel;
import com.demo.architect.data.model.offline.ImageModel;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.architect.utils.view.ConvertUtils;
import com.demo.sp19.R;
import com.demo.sp19.adapter.ImageOrderAdapter;
import com.demo.sp19.app.base.BaseFragment;
import com.demo.sp19.dialogs.ConfirmProductDialog;
import com.demo.sp19.dialogs.DetailGiftCustomerDialog;
import com.demo.sp19.enums.MainViewType;
import com.demo.sp19.manager.UserManager;
import com.demo.sp19.screen.rotation.RotationActivity;
import com.demo.sp19.screen.rotation_mega.RotationMegaActivity;
import com.demo.sp19.util.Precondition;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by MSI on 26/11/2017.
 */

public class GiftFragment extends BaseFragment implements GiftContract.View {
    private static final int REQUEST_CODE_TAKE_PHOTO = 332;
    private final String TAG = GiftFragment.class.getName();
    private GiftContract.Presenter mPresenter;
    private int totalRotaion;
    private CustomerModel mCustomer;
    private boolean isClickDialog = true;
    private boolean isClickCamera = false;
    private ImageOrderAdapter adapter;
    private LinkedHashMap<Integer, Integer> productIdNumberList = new LinkedHashMap<>();
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    private boolean isDetach = false;
    @BindView(R.id.rv_image)
    RecyclerView rvImage;

    @BindView(R.id.et_order_id)
    EditText etOrderId;
    @BindView(R.id.iv_order_id)
    ImageView ivOrderId;
    @BindView(R.id.et_customer_id)
    EditText etCustomerId;

    @BindView(R.id.et_customer_name)
    EditText etCustomerName;
    @BindView(R.id.iv_customer_name)
    ImageView ivCustomerName;
    @BindView(R.id.radioGroupSex)
    RadioGroup radioGroupSex;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.iv_sex)
    ImageView ivSex;

    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.et_note)
    EditText etNote;

    @BindView(R.id.iv_note)
    ImageView ivNote;

    @BindView(R.id.iv_content)
    ImageView ivContent;
    @BindView(R.id.iv_customer_code)
    ImageView ivCustomerCode;

    @BindView(R.id.iv_customer_email)
    ImageView ivCustomerEmail;

    @BindView(R.id.et_year_of_birth)
    EditText etYearOfBirth;
    @BindView(R.id.iv_customer_phone)
    ImageView ivCustomerPhone;

    @BindView(R.id.iv_customer_year_of_birth)
    ImageView ivCustomerYearOfBirth;

    @BindView(R.id.sp_reason)
    Spinner spReason;
    @BindView(R.id.iv_reason_buy)
    ImageView ivReasonBuy;
    private int customerId = -1;
    private LinkedHashMap<Integer, Integer> brandRotationList = new LinkedHashMap<>();
    private LinkedHashMap<ProductModel, Integer> productChooseNumberList = new LinkedHashMap<>();
    private LinkedHashMap<Integer, Integer> productNotChangeList = new LinkedHashMap<>();
    private LinkedHashMap<ProductModel, Integer> allNumberProductList = new LinkedHashMap<>();
    private String mCurrentPhotoPath;
    private List<EditText> listEdittext = new ArrayList<>();
    private boolean isGetInfo;
    private String sex, reasonBuy;

    public GiftFragment() {
        // Required empty public constructor
    }


    public static GiftFragment newInstance() {
        GiftFragment fragment = new GiftFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
                if (mCurrentPhotoPath != null) {

                    File file = new File(mCurrentPhotoPath);
                    if (file.exists()) {
                        adapter.addImage(mCurrentPhotoPath);
                    } else {
                        showError(getString(R.string.text_capture_image_error));
                    }

                }
            } else if (requestCode == 137) {
                initView();
                showSuccess(getString(R.string.text_save_success));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gift, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public void initView() {
        etCustomerId.setText("");
        etEmail.setText("");
        etOrderId.setText("");
        etPhone.setText("");
        etCustomerName.setText("");
        etNote.setText("");
        etYearOfBirth.setText("");
        productChooseNumberList.clear();
        totalRotaion = 0;
        allNumberProductList.clear();
        productIdNumberList.clear();
        customerId = -1;
        isClickDialog = true;
        brandRotationList.clear();
        mCurrentPhotoPath = null;
        ivCustomerPhone.setVisibility(View.GONE);
        ivCustomerCode.setVisibility(View.GONE);
        ivCustomerEmail.setVisibility(View.GONE);
        ivCustomerName.setVisibility(View.GONE);
        ivSex.setVisibility(View.GONE);
        ivCustomerYearOfBirth.setVisibility(View.GONE);
        ivReasonBuy.setVisibility(View.GONE);
        ivNote.setVisibility(View.GONE);
        ivContent.setVisibility(View.GONE);
        ivOrderId.setVisibility(View.GONE);
        mPresenter.getListProductChangeGift();
        adapter = new ImageOrderAdapter(new ArrayList<>(), getContext());
        rvImage.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        rvImage.setAdapter(adapter);

        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    etPhone.addTextChangedListener(textWatcherPhone);
                } else {
                    etPhone.removeTextChangedListener(textWatcherPhone);
                }
            }
        });


        etOrderId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    etOrderId.addTextChangedListener(textWatcherOrderId);
                } else {
                    etOrderId.removeTextChangedListener(textWatcherOrderId);
                }
            }
        });

        ArrayAdapter<String> reasonAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.reason_buy));
        reasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spReason.setAdapter(reasonAdapter);
        spReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 2) {
                    reasonBuy = "Mua để uống và tặng";
                } else {
                    reasonBuy = spReason.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        radioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_male:
                        sex = "Nam";
                        break;

                    case R.id.rb_female:
                        sex = "Nữ";
                        break;
                }
            }
        });
        sex = "Nam";

    }

    TextWatcher textWatcherPhone = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!TextUtils.isEmpty(s.toString())) {
                mPresenter.checkCustomerPhone(s.toString());
                if (!TextUtils.isEmpty(etOrderId.getText().toString())) {
                    mPresenter.getInfoCusCrash(etOrderId.getText().toString(), etPhone.getText().toString(), customerId);
                }
            }

        }
    };

    TextWatcher textWatcherOrderId = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s.toString())) {
                mPresenter.checkOrderCode(s.toString());

                if (!TextUtils.isEmpty(etPhone.getText().toString())) {
                    mPresenter.getInfoCusCrash(etOrderId.getText().toString(), etPhone.getText().toString(), customerId);
                }
            }

        }
    };


    @Override
    public void setPresenter(GiftContract.Presenter presenter) {
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
        if (!isClickCamera && isDetach) {
            initView();
            isDetach = false;
        } else {
            isClickCamera = false;
        }
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
    public void showListProductChangeGift(List<ProductModel> list) {
        llContent.removeAllViews();
        listEdittext.clear();
        for (ProductModel productModel : list) {
            setLayoutNumber(productModel);
        }
    }


    @Override
    public void showCustomerCode(LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list) {
        if (list.size() > 0) {
            ivOrderId.setVisibility(View.VISIBLE);
            if (ivCustomerPhone.getVisibility() != View.VISIBLE) {
                ivCustomerPhone.setVisibility(View.INVISIBLE);
            }
            ivCustomerEmail.setVisibility(View.INVISIBLE);
            ivCustomerName.setVisibility(View.INVISIBLE);
            ivNote.setVisibility(View.INVISIBLE);
            ivSex.setVisibility(View.INVISIBLE);
            ivCustomerYearOfBirth.setVisibility(View.INVISIBLE);
            ivReasonBuy.setVisibility(View.INVISIBLE);
            ivContent.setVisibility(View.INVISIBLE);
            ivCustomerCode.setVisibility(View.INVISIBLE);
            ivOrderId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailGiftCustomerDialog dialog = new DetailGiftCustomerDialog();
                    dialog.show(getActivity().getFragmentManager(), TAG);
                    dialog.setList(list);
                }
            });
        } else {
            if (ivCustomerPhone.getVisibility() != View.VISIBLE) {
                ivOrderId.setVisibility(View.GONE);
                ivCustomerCode.setVisibility(View.GONE);
                ivCustomerEmail.setVisibility(View.GONE);
                ivCustomerName.setVisibility(View.GONE);
                ivSex.setVisibility(View.GONE);
                ivCustomerYearOfBirth.setVisibility(View.GONE);
                ivReasonBuy.setVisibility(View.GONE);
                ivNote.setVisibility(View.GONE);
                ivContent.setVisibility(View.GONE);
                ivCustomerPhone.setVisibility(View.GONE);
            } else {
                ivOrderId.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void showCustomerPhone(LinkedHashMap<CustomerModel, List<CustomerGiftModel>> list, LinkedHashMap<BrandModel, Integer> brandModelIntegerLinkedHashMap) {
        if (list.size() > 0 && brandModelIntegerLinkedHashMap.size() > 0) {
            ivCustomerPhone.setImageResource(R.drawable.ic_warning);
            ivCustomerPhone.setVisibility(View.VISIBLE);
            if (ivOrderId.getVisibility() != View.VISIBLE) {
                ivOrderId.setVisibility(View.INVISIBLE);
            }
            ivCustomerEmail.setVisibility(View.INVISIBLE);
            ivCustomerName.setVisibility(View.INVISIBLE);
            ivNote.setVisibility(View.INVISIBLE);
            ivSex.setVisibility(View.INVISIBLE);
            ivCustomerYearOfBirth.setVisibility(View.INVISIBLE);
            ivReasonBuy.setVisibility(View.INVISIBLE);
            ivContent.setVisibility(View.INVISIBLE);
            ivCustomerCode.setVisibility(View.INVISIBLE);
            String content = "";
            for (Map.Entry<BrandModel, Integer> map : brandModelIntegerLinkedHashMap.entrySet()) {
                if (map.getValue() >= map.getKey().getNumberGiftOfDay()) {
                    ivCustomerPhone.setImageResource(R.drawable.ic_warning_red);
                }
                content += "\n" + "Bạn đã đổi " + map.getValue() + " quà " + map.getKey().getBrandName();
            }
            final String contentDialog = content.trim();
            ivCustomerPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DetailGiftCustomerDialog dialog = new DetailGiftCustomerDialog();
                    dialog.show(getActivity().getFragmentManager(), TAG);
                    dialog.setList(list);
                    dialog.setContent(contentDialog);
                }
            });
        } else {
            if (ivOrderId.getVisibility() != View.VISIBLE) {
                ivCustomerPhone.setVisibility(View.GONE);
                ivCustomerCode.setVisibility(View.GONE);
                ivCustomerEmail.setVisibility(View.GONE);
                ivCustomerName.setVisibility(View.GONE);
                ivNote.setVisibility(View.GONE);
                ivContent.setVisibility(View.GONE);
                ivSex.setVisibility(View.GONE);
                ivCustomerYearOfBirth.setVisibility(View.GONE);
                ivReasonBuy.setVisibility(View.GONE);
                ivOrderId.setVisibility(View.GONE);
            } else {
                ivCustomerPhone.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void showInfoCus(CustomerModel customerModel, List<CustomerProductModel> customerProductModelProductModelLinkedHashMap, List<ImageModel> customerImageModelImageModelLinkedHashMap) {
        mCustomer = customerModel;
        adapter.setEnableDelete(false);
        customerId = customerModel.getId();
        etCustomerName.setText(customerModel.getCustomerName());
        etNote.setText(customerModel.getNote());
        sex = customerModel.getSex();
        if (customerModel.getSex().equals("Nam")) {
            radioGroupSex.check(R.id.rb_male);
        } else {
            radioGroupSex.check(R.id.rb_female);
        }

        etEmail.setText(customerModel.getEmail());
        reasonBuy = customerModel.getReasonBuy();
        if (reasonBuy.equals("Mua để uống")) {
            spReason.setSelection(0);
        } else if (reasonBuy.equals("Mua để tặng")) {
            spReason.setSelection(1);
        } else {
            spReason.setSelection(2);
        }
        if (customerModel.getYearOfBirth() > 0)
            etYearOfBirth.setText(String.valueOf(customerModel.getYearOfBirth()));
        List<String> imageList = new ArrayList<>();
        for (ImageModel imageModel : customerImageModelImageModelLinkedHashMap) {
            imageList.add(imageModel.getPath());
        }
        adapter.addImageList(imageList);
        for (CustomerProductModel customerProductModel : customerProductModelProductModelLinkedHashMap) {
            for (EditText editText : listEdittext) {
                editText.setEnabled(false);
                ProductModel productModel = (ProductModel) editText.getTag();
                if (productModel.getId() == customerProductModel.getProductID()) {

                    editText.setText(String.valueOf(customerProductModel.getNumber()));
                }
            }
        }
    }


    @Override
    public void showNameAndCusCode(CustomerModel customerModel) {
        etCustomerId.setText(customerModel.getCustomerCode());
        etCustomerName.setText(customerModel.getCustomerName());
        sex = customerModel.getSex();
        if (customerModel.getSex().equals("Nam")) {
            radioGroupSex.check(R.id.rb_male);
        } else {
            radioGroupSex.check(R.id.rb_female);
        }

        etEmail.setText(customerModel.getEmail());
        reasonBuy = customerModel.getReasonBuy();
        if (reasonBuy.equals("Mua để uống")) {
            spReason.setSelection(0);
        } else if (reasonBuy.equals("Mua để tặng")) {
            spReason.setSelection(1);
        } else {
            spReason.setSelection(2);
        }
        if (customerModel.getYearOfBirth() > 0)
            etYearOfBirth.setText(String.valueOf(customerModel.getYearOfBirth()));
    }

    @Override
    public void goToRotationMega(int customerId) {
        RotationMegaActivity.start(getActivity(), customerId, true);
    }

    @Override
    public void goToRotationSP(int customerId) {
        RotationActivity.start(getActivity(), customerId);
    }

    @Override
    public void resetInfoCutomerWhenInput() {
        customerId = -1;
        for (EditText editText : listEdittext) {
            editText.setEnabled(true);
            editText.setText("");
        }
        etCustomerName.setText("");
        etNote.setText("");
        adapter = new ImageOrderAdapter(new ArrayList<>(), getContext());
        rvImage.setAdapter(adapter);
    }


    private void setLayoutNumber(ProductModel productModel) {
        LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inf.inflate(R.layout.item_product_gift, null);
        EditText etNumber = v.findViewById(R.id.et_number);
        TextView tvTitle = v.findViewById(R.id.tv_title);
        etNumber.setText("");
        etNumber.setTag(productModel);
        listEdittext.add(etNumber);
        etNumber.setHint(String.format(getString(R.string.text_number_product), productModel.getProductName()));
        tvTitle.setText(String.format(getString(R.string.text_number_product_short), productModel.getProductName()));
        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ProductModel productModel1 = (ProductModel) etNumber.getTag();
                BrandModel brandModel = mPresenter.getBrandModelByBrandId(productModel1.getBrandID());
                int number = 0;
                try {
                    number = Integer.parseInt(s.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (productModel1.isChangeGift()) {
                    if (brandModel.isDialLucky()) {
                        List<ProductModel> productModelList = new ArrayList<>();
                        productModelList.addAll(brandModel.getProductList());
                        int totalProduct = number;
                        for (ProductModel productModel2 : productModelList) {
                            if (productModel2.getId() != productModel1.getId()) {
                                if (productIdNumberList.get(productModel2.getId()) != null) {
                                    totalProduct += productIdNumberList.get(productModel2.getId());
                                }
                            }
                        }
                        if (totalProduct >= brandModel.getNumberOfEnough()) {
                            if (brandModel.getMaximumChangeGift() > -1) {
                                brandRotationList.put(productModel1.getBrandID(), Math.min(totalProduct / brandModel.getNumberOfEnough(), brandModel.getMaximumChangeGift()));
                            } else {
                                brandRotationList.put(productModel1.getBrandID(), totalProduct / brandModel.getNumberOfEnough());
                            }

                        } else {
                            brandRotationList.remove(productModel1.getBrandID());
                        }
                    } else {
                        if (number >= productModel1.getNumberOfEnough()) {
                            if (brandModel.getMaximumChangeGift() == -1) {

                                productChooseNumberList.put(productModel1, number / productModel1.getNumberOfEnough());
                            } else {
                                productChooseNumberList.put(productModel1, Math.min(number / productModel1.getNumberOfEnough(), brandModel.getMaximumChangeGift()));
                            }
                        } else {
                            productChooseNumberList.remove(productModel1);
                        }
                    }
                    if (number == 0) {
                        if (allNumberProductList.get(productModel1) != null) {
                            //numberProductList.remove(productModel1.getId());
                            allNumberProductList.remove(productModel1);
                            productIdNumberList.remove(productModel1.getId());
                        }
                    } else {
                        //numberProductList.put(productModel1.getId(), number);
                        allNumberProductList.put(productModel1, number);
                        productIdNumberList.put(productModel1.getId(), number);
                    }

                } else {
                    if (number == 0) {
                        if (allNumberProductList.get(productModel1) != null) {
                            allNumberProductList.remove(productModel1);
                            productIdNumberList.remove(productModel1.getId());
                        }
                    } else {
                        allNumberProductList.put(productModel1, number);
                        productIdNumberList.put(productModel1.getId(), number);
                    }
                }
            }
        });
        llContent.addView(v);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        UserEntity user = UserManager.getInstance().getUser();
        String imageFileName = MainViewType.getTypeFromInt(2).name() + "_" + user.getTeamOutletId() + "_"
                + ConvertUtils.getCodeGenerationByTime();
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @OnClick(R.id.iv_capture)
    public void capture() {
        if (customerId != -1) {
            return;
        }
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePicture.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.demo.sp19.fileprovider",
                        photoFile);
                isClickCamera = true;
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePicture, REQUEST_CODE_TAKE_PHOTO);
            }
        }
    }

    @OnClick(R.id.tv_next)
    public void next() {
        if (TextUtils.isEmpty(etOrderId.getText().toString())) {
            showError(getString(R.string.text_order_code_null));
            return;
        }

        if (TextUtils.isEmpty(etCustomerName.getText().toString())) {
            showError(getString(R.string.text_customer_name_null));
            return;
        }
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            showError(getString(R.string.text_customer_phone_null));
            return;
        }

        if (etPhone.getText().toString().trim().length() != 10) {
            showError(getString(R.string.text_lenght_phone_incorrect));
            return;
        }
        if (adapter.getList().size() == 0) {
            showError(getString(R.string.text_image_bill_null));
            return;
        }

        if (brandRotationList.size() == 0 && productChooseNumberList.size() == 0 && productNotChangeList.size() == 0) {
            showError(getString(R.string.text_unqualified_change_gift));
            return;
        }

        for (Map.Entry<Integer, Integer> map : brandRotationList.entrySet()) {
            totalRotaion += map.getValue();
        }

        for (Map.Entry<ProductModel, Integer> map : productChooseNumberList.entrySet()) {
            totalRotaion += map.getValue();
        }
        if (totalRotaion == 0) {
            showError(getString(R.string.text_unqualified_change_gift));
            return;
        }

        if (!TextUtils.isEmpty(etYearOfBirth.getText().toString())){
            int year = Integer.parseInt(etYearOfBirth.getText().toString());
            int yearCurrent = Calendar.getInstance().get(Calendar.YEAR);
            if (yearCurrent - year < 18){
                showError("Tuổi dưới 18 , không hợp lệ");
                return;
            }
        }
        if (customerId == -1) {
            if (isClickDialog) {
                isClickDialog = false;
                ConfirmProductDialog dialog = new ConfirmProductDialog();
                dialog.show(getActivity().getFragmentManager(), TAG);
                dialog.setProductList(allNumberProductList);
                dialog.setListener(new ConfirmProductDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        mPresenter.saveInfoCustomer(etOrderId.getText().toString(), etCustomerId.getText().toString(),
                                etCustomerName.getText().toString(), etPhone.getText().toString(),
                                sex, etEmail.getText().toString(), Integer.parseInt(etYearOfBirth.getText().toString().length() > 0 ? etYearOfBirth.getText().toString() : "0"), reasonBuy, etNote.getText().toString(), adapter.getList(),
                                allNumberProductList, brandRotationList, productChooseNumberList, totalRotaion);

                    }
                }, new ConfirmProductDialog.OnCancleListener() {
                    @Override
                    public void onCancle() {
                        isClickDialog = true;
                    }
                });
            }

        } else {
            mPresenter.checkCondition(mCustomer);

        }
    }

    @Override
    public void onDetach() {
        isDetach = false;
        super.onDetach();
        Log.d(TAG, "Detach gift fragment");
    }

    @Override
    public void onAttach(Context context) {
        isDetach = true;
        super.onAttach(context);
        Log.d(TAG, "Attach gift fragment");
    }
}
