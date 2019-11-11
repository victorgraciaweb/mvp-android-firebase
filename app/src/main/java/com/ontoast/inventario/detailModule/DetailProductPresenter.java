package com.ontoast.inventario.detailModule;

import com.ontoast.inventario.common.pojo.Product;
import com.ontoast.inventario.detailModule.events.DetailProductEvent;

public interface DetailProductPresenter {
    void onCreate();
    void onDestroy();

    void updateProduct(Product product);

    void onEventListenr(DetailProductEvent event);
}
