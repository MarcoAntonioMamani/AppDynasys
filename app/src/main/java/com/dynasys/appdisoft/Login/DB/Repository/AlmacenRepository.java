package com.dynasys.appdisoft.Login.DB.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dynasys.appdisoft.Login.DB.AppDatabase;
import com.dynasys.appdisoft.Login.DB.Dao.AlmacenDao;
import com.dynasys.appdisoft.Login.DB.Entity.AlmacenEntity;
import com.dynasys.appdisoft.Login.DB.Entity.AlmacenEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AlmacenRepository {

    private AlmacenDao mAlmacenDao;
    private LiveData<List<AlmacenEntity>> mAllAlmacen;

    public AlmacenRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mAlmacenDao = db.AlmacenDao();
        mAllAlmacen = mAlmacenDao.getAllAlmacen();
    }

    public LiveData<List<AlmacenEntity>> getAllAlmacen() {
        return mAllAlmacen;
    }

    public AlmacenEntity getAlmacen(int noteId) throws ExecutionException, InterruptedException {
        return new AlmacenRepository.getAlmacenAsync(mAlmacenDao).execute(noteId).get();
    }
    public List<AlmacenEntity> getMAlmacenAll(int clienteId) throws ExecutionException, InterruptedException {
        return new AlmacenRepository.getMAlmacenAllAsync(mAlmacenDao).execute(clienteId).get();
    }
    public void insertAlmacen(AlmacenEntity user) {
        new AlmacenRepository.insertAlmacenAsync(mAlmacenDao).execute(user);
    }

    public void updateAlmacen(AlmacenEntity user) {
        new AlmacenRepository.updateAlmacenAsync(mAlmacenDao).execute(user);
    }

    public void deleteAlmacen(AlmacenEntity user) {
        new AlmacenRepository.deleteAlmacenAsync(mAlmacenDao).execute(user);
    }

    public void deleteAllAlmacen() {
        new AlmacenRepository.deleteAllAlmacenAsync(mAlmacenDao).execute();
    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */

    private static class getAlmacenAsync extends AsyncTask<Integer, Void, AlmacenEntity> {

        private AlmacenDao mAlmacenDaoAsync;

        getAlmacenAsync(AlmacenDao animalDao) {
            mAlmacenDaoAsync = animalDao;
        }

        @Override
        protected AlmacenEntity doInBackground(Integer... ids) {
            return mAlmacenDaoAsync.getAlmacenProducto(ids[0]);
        }
    }


    private static class getMAlmacenAllAsync extends AsyncTask<Integer, Void, List<AlmacenEntity>> {

        private AlmacenDao mPedidoDaoAsync;

        getMAlmacenAllAsync(AlmacenDao clienteDao) {
            mPedidoDaoAsync = clienteDao;
        }

        @Override
        protected List<AlmacenEntity> doInBackground(Integer... ids) {
            return mPedidoDaoAsync.getAlmacenMAll();
        }
    }
    private static class insertAlmacenAsync extends AsyncTask<AlmacenEntity, Void, Long> {

        private AlmacenDao mAlmacenDaoAsync;

        insertAlmacenAsync(AlmacenDao userDao) {
            mAlmacenDaoAsync = userDao;
        }

        @Override
        protected Long doInBackground(AlmacenEntity... notes) {
            long id = mAlmacenDaoAsync.insert(notes[0]);
            return id;
        }
    }

    private static class updateAlmacenAsync extends AsyncTask<AlmacenEntity, Void, Void> {

        private AlmacenDao mAlmacenDaoAsync;

        updateAlmacenAsync(AlmacenDao userDao) {
            mAlmacenDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(AlmacenEntity... notes) {
            mAlmacenDaoAsync.update(notes[0]);
            return null;
        }
    }

    private static class deleteAlmacenAsync extends AsyncTask<AlmacenEntity, Void, Void> {

        private AlmacenDao mAlmacenDaoAsync;

        deleteAlmacenAsync(AlmacenDao userDao) {
            mAlmacenDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(AlmacenEntity... notes) {
            mAlmacenDaoAsync.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllAlmacenAsync extends AsyncTask<AlmacenEntity, Void, Void> {

        private AlmacenDao mAlmacenDaoAsync;

        deleteAllAlmacenAsync(AlmacenDao AlmacenDao) {
            mAlmacenDaoAsync = AlmacenDao;
        }

        @Override
        protected Void doInBackground(AlmacenEntity... notes) {
            mAlmacenDaoAsync.deleteAll();
            return null;
        }
    }
}
