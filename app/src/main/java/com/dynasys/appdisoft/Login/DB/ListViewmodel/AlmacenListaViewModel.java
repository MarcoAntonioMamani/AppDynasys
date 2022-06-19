package com.dynasys.appdisoft.Login.DB.ListViewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dynasys.appdisoft.Login.DB.Entity.AlmacenEntity;
import com.dynasys.appdisoft.Login.DB.Repository.AlmacenRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AlmacenListaViewModel extends AndroidViewModel {

    private AlmacenRepository mRepository;
    
    private LiveData<List<AlmacenEntity>> users;
    private List<AlmacenEntity> AlmacenList;
    public AlmacenListaViewModel(@NonNull Application application) {
        super(application);

        mRepository = new AlmacenRepository(application);
    }

    public LiveData<List<AlmacenEntity>> getAlmacen() {
        if (users == null) {
            users = mRepository.getAllAlmacen();
        }

        return users;
    }


    public List<AlmacenEntity> getMAlmacenAllAsync() {
        try {
            AlmacenList = mRepository.getMAlmacenAll(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return AlmacenList;
    }
    public AlmacenEntity getAlmacen(int id) throws ExecutionException, InterruptedException {

        return mRepository.getAlmacen(id);

    }

    public void insertAlmacen(AlmacenEntity user) {
        mRepository.insertAlmacen(user);
    }



    public void updateAlmacen(AlmacenEntity user) {
        mRepository.updateAlmacen(user);
    }

    public void deleteAlmacen(AlmacenEntity user) {
        mRepository.deleteAlmacen(user);
    }

    public void deleteAllAlmacens() {
        mRepository.deleteAllAlmacen();
    }
}
