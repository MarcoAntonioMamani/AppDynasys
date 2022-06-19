package com.dynasys.appdisoft.Login.DB.ListViewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dynasys.appdisoft.Login.DB.Entity.DeudaEntity;
import com.dynasys.appdisoft.Login.DB.Repository.DeudaRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DeudaListaViewModel extends AndroidViewModel {

    private DeudaRepository mRepository;
    private LiveData<List<DeudaEntity>> users;
    private List<DeudaEntity> DeudaList;
    public DeudaListaViewModel(@NonNull Application application) {
        super(application);

        mRepository = new DeudaRepository(application);
    }

    public LiveData<List<DeudaEntity>> getDeuda() {
        if (users == null) {
            users = mRepository.getAllDeuda();
        }

        return users;
    }


    public List<DeudaEntity> getMDeudaAllAsync() {
        try {
            DeudaList = mRepository.getMDeudaAll(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return DeudaList;
    }
    public DeudaEntity getDeuda(int id) throws ExecutionException, InterruptedException {

        return mRepository.getDeuda(id);

    }

    public void insertDeuda(DeudaEntity user) {
        mRepository.insertDeuda(user);
    }



    public void updateDeuda(DeudaEntity user) {
        mRepository.updateDeuda(user);
    }

    public void deleteDeuda(DeudaEntity user) {
        mRepository.deleteDeuda(user);
    }

    public void deleteAllDeudas() {
        mRepository.deleteAllDeuda();
    }
}
