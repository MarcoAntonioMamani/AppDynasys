package com.dynasys.appdisoft.Login.DB;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dynasys.appdisoft.Login.DB.Entity.DescuentosEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DescuentosListViewModel  extends AndroidViewModel {

    private DescuentoRepository mRepository;
    private LiveData<List<DescuentosEntity>> users;

    public DescuentosListViewModel(@NonNull Application application) {
        super(application);

        mRepository = new DescuentoRepository(application);
    }
    public List<DescuentosEntity> getMAllDescuentos(int code) throws ExecutionException, InterruptedException {
        return mRepository.getMDescuentoAll(code);
    }
    public List<DescuentosEntity> getDescuentosByProducto(int code) throws ExecutionException, InterruptedException {
        return mRepository.getMDescuentosbyIdProducto(code);
    }
    public LiveData<List<DescuentosEntity>> getDescuentoss() {
        if (users == null) {
            users = mRepository.getAllDescuentos();
        }

        return users;
    }

    public DescuentosEntity getDescuentos(int id) throws ExecutionException, InterruptedException {
        return mRepository.getDescuento(id);
    }

    public void insertDescuentos(DescuentosEntity user) {
        mRepository.insertDescuento(user);
    }

    public void updateDescuentos(DescuentosEntity user) {
        mRepository.updateDescuentos(user);
    }

    public void deleteDescuentos(DescuentosEntity user) {
        mRepository.deleteDescuentos(user);
    }

    public void deleteAllDescuentos() {
        mRepository.deleteAllDescuentos();
    }
}
