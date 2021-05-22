package com.dynasys.appdisoft.Login.DB;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dynasys.appdisoft.Login.DB.Entity.CategoriaPrecioEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CategoriaPrecioListViewModel  extends AndroidViewModel {
    private CategoriaPrecioRepository mRepository;
    private LiveData<List<CategoriaPrecioEntity>> users;
    private List<CategoriaPrecioEntity> CategoriaPrecioList;
    public CategoriaPrecioListViewModel(@NonNull Application application) {
        super(application);

        mRepository = new CategoriaPrecioRepository(application);
    }

    public LiveData<List<CategoriaPrecioEntity>> getCategoriaPrecio() {
        if (users == null) {
            users = mRepository.getAllCategoriaPrecio();
        }

        return users;
    }


    public List<CategoriaPrecioEntity> getMCategoriaPrecioAllAsync() {
        try {
            CategoriaPrecioList = mRepository.getMCategoriaPrecioAll(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return CategoriaPrecioList;
    }
    public CategoriaPrecioEntity getCategoriaPrecio(int id) throws ExecutionException, InterruptedException {

        return mRepository.getCategoriaPrecio(id);

    }

    public void insertCategoriaPrecio(CategoriaPrecioEntity user) {
        mRepository.insertCategoriaPrecio(user);
    }



    public void updateCategoriaPrecio(CategoriaPrecioEntity user) {
        mRepository.updateCategoriaPrecio(user);
    }
    public void insertListCategoriaPrecio(List<CategoriaPrecioEntity> st){
        mRepository.InsertCategoriaPrecioList(st);
    }
    public void deleteCategoriaPrecio(CategoriaPrecioEntity user) {
        mRepository.deleteCategoriaPrecio(user);
    }

    public void deleteAllCategoriaPrecios() {
        mRepository.deleteAllCategoriaPrecio();
    }

}
