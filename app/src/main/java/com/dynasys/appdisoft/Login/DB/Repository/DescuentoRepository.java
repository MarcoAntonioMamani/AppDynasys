package com.dynasys.appdisoft.Login.DB.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dynasys.appdisoft.Login.DB.AppDatabase;
import com.dynasys.appdisoft.Login.DB.Dao.DescuentosDao;
import com.dynasys.appdisoft.Login.DB.Dao.StockDao;
import com.dynasys.appdisoft.Login.DB.Entity.DescuentosEntity;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DescuentoRepository {
    private DescuentosDao mDescuentoDao;
    private LiveData<List<DescuentosEntity>> mAllDescuentos;

    public DescuentoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mDescuentoDao = db.DescuentoDao();
        mAllDescuentos = mDescuentoDao.getAllDescuentos();
    }

    public LiveData<List<DescuentosEntity>> getAllDescuentos() {
        return mAllDescuentos;
    }

    public DescuentosEntity getDescuento(int noteId) throws ExecutionException, InterruptedException {
        return new getDescuentoAsync(mDescuentoDao).execute(noteId).get();
    }
    public List<DescuentosEntity> getMDescuentoAll(int clienteId) throws ExecutionException, InterruptedException {
        return new getMDescuentoAllAsync(mDescuentoDao).execute(clienteId).get();
    }
    public List<DescuentosEntity> getMDescuentosbyIdProducto(int clienteId) throws ExecutionException, InterruptedException {
        return new getMDescuentosByIdProductoAsync(mDescuentoDao).execute(clienteId).get();
    }
    public void insertDescuento(DescuentosEntity user) {
        new insertDescuentosAsync(mDescuentoDao).execute(user);
    }
    public void insertDescuentoList(List<DescuentosEntity> user) {
        new insertListDescuentoAsync(mDescuentoDao).execute(user);
    }
//insertListDescuentoAsync
    public void updateDescuentos(DescuentosEntity user) {
        new updateDescuentosAsync(mDescuentoDao).execute(user);
    }

    public void deleteDescuentos(DescuentosEntity user) {
        new deleteDescuentoAsync(mDescuentoDao).execute(user);
    }

    public void deleteAllDescuentos() {
        new deleteAllDescuentosAsync(mDescuentoDao).execute();
    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */

    private static class getDescuentoAsync extends AsyncTask<Integer, Void, DescuentosEntity> {

        private DescuentosDao mDescuentoDaoAsync;

        getDescuentoAsync(DescuentosDao animalDao) {
            mDescuentoDaoAsync = animalDao;
        }

        @Override
        protected DescuentosEntity doInBackground(Integer... ids) {
            return mDescuentoDaoAsync.getDescuentoById(ids[0]);
        }
    }
    private static class getMDescuentoAllAsync extends AsyncTask<Integer, Void, List<DescuentosEntity>> {

        private DescuentosDao mDescuentoDaoAsync;

        getMDescuentoAllAsync(DescuentosDao clienteDao) {
            mDescuentoDaoAsync = clienteDao;
        }

        @Override
        protected List<DescuentosEntity> doInBackground(Integer... ids) {
            return mDescuentoDaoAsync.getAllMDescuentos();
        }
    }
    private static class getMDescuentosByIdProductoAsync extends AsyncTask<Integer, Void, List<DescuentosEntity>> {

        private DescuentosDao mDescuentoDaoAsync;

        getMDescuentosByIdProductoAsync(DescuentosDao clienteDao) {
            mDescuentoDaoAsync = clienteDao;
        }

        @Override
        protected List<DescuentosEntity> doInBackground(Integer... ids) {
            return mDescuentoDaoAsync.getDescuento(ids[0]);
        }
    }
    private static class insertDescuentosAsync extends AsyncTask<DescuentosEntity, Void, Long> {

        private DescuentosDao mDescuentoDaoAsync;

        insertDescuentosAsync(DescuentosDao userDao) {
            mDescuentoDaoAsync = userDao;
        }

        @Override
        protected Long doInBackground(DescuentosEntity... notes) {
            long id = mDescuentoDaoAsync.insert(notes[0]);
            return id;
        }
    }
    private static class insertListDescuentoAsync extends AsyncTask<List<DescuentosEntity>, Void, Void> {

        private DescuentosDao mDescuentoDaoAsync;

        insertListDescuentoAsync(DescuentosDao userDao) {
            mDescuentoDaoAsync = userDao;
        }



        @Override
        protected Void doInBackground(List<DescuentosEntity>... lists) {
            List<DescuentosEntity> st=new ArrayList<>();
            st=lists[0];
            mDescuentoDaoAsync.insertList(st);
            return null;
        }
    }
    private static class updateDescuentosAsync extends AsyncTask<DescuentosEntity, Void, Void> {

        private DescuentosDao mDescuentoDaoAsync;

        updateDescuentosAsync(DescuentosDao userDao) {
            mDescuentoDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(DescuentosEntity... notes) {
            mDescuentoDaoAsync.update(notes[0]);
            return null;
        }
    }

    private static class deleteDescuentoAsync extends AsyncTask<DescuentosEntity, Void, Void> {

        private DescuentosDao mDescuentoDaoAsync;

        deleteDescuentoAsync(DescuentosDao userDao) {
            mDescuentoDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(DescuentosEntity... notes) {
            mDescuentoDaoAsync.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllDescuentosAsync extends AsyncTask<DescuentosEntity, Void, Void> {

        private DescuentosDao mDescuentoDaoAsync;

        deleteAllDescuentosAsync(DescuentosDao DescuentosDao) {
            mDescuentoDaoAsync = DescuentosDao;
        }

        @Override
        protected Void doInBackground(DescuentosEntity... notes) {
            mDescuentoDaoAsync.deleteAll();
            return null;
        }
    }
}
