package com.ontoast.inventario.detailModule.model.dataAccess;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ontoast.inventario.common.BasicEventCallback;
import com.ontoast.inventario.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.ontoast.inventario.common.pojo.Product;

public class RealtimeDatabase {

    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;

    public RealtimeDatabase() {
        mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    public void updateProduct(Product product, final BasicEventCallback callback){
        mDatabaseAPI.getProductsReference().child(product.getId()).setValue(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError();
                    }
                });
    }
}
