package com.dynasys.appdisoft.Login.DB.ListViewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dynasys.appdisoft.Login.DB.Entity.VisitaEntity;
import com.dynasys.appdisoft.Login.DB.Repository.VisitaRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class VisitaListViewModel extends AndroidViewModel {

    private VisitaRepository mRepository;
    private LiveData<List<VisitaEntity>> users;
    private List<VisitaEntity> VisitaList;
    public VisitaListViewModel(@NonNull Application application) {
        super(application);

        mRepository = new VisitaRepository(application);
    }

    public LiveData<List<VisitaEntity>> getVisita() {
        if (users == null) {
            users = mRepository.getAllVisita();
        }

        return users;
    }


    public List<VisitaEntity> getMVisitaAllAsync() {
        try {
            VisitaList = mRepository.getMVisitaAll(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return VisitaList;
    }
    public VisitaEntity getVisita(int id) throws ExecutionException, InterruptedException {

        return mRepository.getVisita(id);

    }

    public void insertVisita(VisitaEntity user) {
        mRepository.insertVisita(user);
    }



    public void updateVisita(VisitaEntity user) {
        mRepository.updateVisita(user);
    }
    public void insertListVisita(List<VisitaEntity> st){
        mRepository.InsertVisitaList(st);
    }
    public void deleteVisita(VisitaEntity user) {
        mRepository.deleteVisita(user);
    }

    public void deleteAllVisitas() {
        mRepository.deleteAllVisita();
    }
}
