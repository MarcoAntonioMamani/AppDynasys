package com.dynasys.appdisoft.Login.DB;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dynasys.appdisoft.Login.DB.Dao.DetalleDao;
import com.dynasys.appdisoft.Login.DB.Dao.PedidoDao;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class DetalleRepository {

    private DetalleDao mDetalleDao;
    private LiveData<List<DetalleEntity>> mAllDetalle;

    public DetalleRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mDetalleDao = db.detalleDao();
        mAllDetalle = mDetalleDao.getAllDetalle();
    }
    public List<DetalleEntity> getMDetalleAll(int clienteId) throws ExecutionException, InterruptedException {
        return new getMDetalleAllAsync(mDetalleDao).execute(clienteId).get();
    }
    public LiveData<List<DetalleEntity>> getAllDetalle() {
        return mAllDetalle;
    }

    public DetalleEntity getDetalle(int noteId) throws ExecutionException, InterruptedException {
        return new getDetallesAsync(mDetalleDao).execute(noteId).get();
    }



    public void insertDetalles(DetalleEntity user) {
        new insertDetalleAsync(mDetalleDao).execute(user);
    }

    public void updateDetalle(DetalleEntity user) {
        new updateDetalleAsync(mDetalleDao).execute(user);
    }
    public void updateListDetalle(DetalleEntity[] user) {
        new updateListDetalleAsync(mDetalleDao).execute(user);
    }

    public void deleteDetalle(DetalleEntity user) {
        new deleteDetalleAsync(mDetalleDao).execute(user);
    }

    public void deleteAllDetalles() {
        new deleteAllDetalleAsync(mDetalleDao).execute();
    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */
        private static class getMDetalleAllAsync extends AsyncTask<Integer, Void, List<DetalleEntity>> {

        private DetalleDao mDetalleDaoAsync;

        getMDetalleAllAsync(DetalleDao clienteDao) {
            mDetalleDaoAsync = clienteDao;
        }

        @Override
        protected List<DetalleEntity> doInBackground(Integer... ids) {
            return mDetalleDaoAsync.getDetalleAll();
        }
    }
    private static class getDetallesAsync extends AsyncTask<Integer, Void, DetalleEntity> {

        private DetalleDao mDetalleDaoAsync;

        getDetallesAsync(DetalleDao animalDao) {
            mDetalleDaoAsync = animalDao;
        }

        @Override
        protected DetalleEntity doInBackground(Integer... ids) {
            return mDetalleDaoAsync.getDetalleById(ids[0]);
        }
    }
    private static class getDetallesbyNumiAndProdAsync extends AsyncTask<Integer, Void, DetalleEntity> {

        private DetalleDao mDetalleDaoAsync;

        getDetallesbyNumiAndProdAsync(DetalleDao animalDao) {
            mDetalleDaoAsync = animalDao;
        }


        @Override
        protected DetalleEntity doInBackground(Integer[] integers) {
            return mDetalleDaoAsync.getDetalleByIdProducto(integers[0],integers[1]);
        }
    }
    private static class insertDetalleAsync extends AsyncTask<DetalleEntity, Void, Long> {

        private DetalleDao mDetalleDaoAsync;

        insertDetalleAsync(DetalleDao userDao) {
            mDetalleDaoAsync = userDao;
        }

        @Override
        protected Long doInBackground(DetalleEntity... notes) {
            long id = mDetalleDaoAsync.insert(notes[0]);
            return id;
        }
    }

    private static class updateDetalleAsync extends AsyncTask<DetalleEntity, Void, Void> {

        private DetalleDao mDetalleDaoAsync;

        updateDetalleAsync(DetalleDao userDao) {
            mDetalleDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(DetalleEntity... notes) {
            mDetalleDaoAsync.update(notes[0]);
            return null;
        }
    }
    private static class updateListDetalleAsync extends AsyncTask<DetalleEntity, Void, Void> {

        private DetalleDao mDetalleDaoAsync;

        updateListDetalleAsync(DetalleDao userDao) {
            mDetalleDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(DetalleEntity[] notes) {
            mDetalleDaoAsync.update(notes);
            return null;
        }
    }
    private static class deleteDetalleAsync extends AsyncTask<DetalleEntity, Void, Void> {

        private DetalleDao mDetalleDaoAsync;

        deleteDetalleAsync(DetalleDao userDao) {
            mDetalleDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(DetalleEntity... notes) {
            mDetalleDaoAsync.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllDetalleAsync extends AsyncTask<DetalleEntity, Void, Void> {

        private DetalleDao mDetalleDaoAsync;

        deleteAllDetalleAsync(DetalleDao productoDao) {
            mDetalleDaoAsync = productoDao;
        }

        @Override
        protected Void doInBackground(DetalleEntity... notes) {
            mDetalleDaoAsync.deleteAll();
            return null;
        }
    }
}
