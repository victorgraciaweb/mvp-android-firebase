package com.ontoast.inventario.detailModule.model;

import com.ontoast.inventario.common.BasicEventCallback;
import com.ontoast.inventario.common.pojo.Product;
import com.ontoast.inventario.detailModule.events.DetailProductEvent;
import com.ontoast.inventario.detailModule.model.dataAccess.RealtimeDatabase;

import org.greenrobot.eventbus.EventBus;

public class DetailProductInteractorClass implements DetailProductInteractor {
    private RealtimeDatabase mDatabase;

    public DetailProductInteractorClass() {
        mDatabase = new RealtimeDatabase();
    }

    @Override
    public void updateProduct(Product product) {
        mDatabase.updateProduct(product, new BasicEventCallback() {
            @Override
            public void onSuccess() {
                post(DetailProductEvent.UPDATE_SUCCESS);
            }

            @Override
            public void onError() {
                post(DetailProductEvent.ERROR_SERVER);
            }
        });
    }

    private void post(int typeEvent) {
        DetailProductEvent event = new DetailProductEvent();
        event.setTypeEvent(typeEvent);
        EventBus.getDefault().post(event);
    }
}
