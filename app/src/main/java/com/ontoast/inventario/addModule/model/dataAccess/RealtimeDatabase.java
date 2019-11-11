package com.ontoast.inventario.addModule.model.dataAccess;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.ontoast.inventario.R;
import com.ontoast.inventario.addModule.events.AddProductEvent;
import com.ontoast.inventario.common.BasicErrorEventCallback;
import com.ontoast.inventario.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.ontoast.inventario.common.pojo.Product;

public class RealtimeDatabase {

    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;

    public RealtimeDatabase() {
        mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    public void addProduct(Product product, final BasicErrorEventCallback callback){
        mDatabaseAPI.getProductsReference().push().setValue(product, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null){
                    callback.onSuccess();
                } else {
                    switch (databaseError.getCode()){
                        case DatabaseError.PERMISSION_DENIED:
                            callback.onError(AddProductEvent.ERROR_MAX_VALUE,
                                    R.string.addProduct_message_validate_max_quantity);
                            break;
                        default:
                            callback.onError(AddProductEvent.ERROR_SERVER,
                                    R.string.addProduct_message_added_error);
                    }
                }
            }
        });
    }
}
