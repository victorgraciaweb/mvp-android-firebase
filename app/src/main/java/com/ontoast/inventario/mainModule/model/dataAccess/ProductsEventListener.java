package com.ontoast.inventario.mainModule.model.dataAccess;

import com.ontoast.inventario.common.pojo.Product;

public interface ProductsEventListener {
    void onChildAdded(Product product);
    void onChildUpdated(Product product);
    void onChildRemoved(Product product);

    void onError(int resMsg);
}
