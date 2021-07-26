package com.dynasys.appdisoft.Login.DB.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.os.Handler;

import com.dynasys.appdisoft.Login.DB.AppDatabase;
import com.dynasys.appdisoft.Login.DB.Dao.PointDao;
import com.dynasys.appdisoft.Login.DB.Entity.PointEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PointRepository {

    private PointDao mPointDao;
    private LiveData<List<PointEntity>> mAllPoint;

    public PointRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mPointDao = db.pointDao();
        mAllPoint = mPointDao.getAllpoint();
    }

    public LiveData<List<PointEntity>> getAllPoint() {
        return mAllPoint;
    }

    public List<PointEntity> getPoint(int noteId) throws ExecutionException, InterruptedException {
        return new PointRepository.getPointAsync(mPointDao).execute(noteId).get();
    }
    public List<PointEntity> getMPointAll(int clienteId) throws ExecutionException, InterruptedException {
        return new PointRepository.getMPointAllAsync(mPointDao).execute(clienteId).get();
    }

    public void insertPoint(PointEntity user) {
        new PointRepository.insertPointAsync(mPointDao).execute(user);
    }

    public void updatePoint(PointEntity user) {
        new PointRepository.updatePointAsync(mPointDao).execute(user);
    }
    public void InsertPointList(List<PointEntity > list){

        new PointRepository.insertListPointAsync(mPointDao).execute(list);
    }
    public void deletePoint(PointEntity user) {
        new PointRepository.deletePointAsync(mPointDao).execute(user);
    }

    public void deleteAllPoint() {
        new PointRepository.deleteAllPointAsync(mPointDao).execute();
    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */
    private class InsertarListaAsync extends AsyncTask<List<PointEntity>, String, String> {

        private PointDao mPointDaoAsync;
        InsertarListaAsync(PointDao animalDao) {
            mPointDaoAsync = animalDao;
        }



        @Override
        protected String doInBackground(List<PointEntity>... lists) {
            List<PointEntity> st=new ArrayList<>();
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
    private static class getPointAsync extends AsyncTask<Integer, Void, List<PointEntity>> {

        private PointDao mPointDaoAsync;

        getPointAsync(PointDao animalDao) {
            mPointDaoAsync = animalDao;
        }

        @Override
        protected List<PointEntity> doInBackground(Integer... ids) {
            return mPointDaoAsync.getpointProducto(ids[0]);
        }
    }


    private static class getMPointAllAsync extends AsyncTask<Integer, Void, List<PointEntity>> {

        private PointDao mPedidoDaoAsync;

        getMPointAllAsync(PointDao clienteDao) {
            mPedidoDaoAsync = clienteDao;
        }

        @Override
        protected List<PointEntity> doInBackground(Integer... ids) {
            return mPedidoDaoAsync.getpointMAll();
        }
    }
    private static class insertPointAsync extends AsyncTask<PointEntity, Void, Long> {

        private PointDao mPointDaoAsync;

        insertPointAsync(PointDao userDao) {
            mPointDaoAsync = userDao;
        }

        @Override
        protected Long doInBackground(PointEntity... notes) {
            long id = mPointDaoAsync.insert(notes[0]);
            return id;
        }
    }
    private static class insertListPointAsync extends AsyncTask<List<PointEntity>, Void, Void> {

        private PointDao mPointDaoAsync;

        insertListPointAsync(PointDao userDao) {
            mPointDaoAsync = userDao;
        }



        @Override
        protected Void doInBackground(List<PointEntity>... lists) {
            List<PointEntity> st=new ArrayList<>();
            st=lists[0];
            mPointDaoAsync.insertList(st);
            return null;
        }
    }
    private static class updatePointAsync extends AsyncTask<PointEntity, Void, Void> {

        private PointDao mPointDaoAsync;

        updatePointAsync(PointDao userDao) {
            mPointDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(PointEntity... notes) {
            mPointDaoAsync.update(notes[0]);
            return null;
        }
    }

    private static class deletePointAsync extends AsyncTask<PointEntity, Void, Void> {

        private PointDao mPointDaoAsync;

        deletePointAsync(PointDao userDao) {
            mPointDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(PointEntity... notes) {
            mPointDaoAsync.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllPointAsync extends AsyncTask<PointEntity, Void, Void> {

        private PointDao mPointDaoAsync;

        deleteAllPointAsync(PointDao PointDao) {
            mPointDaoAsync = PointDao;
        }

        @Override
        protected Void doInBackground(PointEntity... notes) {
            mPointDaoAsync.deleteAll();
            return null;
        }
    }
}
