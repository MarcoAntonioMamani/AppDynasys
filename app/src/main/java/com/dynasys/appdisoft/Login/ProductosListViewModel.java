package com.dynasys.appdisoft.Login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dynasys.appdisoft.Login.DB.Entity.PrecioEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.UserEntity;
import com.dynasys.appdisoft.Login.DB.ProductoRepository;
import com.dynasys.appdisoft.Login.DB.UserRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProductosListViewModel extends AndroidViewModel {

    private ProductoRepository mRepository;
    private LiveData<List<ProductoEntity>> users;

    public ProductosListViewModel(@NonNull Application application) {
        super(application);

        mRepository = new ProductoRepository(application);
    }
    public List<ProductoEntity> getMAllProducto(int code) throws ExecutionException, InterruptedException {
        return mRepository.getMProductoAll(code);
    }
    public LiveData<List<ProductoEntity>> getProductos() {
        if (users == null) {
            users = mRepository.getAllProductos();
        }

        return users;
    }

    public ProductoEntity getProducto(int id) throws ExecutionException, InterruptedException {
        return mRepository.getProducto(id);
    }

    public void insertProducto(ProductoEntity user) {
        mRepository.insertProducto(user);
    }

    public void updateProducto(ProductoEntity user) {
        mRepository.updateProductos(user);
    }

    public void deleteProducto(ProductoEntity user) {
        mRepository.deleteProductos(user);
    }

    public void deleteAllProductos() {
        mRepository.deleteAllProductos();
    }
}
