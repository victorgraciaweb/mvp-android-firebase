package com.ontoast.inventario.detailModule.view;

import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.ontoast.inventario.R;
import com.ontoast.inventario.common.pojo.Product;
import com.ontoast.inventario.common.utils.CommonUtils;
import com.ontoast.inventario.detailModule.DetailProductPresenter;
import com.ontoast.inventario.detailModule.DetailProductPresenterClass;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetailFragment extends Fragment implements DetailProductView {

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etQuantity)
    EditText etQuantity;
    @BindView(R.id.etPhotoUlr)
    EditText etPhotoUlr;
    @BindView(R.id.imgPhoto)
    AppCompatImageView imgPhoto;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.contentMain)
    ConstraintLayout contentMain;
    Unbinder unbinder;

    private Product mProduct;
    private DetailProductPresenter mPresenter;

    public DetailFragment() {
        mPresenter = new DetailProductPresenterClass(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (getArguments() != null) {
            configProduct(getArguments());
            configValues();
            configEditText();
        }

        mPresenter.onCreate();

        return view;
    }

    private void configProduct(Bundle args) {
        mProduct = new Product();
        mProduct.setId(args.getString(Product.ID));
        mProduct.setName(args.getString(Product.NAME));
        mProduct.setQuantity(args.getInt(Product.QUANTITY));
        mProduct.setPhotoUrl(args.getString(Product.PHOTO_URL));
    }

    private void configValues() {
        etName.setText(mProduct.getName());
        etQuantity.setText(String.valueOf(mProduct.getQuantity()));
        etPhotoUlr.setText(mProduct.getPhotoUrl());

        configPhoto(mProduct.getPhotoUrl());
    }

    private void configPhoto(String photoUrl) {
        if (getActivity() != null) {
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop();

            Glide.with(getActivity())
                    .load(photoUrl)
                    .apply(options)
                    .into(imgPhoto);
        }
    }

    private void configEditText() {
        etPhotoUlr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String photoUrl = etPhotoUlr.getText().toString().trim();
                if (photoUrl.isEmpty()) {
                    imgPhoto.setImageDrawable(null);
                } else {
                    configPhoto(photoUrl);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mPresenter.onDestroy();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void enableUIElements() {
        etName.setEnabled(true);
        etQuantity.setEnabled(true);
        etPhotoUlr.setEnabled(true);
        btnSave.setEnabled(true);
    }

    @Override
    public void disableUIElements() {
        etName.setEnabled(false);
        etQuantity.setEnabled(false);
        etPhotoUlr.setEnabled(false);
        btnSave.setEnabled(false);
    }

    @Override
    public void showFab() {
        if (getActivity() != null) {
            getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideFab() {
        if (getActivity() != null) {
            getActivity().findViewById(R.id.fab).setVisibility(View.GONE);
        }
    }

    @Override
    public void updateSuccess() {
        Snackbar.make(contentMain, R.string.detailProduct_update_successfully, Snackbar.LENGTH_LONG)
                .setAction(R.string.detailProduct_snackbar_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getActivity() != null) {
                            getActivity().onBackPressed();
                        }
                    }
                })
                .show();
    }

    @Override
    public void updateError() {
        Snackbar.make(contentMain, R.string.detailProduct_update_error, Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.btnSave)
    public void onSaveClicked() {
        if (CommonUtils.validateProduct(getActivity(), etName, etQuantity, etPhotoUlr)) {
            mProduct.setName(etName.getText().toString().trim());
            mProduct.setQuantity(Integer.valueOf(etQuantity.getText().toString().trim()));
            mProduct.setPhotoUrl(etPhotoUlr.getText().toString().trim());
            mPresenter.updateProduct(mProduct);
        }
    }
}
