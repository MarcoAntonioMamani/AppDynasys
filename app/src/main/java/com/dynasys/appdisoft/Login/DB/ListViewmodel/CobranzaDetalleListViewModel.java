package com.dynasys.appdisoft.Login.DB.ListViewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dynasys.appdisoft.Login.DB.Entity.CobranzaDetalleEntity;
import com.dynasys.appdisoft.Login.DB.Repository.CobranzaDetalleRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CobranzaDetalleListViewModel  extends AndroidViewModel {


    private CobranzaDetalleRepository mRepository;
    private LiveData<List<CobranzaDetalleEntity>> users;
    private List<CobranzaDetalleEntity> CobranzaDetalleList;
    public CobranzaDetalleListViewModel(@NonNull Application application) {
        super(application);

        mRepository = new CobranzaDetalleRepository(application);
    }

    public LiveData<List<CobranzaDetalleEntity>> getCobranzaDetalle() {
        if (users == null) {
            users = mRepository.getAllCobranza();
        }

        return users;
    }


    public List<CobranzaDetalleEntity> getMCobranzaDetalleAllAsync() {
        try {
            CobranzaDetalleList = mRepository.getMCobranzaDetalleAll(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return CobranzaDetalleList;
    }
    public List<CobranzaDetalleEntity> getCobranzaDetalle(String id) throws ExecutionException, InterruptedException {

        return mRepository.getCobranzaDetalle(id);

    }

    public void insertCobranzaDetalle(CobranzaDetalleEntity user) {
        mRepository.insertCobranzaDetalle(user);
    }



    public void updateCobranzaDetalle(CobranzaDetalleEntity user) {
        mRepository.updateCobranzaDetalle(user);
    }

    public void deleteCobranzaDetalle(CobranzaDetalleEntity user) {
        mRepository.deleteCobranzaDetalle(user);
    }

    public void deleteAllCobranzaDetalles() {
        mRepository.deleteAllCobranzaDetalle();
    }
}
