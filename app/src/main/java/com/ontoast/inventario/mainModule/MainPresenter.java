package com.ontoast.inventario.mainModule;

import com.ontoast.inventario.common.pojo.Product;
import com.ontoast.inventario.mainModule.events.MainEvent;

public interface MainPresenter {
    void onCreate();
    void onPause();
    void onResume();
    void onDestroy();

    void remove(Product product);

    void onEventListener(MainEvent event);
}
