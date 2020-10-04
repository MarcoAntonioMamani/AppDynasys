package com.dynasys.appdisoft.Login.DB.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dynasys.appdisoft.Login.DB.AppDatabase;
import com.dynasys.appdisoft.Login.DB.Dao.CobranzaDetalleDao;
import com.dynasys.appdisoft.Login.DB.Dao.CobranzaDetalleDao;
import com.dynasys.appdisoft.Login.DB.Entity.CobranzaDetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.CobranzaDetalleEntity;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class CobranzaDetalleRepository {

    private CobranzaDetalleDao mCobranzaDetalleDao;
    private LiveData<List<CobranzaDetalleEntity>> mAllCobranzaDetalle;

    public CobranzaDetalleRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mCobranzaDetalleDao = db.CobranzaDetalleDao();
        mAllCobranzaDetalle = mCobranzaDetalleDao.getAllcobranzaDetalle();
    }

    public LiveData<List<CobranzaDetalleEntity>> getAllCobranza() {
        return mAllCobranzaDetalle;
    }

    public List<CobranzaDetalleEntity> getCobranzaDetalle(String noteId) throws ExecutionException, InterruptedException {
        return new getCobranzaAsync(mCobranzaDetalleDao).execute(noteId).get();
    }
    public List<CobranzaDetalleEntity> getMCobranzaDetalleAll(int clienteId) throws ExecutionException, InterruptedException {
        return new getMCobranzaAllAsync(mCobranzaDetalleDao).execute(clienteId).get();
    }
    public void insertCobranzaDetalle(CobranzaDetalleEntity user) {
        new insertCobranzaAsync(mCobranzaDetalleDao).execute(user);
    }

    public void updateCobranzaDetalle(CobranzaDetalleEntity user) {
        new updateCobranzaAsync(mCobranzaDetalleDao).execute(user);
    }

    public void deleteCobranzaDetalle(CobranzaDetalleEntity user) {
        new deleteCobranzaAsync(mCobranzaDetalleDao).execute(user);
    }

    public void deleteAllCobranzaDetalle() {
        new deleteAllCobranzaAsync(mCobranzaDetalleDao).execute();
    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */

    private static class getCobranzaAsync extends AsyncTask<String, Void, List<CobranzaDetalleEntity>> {

        private CobranzaDetalleDao mCobranzaDetalleDaoAsync;

        getCobranzaAsync(CobranzaDetalleDao animalDao) {
            mCobranzaDetalleDaoAsync = animalDao;
        }

        @Override
        protected List<CobranzaDetalleEntity> doInBackground(String... ids) {
            return mCobranzaDetalleDaoAsync.getcobranzaDetalleByPedido(ids[0]);
        }
    }


    private static class getMCobranzaAllAsync extends AsyncTask<Integer, Void, List<CobranzaDetalleEntity>> {

        private CobranzaDetalleDao mPedidoDaoAsync;

        getMCobranzaAllAsync(CobranzaDetalleDao clienteDao) {
            mPedidoDaoAsync = clienteDao;
        }

        @Override
        protected List<CobranzaDetalleEntity> doInBackground(Integer... ids) {
            return mPedidoDaoAsync.getcobranzaDetalleMAll();
        }
    }
    private static class insertCobranzaAsync extends AsyncTask<CobranzaDetalleEntity, Void, Long> {

        private CobranzaDetalleDao mCobranzaDetalleDaoAsync;

        insertCobranzaAsync(CobranzaDetalleDao userDao) {
            mCobranzaDetalleDaoAsync = userDao;
        }

        @Override
        protected Long doInBackground(CobranzaDetalleEntity... notes) {
            long id = mCobranzaDetalleDaoAsync.insert(notes[0]);
            return id;
        }
    }

    private static class updateCobranzaAsync extends AsyncTask<CobranzaDetalleEntity, Void, Void> {

        private CobranzaDetalleDao mCobranzaDetalleDaoAsync;

        updateCobranzaAsync(CobranzaDetalleDao userDao) {
            mCobranzaDetalleDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(CobranzaDetalleEntity... notes) {
            mCobranzaDetalleDaoAsync.update(notes[0]);
            return null;
        }
    }

    private static class deleteCobranzaAsync extends AsyncTask<CobranzaDetalleEntity, Void, Void> {

        private CobranzaDetalleDao mCobranzaDetalleDaoAsync;

        deleteCobranzaAsync(CobranzaDetalleDao userDao) {
            mCobranzaDetalleDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(CobranzaDetalleEntity... notes) {
            mCobranzaDetalleDaoAsync.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllCobranzaAsync extends AsyncTask<CobranzaDetalleEntity, Void, Void> {

        private CobranzaDetalleDao mCobranzaDetalleDaoAsync;

        deleteAllCobranzaAsync(CobranzaDetalleDao CobranzaDetalleDao) {
            mCobranzaDetalleDaoAsync = CobranzaDetalleDao;
        }

        @Override
        protected Void doInBackground(CobranzaDetalleEntity... notes) {
            mCobranzaDetalleDaoAsync.deleteAll();
            return null;
        }
    }
}
