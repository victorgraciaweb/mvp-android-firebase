package com.ontoast.inventario.mainModule.model.dataAccess;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.ontoast.inventario.R;
import com.ontoast.inventario.common.BasicErrorEventCallback;
import com.ontoast.inventario.common.model.dataAccess.FirebaseRealtimeDatabaseAPI;
import com.ontoast.inventario.common.pojo.Product;
import com.ontoast.inventario.mainModule.events.MainEvent;

public class RealtimeDatabase {

    private FirebaseRealtimeDatabaseAPI mDatabaseAPI;
    private ChildEventListener mProductsChildEventListener;

    public RealtimeDatabase() {
        mDatabaseAPI = FirebaseRealtimeDatabaseAPI.getInstance();
    }

    public void subscribeToProducts(final ProductsEventListener listener){
        if (mProductsChildEventListener == null){
            mProductsChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listener.onChildAdded(getProduct(dataSnapshot));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    listener.onChildUpdated(getProduct(dataSnapshot));
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    listener.onChildRemoved(getProduct(dataSnapshot));
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    switch (databaseError.getCode()){
                        case DatabaseError.PERMISSION_DENIED:
                            listener.onError(R.string.main_error_permission_denied);
                            break;
                        default:
                            listener.onError(R.string.main_error_server);
                    }
                }
            };
        }
        mDatabaseAPI.getProductsReference().addChildEventListener(mProductsChildEventListener);
    }

    private Product getProduct(DataSnapshot dataSnapshot) {
        Product product = dataSnapshot.getValue(Product.class);
        if (product != null){
            product.setId(dataSnapshot.getKey());
        }
        return product;
    }

    public void unsubscribeToProducts(){
        if (mProductsChildEventListener != null){
            mDatabaseAPI.getProductsReference().removeEventListener(mProductsChildEventListener);
        }
    }

    public void removeProduct(Product product, final BasicErrorEventCallback callback){
        mDatabaseAPI.getProductsReference().child(product.getId())
                .removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null){
                            callback.onSuccess();
                        } else {
                            switch (databaseError.getCode()){
                                case DatabaseError.PERMISSION_DENIED:
                                    callback.onError(MainEvent.ERROR_TO_REMOVE, R.string.main_error_remove);
                                    break;
                                default:
                                    callback.onError(MainEvent.ERROR_SERVER, R.string.main_error_server);
                            }
                        }
                    }
                });
    }
}
