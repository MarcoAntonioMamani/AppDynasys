package com.dynasys.appdisoft.Login.DB.ListViewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dynasys.appdisoft.Login.DB.Entity.ProductoViewEntity;
import com.dynasys.appdisoft.Login.DB.Repository.ProductoViewRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProductoViewListViewModel  extends AndroidViewModel {

    private ProductoViewRepository mRepository;
    private LiveData<List<ProductoViewEntity>> users;
    private List<ProductoViewEntity> ProductoViewList;
    public ProductoViewListViewModel(@NonNull Application application) {
        super(application);

        mRepository = new ProductoViewRepository(application);
    }

    public LiveData<List<ProductoViewEntity>> getProductoView() {
        if (users == null) {
            users = mRepository.getAllProductoView();
        }

        return users;
    }


    public List<ProductoViewEntity> getMProductoViewAllAsync() {
        try {
            ProductoViewList = mRepository.getMProductoViewAll(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return ProductoViewList;
    }
    public ProductoViewEntity getProductoView(int id) throws ExecutionException, InterruptedException {

        return mRepository.getProductoView(id);

    }

    public void insertProductoView(ProductoViewEntity user) {
        mRepository.insertProductoView(user);
    }



    public void updateProductoView(ProductoViewEntity user) {
        mRepository.updateProductoView(user);
    }
    public void insertListProductoView(List<ProductoViewEntity> st){
        mRepository.InsertProductoViewList(st);
    }
    public void deleteProductoView(ProductoViewEntity user) {
        mRepository.deleteProductoView(user);
    }

    public void deleteAllProductoViews() {
        mRepository.deleteAllProductoView();
    }
}
