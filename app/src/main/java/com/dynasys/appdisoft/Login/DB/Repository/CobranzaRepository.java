package com.dynasys.appdisoft.Login.DB.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dynasys.appdisoft.Login.DB.AppDatabase;
import com.dynasys.appdisoft.Login.DB.Dao.CobranzaDao;
import com.dynasys.appdisoft.Login.DB.Entity.CobranzaEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CobranzaRepository {
    private CobranzaDao mCobranzaDao;
    private LiveData<List<CobranzaEntity>> mAllCobranza;

    public CobranzaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mCobranzaDao = db.CobranzaDao();
        mAllCobranza = mCobranzaDao.getAllcobranza();
    }

    public LiveData<List<CobranzaEntity>> getAllCobranza() {
        return mAllCobranza;
    }

    public CobranzaEntity getCobranza(String noteId) throws ExecutionException, InterruptedException {
        return new getCobranzaAsync(mCobranzaDao).execute(noteId).get();
    }
    public List<CobranzaEntity> getMCobranzaAll(int clienteId) throws ExecutionException, InterruptedException {
        return new getMCobranzaAllAsync(mCobranzaDao).execute(clienteId).get();
    }
    public List<CobranzaEntity> getMCobranzaAllNoSincronizadas(int clienteId) throws ExecutionException, InterruptedException {
        return new getCobranzasNoSincronizadasAllAsync(mCobranzaDao).execute(clienteId).get();
    }
    public void insertCobranza(CobranzaEntity user) {
        new insertCobranzaAsync(mCobranzaDao).execute(user);
    }

    public void updateCobranza(CobranzaEntity user) {
        new updateCobranzaAsync(mCobranzaDao).execute(user);
    }

    public void deleteCobranza(CobranzaEntity user) {
        new deleteCobranzaAsync(mCobranzaDao).execute(user);
    }

    public void deleteAllCobranza() {
        new deleteAllCobranzaAsync(mCobranzaDao).execute();
    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */

    private static class getCobranzaAsync extends AsyncTask<String, Void, CobranzaEntity> {

        private CobranzaDao mCobranzaDaoAsync;

        getCobranzaAsync(CobranzaDao animalDao) {
            mCobranzaDaoAsync = animalDao;
        }

        @Override
        protected CobranzaEntity doInBackground(String... ids) {
            return mCobranzaDaoAsync.getcobranza(ids[0]);
        }
    }


    private static class getMCobranzaAllAsync extends AsyncTask<Integer, Void, List<CobranzaEntity>> {

        private CobranzaDao mPedidoDaoAsync;

        getMCobranzaAllAsync(CobranzaDao clienteDao) {
            mPedidoDaoAsync = clienteDao;
        }

        @Override
        protected List<CobranzaEntity> doInBackground(Integer... ids) {
            return mPedidoDaoAsync.getcobranzaMAll();
        }
    }

    private static class getCobranzasNoSincronizadasAllAsync extends AsyncTask<Integer, Void, List<CobranzaEntity>> {

        private CobranzaDao mPedidoDaoAsync;

        getCobranzasNoSincronizadasAllAsync(CobranzaDao clienteDao) {
            mPedidoDaoAsync = clienteDao;
        }

        @Override
        protected List<CobranzaEntity> doInBackground(Integer... ids) {
            return mPedidoDaoAsync.getcobranzaNoSincronizados();
        }
    }
    private static class insertCobranzaAsync extends AsyncTask<CobranzaEntity, Void, Long> {

        private CobranzaDao mCobranzaDaoAsync;

        insertCobranzaAsync(CobranzaDao userDao) {
            mCobranzaDaoAsync = userDao;
        }

        @Override
        protected Long doInBackground(CobranzaEntity... notes) {
            long id = mCobranzaDaoAsync.insert(notes[0]);
            return id;
        }
    }

    private static class updateCobranzaAsync extends AsyncTask<CobranzaEntity, Void, Void> {

        private CobranzaDao mCobranzaDaoAsync;

        updateCobranzaAsync(CobranzaDao userDao) {
            mCobranzaDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(CobranzaEntity... notes) {
            mCobranzaDaoAsync.update(notes[0]);
            return null;
        }
    }

    private static class deleteCobranzaAsync extends AsyncTask<CobranzaEntity, Void, Void> {

        private CobranzaDao mCobranzaDaoAsync;

        deleteCobranzaAsync(CobranzaDao userDao) {
            mCobranzaDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(CobranzaEntity... notes) {
            mCobranzaDaoAsync.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllCobranzaAsync extends AsyncTask<CobranzaEntity, Void, Void> {

        private CobranzaDao mCobranzaDaoAsync;

        deleteAllCobranzaAsync(CobranzaDao CobranzaDao) {
            mCobranzaDaoAsync = CobranzaDao;
        }

        @Override
        protected Void doInBackground(CobranzaEntity... notes) {
            mCobranzaDaoAsync.deleteAll();
            return null;
        }
    }
}
