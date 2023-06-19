package com.dynasys.appdisoft.Login.DB.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dynasys.appdisoft.Login.DB.AppDatabase;
import com.dynasys.appdisoft.Login.DB.Dao.DeudadDao;
import com.dynasys.appdisoft.Login.DB.Entity.DeudaEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DeudaRepository {

    private DeudadDao mDeudadDao;
    private LiveData<List<DeudaEntity>> mAllDeuda;

    public DeudaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mDeudadDao = db.DeudadDao();
        mAllDeuda = mDeudadDao.getAllDeuda();
    }

    public LiveData<List<DeudaEntity>> getAllDeuda() {
        return mAllDeuda;
    }

    public DeudaEntity getDeuda(int noteId) throws ExecutionException, InterruptedException {
        return new getDeudaAsync(mDeudadDao).execute(noteId).get();
    }
    public List<DeudaEntity> getMDeudaAll(int clienteId) throws ExecutionException, InterruptedException {
        return new getMDeudaAllAsync(mDeudadDao).execute(clienteId).get();
    }
    public void insertDeuda(DeudaEntity user) {
        new insertDeudaAsync(mDeudadDao).execute(user);
    }

    public void updateDeuda(DeudaEntity user) {
        new updateDeudaAsync(mDeudadDao).execute(user);
    }

    public void deleteDeuda(DeudaEntity user) {
        new deleteDeudaAsync(mDeudadDao).execute(user);
    }

    public void deleteAllDeuda() {
        new deleteAllDeudaAsync(mDeudadDao).execute();
    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */

    private static class getDeudaAsync extends AsyncTask<Integer, Void, DeudaEntity> {

        private DeudadDao mDeudadDaoAsync;

        getDeudaAsync(DeudadDao animalDao) {
            mDeudadDaoAsync = animalDao;
        }

        @Override
        protected DeudaEntity doInBackground(Integer... ids) {
            return mDeudadDaoAsync.getDeudaPedido(ids[0]);
        }
    }


    private static class getMDeudaAllAsync extends AsyncTask<Integer, Void, List<DeudaEntity>> {

        private DeudadDao mPedidoDaoAsync;

        getMDeudaAllAsync(DeudadDao clienteDao) {
            mPedidoDaoAsync = clienteDao;
        }

        @Override
        protected List<DeudaEntity> doInBackground(Integer... ids) {
            return mPedidoDaoAsync.getDeudaMAll();
        }
    }
    private static class insertDeudaAsync extends AsyncTask<DeudaEntity, Void, Long> {

        private DeudadDao mDeudadDaoAsync;

        insertDeudaAsync(DeudadDao userDao) {
            mDeudadDaoAsync = userDao;
        }

        @Override
        protected Long doInBackground(DeudaEntity... notes) {
            long id = mDeudadDaoAsync.insert(notes[0]);
            return id;
        }
    }

    private static class updateDeudaAsync extends AsyncTask<DeudaEntity, Void, Void> {

        private DeudadDao mDeudadDaoAsync;

        updateDeudaAsync(DeudadDao userDao) {
            mDeudadDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(DeudaEntity... notes) {
            mDeudadDaoAsync.update(notes[0]);
            return null;
        }
    }

    private static class deleteDeudaAsync extends AsyncTask<DeudaEntity, Void, Void> {

        private DeudadDao mDeudadDaoAsync;

        deleteDeudaAsync(DeudadDao userDao) {
            mDeudadDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(DeudaEntity... notes) {
            mDeudadDaoAsync.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllDeudaAsync extends AsyncTask<DeudaEntity, Void, Void> {

        private DeudadDao mDeudadDaoAsync;

        deleteAllDeudaAsync(DeudadDao DeudadDao) {
            mDeudadDaoAsync = DeudadDao;
        }

        @Override
        protected Void doInBackground(DeudaEntity... notes) {
            mDeudadDaoAsync.deleteAll();
            return null;
        }
    }
}
