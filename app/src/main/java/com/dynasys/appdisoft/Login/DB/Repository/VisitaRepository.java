package com.dynasys.appdisoft.Login.DB.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.os.Handler;

import com.dynasys.appdisoft.Login.DB.AppDatabase;
import com.dynasys.appdisoft.Login.DB.Dao.VisitaDao;
import com.dynasys.appdisoft.Login.DB.Entity.VisitaEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class VisitaRepository {



    private VisitaDao mVisitaDao;
    private LiveData<List<VisitaEntity>> mAllVisita;

    public VisitaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mVisitaDao = db.visitaDao();
        mAllVisita = mVisitaDao.getAllVisita();
    }

    public LiveData<List<VisitaEntity>> getAllVisita() {
        return mAllVisita;
    }

    public VisitaEntity getVisita(int noteId) throws ExecutionException, InterruptedException {
        return new VisitaRepository.getVisitaAsync(mVisitaDao).execute(noteId).get();
    }
    public List<VisitaEntity> getMVisitaAll(int clienteId) throws ExecutionException, InterruptedException {
        return new VisitaRepository.getMVisitaAllAsync(mVisitaDao).execute(clienteId).get();
    }

    public void insertVisita(VisitaEntity user) {
        new VisitaRepository.insertVisitaAsync(mVisitaDao).execute(user);
    }

    public void updateVisita(VisitaEntity user) {
        new VisitaRepository.updateVisitaAsync(mVisitaDao).execute(user);
    }
    public void InsertVisitaList(List<VisitaEntity > list){

        new VisitaRepository.insertListVisitaAsync(mVisitaDao).execute(list);
    }
    public void deleteVisita(VisitaEntity user) {
        new VisitaRepository.deleteVisitaAsync(mVisitaDao).execute(user);
    }

    public void deleteAllVisita() {
        new VisitaRepository.deleteAllVisitaAsync(mVisitaDao).execute();
    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */
    private class InsertarListaAsync extends AsyncTask<List<VisitaEntity>, String, String> {

        private VisitaDao mVisitaDaoAsync;
        InsertarListaAsync(VisitaDao animalDao) {
            mVisitaDaoAsync = animalDao;
        }



        @Override
        protected String doInBackground(List<VisitaEntity>... lists) {
            List<VisitaEntity> st=new ArrayList<>();
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
    private static class getVisitaAsync extends AsyncTask<Integer, Void, VisitaEntity> {

        private VisitaDao mVisitaDaoAsync;

        getVisitaAsync(VisitaDao animalDao) {
            mVisitaDaoAsync = animalDao;
        }

        @Override
        protected VisitaEntity doInBackground(Integer... ids) {
            return mVisitaDaoAsync.getVisitaProducto(ids[0]);
        }
    }


    private static class getMVisitaAllAsync extends AsyncTask<Integer, Void, List<VisitaEntity>> {

        private VisitaDao mPedidoDaoAsync;

        getMVisitaAllAsync(VisitaDao clienteDao) {
            mPedidoDaoAsync = clienteDao;
        }

        @Override
        protected List<VisitaEntity> doInBackground(Integer... ids) {
            return mPedidoDaoAsync.getVisitaMAll();
        }
    }
    private static class insertVisitaAsync extends AsyncTask<VisitaEntity, Void, Long> {

        private VisitaDao mVisitaDaoAsync;

        insertVisitaAsync(VisitaDao userDao) {
            mVisitaDaoAsync = userDao;
        }

        @Override
        protected Long doInBackground(VisitaEntity... notes) {
            long id = mVisitaDaoAsync.insert(notes[0]);
            return id;
        }
    }
    private static class insertListVisitaAsync extends AsyncTask<List<VisitaEntity>, Void, Void> {

        private VisitaDao mVisitaDaoAsync;

        insertListVisitaAsync(VisitaDao userDao) {
            mVisitaDaoAsync = userDao;
        }



        @Override
        protected Void doInBackground(List<VisitaEntity>... lists) {
            List<VisitaEntity> st=new ArrayList<>();
            st=lists[0];
            mVisitaDaoAsync.insertList(st);
            return null;
        }
    }
    private static class updateVisitaAsync extends AsyncTask<VisitaEntity, Void, Void> {

        private VisitaDao mVisitaDaoAsync;

        updateVisitaAsync(VisitaDao userDao) {
            mVisitaDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(VisitaEntity... notes) {
            mVisitaDaoAsync.update(notes[0]);
            return null;
        }
    }

    private static class deleteVisitaAsync extends AsyncTask<VisitaEntity, Void, Void> {

        private VisitaDao mVisitaDaoAsync;

        deleteVisitaAsync(VisitaDao userDao) {
            mVisitaDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(VisitaEntity... notes) {
            mVisitaDaoAsync.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllVisitaAsync extends AsyncTask<VisitaEntity, Void, Void> {

        private VisitaDao mVisitaDaoAsync;

        deleteAllVisitaAsync(VisitaDao VisitaDao) {
            mVisitaDaoAsync = VisitaDao;
        }

        @Override
        protected Void doInBackground(VisitaEntity... notes) {
            mVisitaDaoAsync.deleteAll();
            return null;
        }
    }
}
