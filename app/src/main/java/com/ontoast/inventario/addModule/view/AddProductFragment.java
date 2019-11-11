package com.ontoast.inventario.addModule.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.ontoast.inventario.R;
import com.ontoast.inventario.addModule.AddProductPresenter;
import com.ontoast.inventario.addModule.AddProductPresenterClass;
import com.ontoast.inventario.common.pojo.Product;
import com.ontoast.inventario.common.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddProductFragment extends DialogFragment implements DialogInterface.OnShowListener, AddProductView {

    @BindView(R.id.etPhotoUlr)
    EditText etPhotoUlr;
    @BindView(R.id.imgPhoto)
    AppCompatImageView imgPhoto;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etQuantity)
    EditText etQuantity;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.contentMain)
    ConstraintLayout contentMain;

    Unbinder unbinder;

    private AddProductPresenter mPresenter;

    public AddProductFragment() {
        mPresenter = new AddProductPresenterClass(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.addProduct_title)
                .setPositiveButton(R.string.addProduct_dialog_ok, null)
                .setNegativeButton(R.string.common_dialog_cancel, null);

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_add_product, null);
        unbinder = ButterKnife.bind(this, view);
        builder.setView(view);

        configFocus();
        configEditText();

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);
        return dialog;
    }

    private void configFocus() {
        etName.requestFocus();
    }

    private void configEditText() {
        etPhotoUlr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                final String photoUrl = etPhotoUlr.getText().toString().trim();
                if (photoUrl.isEmpty()){
                    imgPhoto.setImageDrawable(null);
                } else {
                    if (getActivity() != null){
                        RequestOptions options = new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop();

                        Glide.with(getActivity())
                                .load(photoUrl)
                                .apply(options)
                                .into(imgPhoto);
                    }
                }
            }
        });
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        final AlertDialog dialog = (AlertDialog)getDialog();
        if (dialog != null){
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (CommonUtils.validateProduct(getActivity(), etName, etQuantity, etPhotoUlr)) {
                        Product product = new Product();
                        product.setName(etName.getText().toString().trim());
                        product.setQuantity(Integer.valueOf(etQuantity.getText().toString().trim()));
                        product.setPhotoUrl(etPhotoUlr.getText().toString().trim());
                        mPresenter.addProduct(product);
                    }
                }
            });

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
        mPresenter.onShow();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mPresenter.onDestroy();
    }

    @Override
    public void enableUIElements() {
        etName.setEnabled(true);
        etQuantity.setEnabled(true);
        etPhotoUlr.setEnabled(true);
    }

    @Override
    public void disableUIElements() {
        etName.setEnabled(false);
        etQuantity.setEnabled(false);
        etPhotoUlr.setEnabled(false);
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
    public void productAdded() {
        Toast.makeText(getActivity(), R.string.addProduct_message_added_successfully, Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    public void showError(int resMsg) {
        Snackbar.make(contentMain, resMsg, Snackbar.LENGTH_INDEFINITE).setAction(
                R.string.addProduct_snackbar_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
    }

    @Override
    public void maxValueError(int resMsg) {
        etQuantity.setError(getString(resMsg));
        etQuantity.requestFocus();
    }
}
