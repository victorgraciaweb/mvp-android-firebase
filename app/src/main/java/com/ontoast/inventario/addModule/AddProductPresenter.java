package com.ontoast.inventario.addModule;

import com.ontoast.inventario.addModule.events.AddProductEvent;
import com.ontoast.inventario.common.pojo.Product;

public interface AddProductPresenter {
    void onShow();
    void onDestroy();

    void addProduct(Product product);

    void onEventListener(AddProductEvent event);
}
