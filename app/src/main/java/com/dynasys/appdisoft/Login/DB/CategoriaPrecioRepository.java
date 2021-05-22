package com.dynasys.appdisoft.Login.DB;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.os.Handler;

import com.dynasys.appdisoft.Login.DB.Dao.CategoriaPrecioDao;
import com.dynasys.appdisoft.Login.DB.Entity.CategoriaPrecioEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CategoriaPrecioRepository {



    private CategoriaPrecioDao mCategoriaPrecioDao;
    private LiveData<List<CategoriaPrecioEntity>> mAllCategoriaPrecio;

    public CategoriaPrecioRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mCategoriaPrecioDao = db.categoriaPrecioDao();
        mAllCategoriaPrecio = mCategoriaPrecioDao.getAllCategoriaPrecio();
    }

    public LiveData<List<CategoriaPrecioEntity>> getAllCategoriaPrecio() {
        return mAllCategoriaPrecio;
    }

    public CategoriaPrecioEntity getCategoriaPrecio(int noteId) throws ExecutionException, InterruptedException {
        return new CategoriaPrecioRepository.getCategoriaPrecioAsync(mCategoriaPrecioDao).execute(noteId).get();
    }
    public List<CategoriaPrecioEntity> getMCategoriaPrecioAll(int clienteId) throws ExecutionException, InterruptedException {
        return new CategoriaPrecioRepository.getMCategoriaPrecioAllAsync(mCategoriaPrecioDao).execute(clienteId).get();
    }

    public void insertCategoriaPrecio(CategoriaPrecioEntity user) {
        new CategoriaPrecioRepository.insertCategoriaPrecioAsync(mCategoriaPrecioDao).execute(user);
    }

    public void updateCategoriaPrecio(CategoriaPrecioEntity user) {
        new CategoriaPrecioRepository.updateCategoriaPrecioAsync(mCategoriaPrecioDao).execute(user);
    }
    public void InsertCategoriaPrecioList(List<CategoriaPrecioEntity > list){

        new CategoriaPrecioRepository.insertListCategoriaPrecioAsync(mCategoriaPrecioDao).execute(list);
    }
    public void deleteCategoriaPrecio(CategoriaPrecioEntity user) {
        new CategoriaPrecioRepository.deleteCategoriaPrecioAsync(mCategoriaPrecioDao).execute(user);
    }

    public void deleteAllCategoriaPrecio() {
        new CategoriaPrecioRepository.deleteAllCategoriaPrecioAsync(mCategoriaPrecioDao).execute();
    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */
    private class InsertarListaAsync extends AsyncTask<List<CategoriaPrecioEntity>, String, String> {

        private CategoriaPrecioDao mCategoriaPrecioDaoAsync;
        InsertarListaAsync(CategoriaPrecioDao animalDao) {
            mCategoriaPrecioDaoAsync = animalDao;
        }



        @Override
        protected String doInBackground(List<CategoriaPrecioEntity>... lists) {
            List<CategoriaPrecioEntity> st=new ArrayList<>();
            st=lists[0];


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //NUESTRO CODIGO
            new Handler().postDelayed(new Runnable() {
                public void run() {

                }
            }, 0);
            super.onPostExecute(result);
        }
    }
    private static class getCategoriaPrecioAsync extends AsyncTask<Integer, Void, CategoriaPrecioEntity> {

        private CategoriaPrecioDao mCategoriaPrecioDaoAsync;

        getCategoriaPrecioAsync(CategoriaPrecioDao animalDao) {
            mCategoriaPrecioDaoAsync = animalDao;
        }

        @Override
        protected CategoriaPrecioEntity doInBackground(Integer... ids) {
            return mCategoriaPrecioDaoAsync.getCategoriaPrecioProducto(ids[0]);
        }
    }


    private static class getMCategoriaPrecioAllAsync extends AsyncTask<Integer, Void, List<CategoriaPrecioEntity>> {

        private CategoriaPrecioDao mPedidoDaoAsync;

        getMCategoriaPrecioAllAsync(CategoriaPrecioDao clienteDao) {
            mPedidoDaoAsync = clienteDao;
        }

        @Override
        protected List<CategoriaPrecioEntity> doInBackground(Integer... ids) {
            return mPedidoDaoAsync.getCategoriaPrecioMAll();
        }
    }
    private static class insertCategoriaPrecioAsync extends AsyncTask<CategoriaPrecioEntity, Void, Long> {

        private CategoriaPrecioDao mCategoriaPrecioDaoAsync;

        insertCategoriaPrecioAsync(CategoriaPrecioDao userDao) {
            mCategoriaPrecioDaoAsync = userDao;
        }

        @Override
        protected Long doInBackground(CategoriaPrecioEntity... notes) {
            long id = mCategoriaPrecioDaoAsync.insert(notes[0]);
            return id;
        }
    }
    private static class insertListCategoriaPrecioAsync extends AsyncTask<List<CategoriaPrecioEntity>, Void, Void> {

        private CategoriaPrecioDao mCategoriaPrecioDaoAsync;

        insertListCategoriaPrecioAsync(CategoriaPrecioDao userDao) {
            mCategoriaPrecioDaoAsync = userDao;
        }



        @Override
        protected Void doInBackground(List<CategoriaPrecioEntity>... lists) {
            List<CategoriaPrecioEntity> st=new ArrayList<>();
            st=lists[0];
            mCategoriaPrecioDaoAsync.insertList(st);
            return null;
        }
    }
    private static class updateCategoriaPrecioAsync extends AsyncTask<CategoriaPrecioEntity, Void, Void> {

        private CategoriaPrecioDao mCategoriaPrecioDaoAsync;

        updateCategoriaPrecioAsync(CategoriaPrecioDao userDao) {
            mCategoriaPrecioDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(CategoriaPrecioEntity... notes) {
            mCategoriaPrecioDaoAsync.update(notes[0]);
            return null;
        }
    }

    private static class deleteCategoriaPrecioAsync extends AsyncTask<CategoriaPrecioEntity, Void, Void> {

        private CategoriaPrecioDao mCategoriaPrecioDaoAsync;

        deleteCategoriaPrecioAsync(CategoriaPrecioDao userDao) {
            mCategoriaPrecioDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(CategoriaPrecioEntity... notes) {
            mCategoriaPrecioDaoAsync.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllCategoriaPrecioAsync extends AsyncTask<CategoriaPrecioEntity, Void, Void> {

        private CategoriaPrecioDao mCategoriaPrecioDaoAsync;

        deleteAllCategoriaPrecioAsync(CategoriaPrecioDao CategoriaPrecioDao) {
            mCategoriaPrecioDaoAsync = CategoriaPrecioDao;
        }

        @Override
        protected Void doInBackground(CategoriaPrecioEntity... notes) {
            mCategoriaPrecioDaoAsync.deleteAll();
            return null;
        }
    }
}
