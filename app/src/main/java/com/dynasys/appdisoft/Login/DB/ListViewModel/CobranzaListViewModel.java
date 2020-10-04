package com.dynasys.appdisoft.Login.DB.ListViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dynasys.appdisoft.Login.DB.Entity.CobranzaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.CobranzaEntity;
import com.dynasys.appdisoft.Login.DB.Repository.CobranzaRepository;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class CobranzaListViewModel extends AndroidViewModel {

    private CobranzaRepository mRepository;
    private LiveData<List<CobranzaEntity>> users;
    private List<CobranzaEntity> cobranzaList;
    public CobranzaListViewModel(@NonNull Application application) {
        super(application);

        mRepository = new CobranzaRepository(application);
    }

    public LiveData<List<CobranzaEntity>> getCobranza() {
        if (users == null) {
            users = mRepository.getAllCobranza();
        }

        return users;
    }


    public List<CobranzaEntity> getMCobranzaAllAsync() {
        try {
            cobranzaList = mRepository.getMCobranzaAll(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return cobranzaList;
    }


    public List<CobranzaEntity> getMCobranzaNoSincronizadas() {
        try {
            cobranzaList = mRepository.getMCobranzaAllNoSincronizadas(1);
        } catch (ExecutionException e) {
            return null;
        } catch (InterruptedException e) {
           return null;
        }


        return cobranzaList;
    }
    public CobranzaEntity getCobranza(String id) throws ExecutionException, InterruptedException {

        return mRepository.getCobranza(id);

    }

    public void insertCobranza(CobranzaEntity user) {
        mRepository.insertCobranza(user);
    }



    public void updateCobranza(CobranzaEntity user) {
        mRepository.updateCobranza(user);
    }

    public void deleteCobranza(CobranzaEntity user) {
        mRepository.deleteCobranza(user);
    }

    public void deleteAllCobranzas() {
        mRepository.deleteAllCobranza();
    }
}
