package com.ontoast.inventario.mainModule.view.adapters;

import com.ontoast.inventario.common.pojo.Product;

public interface OnItemClickListener {
    void onItemClick(Product product);
    void onLongItemClick(Product product);
}
