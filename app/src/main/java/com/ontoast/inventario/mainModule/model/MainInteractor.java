package com.ontoast.inventario.mainModule.model;

import com.ontoast.inventario.common.pojo.Product;

public interface MainInteractor {
    void subscribeToProducts();
    void unsubscribeToProducts();

    void removeProduct(Product product);
}
