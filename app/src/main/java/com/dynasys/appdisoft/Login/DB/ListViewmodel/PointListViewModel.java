package com.dynasys.appdisoft.Login.DB.ListViewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dynasys.appdisoft.Login.DB.Entity.PointEntity;
import com.dynasys.appdisoft.Login.DB.Repository.PointRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PointListViewModel extends AndroidViewModel {

    private PointRepository mRepository;
    private LiveData<List<PointEntity>> users;
    private List<PointEntity> PointList;
    public PointListViewModel(@NonNull Application application) {
        super(application);

        mRepository = new PointRepository(application);
    }

    public LiveData<List<PointEntity>> getPoint() {
        if (users == null) {
            users = mRepository.getAllPoint();
        }

        return users;
    }


    public List<PointEntity> getMPointAllAsync() {
        try {
            PointList = mRepository.getMPointAll(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return PointList;
    }
    public List<PointEntity> getPoint(int id) throws ExecutionException, InterruptedException {

        return mRepository.getPoint(id);

    }

    public void insertPoint(PointEntity user) {
        mRepository.insertPoint(user);
    }



    public void updatePoint(PointEntity user) {
        mRepository.updatePoint(user);
    }
    public void insertListPoint(List<PointEntity> st){
        mRepository.InsertPointList(st);
    }
    public void deletePoint(PointEntity user) {
        mRepository.deletePoint(user);
    }

    public void deleteAllPoints() {
        mRepository.deleteAllPoint();
    }
}
