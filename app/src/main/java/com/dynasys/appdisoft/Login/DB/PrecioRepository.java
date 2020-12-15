package com.dynasys.appdisoft.Login.DB;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dynasys.appdisoft.Login.DB.Dao.PrecioDao;
import com.dynasys.appdisoft.Login.DB.Dao.ProductoDao;
import com.dynasys.appdisoft.Login.DB.Dao.StockDao;
import com.dynasys.appdisoft.Login.DB.Entity.PrecioEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteRepository;
import com.dynasys.appdisoft.SincronizarData.DB.Dao.ClientesDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;



public class PrecioRepository {

    private PrecioDao mPrecioDao;
    private LiveData<List<PrecioEntity>> mAllPrecio;

    public PrecioRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mPrecioDao = db.precioDao();
        mAllPrecio = mPrecioDao.getAllPrecios();
    }
    public List<PrecioEntity> getMPrecioAll(int clienteId) throws ExecutionException, InterruptedException {
        return new getMPrecioAllAsync(mPrecioDao).execute(clienteId).get();
    }
    public LiveData<List<PrecioEntity>> getAllPrecios() {
        return mAllPrecio;
    }

    public PrecioEntity getPrecio(int noteId) throws ExecutionException, InterruptedException {
        return new getPreciosAsync(mPrecioDao).execute(noteId).get();
    }

    public void insertPrecios(PrecioEntity user) {
        new insertPreciosAsync(mPrecioDao).execute(user);
    }
public void insertpreciosList(List<PrecioEntity> precio){
        new insertListPrecioAsync(mPrecioDao).execute(precio);

}
    public void updatePrecios(PrecioEntity user) {
        new updatePrecioAsync(mPrecioDao).execute(user);
    }
    public void updateListPrecios(PrecioEntity[] user) {
        new updateListPrecioAsync(mPrecioDao).execute(user);
    }

    public void deletePrecios(PrecioEntity user) {
        new deletePrecioAsync(mPrecioDao).execute(user);
    }

    public void deleteAllPrecios() {
        new deleteAllPreciosAsync(mPrecioDao).execute();
    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */
        private static class getMPrecioAllAsync extends AsyncTask<Integer, Void, List<PrecioEntity>> {

        private PrecioDao mPrecioDaoAsync;

        getMPrecioAllAsync(PrecioDao clienteDao) {
            mPrecioDaoAsync = clienteDao;
        }

        @Override
        protected List<PrecioEntity> doInBackground(Integer... ids) {
            return mPrecioDaoAsync.getPrecioAll();
        }
    }
    private static class getPreciosAsync extends AsyncTask<Integer, Void, PrecioEntity> {

        private PrecioDao mPrecioDaoAsync;

        getPreciosAsync(PrecioDao animalDao) {
            mPrecioDaoAsync = animalDao;
        }

        @Override
        protected PrecioEntity doInBackground(Integer... ids) {
            return mPrecioDaoAsync.getPrecioById(ids[0]);
        }
    }

    private static class insertPreciosAsync extends AsyncTask<PrecioEntity, Void, Long> {

        private PrecioDao mPrecioDaoAsync;

        insertPreciosAsync(PrecioDao userDao) {
            mPrecioDaoAsync = userDao;
        }

        @Override
        protected Long doInBackground(PrecioEntity... notes) {
            long id = mPrecioDaoAsync.insert(notes[0]);
            return id;
        }
    }
    private static class insertListPrecioAsync extends AsyncTask<List<PrecioEntity>, Void, Void> {

        private PrecioDao mPrecioDaoAsync;

        insertListPrecioAsync(PrecioDao userDao) {
            mPrecioDaoAsync = userDao;
        }



        @Override
        protected Void doInBackground(List<PrecioEntity>... lists) {
            List<PrecioEntity> st=new ArrayList<>();
            st=lists[0];
            mPrecioDaoAsync.insertList(st);
            return null;
        }
    }

    private static class updatePrecioAsync extends AsyncTask<PrecioEntity, Void, Void> {

        private PrecioDao mPrecioDaoAsync;

        updatePrecioAsync(PrecioDao userDao) {
            mPrecioDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(PrecioEntity... notes) {
            mPrecioDaoAsync.update(notes[0]);
            return null;
        }
    }
    private static class updateListPrecioAsync extends AsyncTask<PrecioEntity, Void, Void> {

        private PrecioDao mPrecioDaoAsync;

        updateListPrecioAsync(PrecioDao userDao) {
            mPrecioDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(PrecioEntity[] notes) {
            mPrecioDaoAsync.update(notes);
            return null;
        }
    }
    private static class deletePrecioAsync extends AsyncTask<PrecioEntity, Void, Void> {

        private PrecioDao mPrecioDaoAsync;

        deletePrecioAsync(PrecioDao userDao) {
            mPrecioDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(PrecioEntity... notes) {
            mPrecioDaoAsync.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllPreciosAsync extends AsyncTask<PrecioEntity, Void, Void> {

        private PrecioDao mPrecioDaoAsync;

        deleteAllPreciosAsync(PrecioDao productoDao) {
            mPrecioDaoAsync = productoDao;
        }

        @Override
        protected Void doInBackground(PrecioEntity... notes) {
            mPrecioDaoAsync.deleteAll();
            return null;
        }
    }
}
