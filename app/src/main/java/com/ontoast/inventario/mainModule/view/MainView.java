package com.ontoast.inventario.mainModule.view;

import com.ontoast.inventario.common.pojo.Product;

public interface MainView {
    void showProgress();
    void hideProgress();

    void add(Product product);
    void update(Product product);
    void remove(Product product);

    void removeFail();
    void onShowError(int resMsg);
}
