package com.dynasys.appdisoft.Login.DB.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.os.Handler;

import com.dynasys.appdisoft.Login.DB.AppDatabase;
import com.dynasys.appdisoft.Login.DB.Dao.StockDao;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StockRepository {

    private StockDao mStockDao;
    private LiveData<List<StockEntity>> mAllStock;

    public StockRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mStockDao = db.stockDao();
        mAllStock = mStockDao.getAllStock();
    }

    public LiveData<List<StockEntity>> getAllStock() {
        return mAllStock;
    }

    public StockEntity getStock(int noteId) throws ExecutionException, InterruptedException {
        return new getStockAsync(mStockDao).execute(noteId).get();
    }
    public List<StockEntity> getMStockAll(int clienteId) throws ExecutionException, InterruptedException {
        return new StockRepository.getMStockAllAsync(mStockDao).execute(clienteId).get();
    }

    public void insertStock(StockEntity user) {
        new insertStockAsync(mStockDao).execute(user);
    }

    public void updateStock(StockEntity user) {
        new updateStockAsync(mStockDao).execute(user);
    }
public void InsertStockList(List<StockEntity > list){

        new insertListStockAsync(mStockDao).execute(list);
}
    public void deleteStock(StockEntity user) {
        new deleteStockAsync(mStockDao).execute(user);
    }

    public void deleteAllStock() {
        new deleteAllStockAsync(mStockDao).execute();
    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */
    private class InsertarListaAsync extends AsyncTask<List<StockEntity>, String, String> {

        private StockDao mStockDaoAsync;
        InsertarListaAsync(StockDao animalDao) {
            mStockDaoAsync = animalDao;
        }



        @Override
        protected String doInBackground(List<StockEntity>... lists) {
            List<StockEntity> st=new ArrayList<>();
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
    private static class getStockAsync extends AsyncTask<Integer, Void, StockEntity> {

        private StockDao mStockDaoAsync;

        getStockAsync(StockDao animalDao) {
            mStockDaoAsync = animalDao;
        }

        @Override
        protected StockEntity doInBackground(Integer... ids) {
            return mStockDaoAsync.getStockProducto(ids[0]);
        }
    }


    private static class getMStockAllAsync extends AsyncTask<Integer, Void, List<StockEntity>> {

        private StockDao mPedidoDaoAsync;

        getMStockAllAsync(StockDao clienteDao) {
            mPedidoDaoAsync = clienteDao;
        }

        @Override
        protected List<StockEntity> doInBackground(Integer... ids) {
            return mPedidoDaoAsync.getStockMAll();
        }
    }
    private static class insertStockAsync extends AsyncTask<StockEntity, Void, Long> {

        private StockDao mStockDaoAsync;

        insertStockAsync(StockDao userDao) {
            mStockDaoAsync = userDao;
        }

        @Override
        protected Long doInBackground(StockEntity... notes) {
            long id = mStockDaoAsync.insert(notes[0]);
            return id;
        }
    }
    private static class insertListStockAsync extends AsyncTask<List<StockEntity>, Void, Void> {

        private StockDao mStockDaoAsync;

        insertListStockAsync(StockDao userDao) {
            mStockDaoAsync = userDao;
        }



        @Override
        protected Void doInBackground(List<StockEntity>... lists) {
            List<StockEntity> st=new ArrayList<>();
            st=lists[0];
            mStockDaoAsync.insertList(st);
            return null;
        }
    }
    private static class updateStockAsync extends AsyncTask<StockEntity, Void, Void> {

        private StockDao mStockDaoAsync;

        updateStockAsync(StockDao userDao) {
            mStockDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(StockEntity... notes) {
            mStockDaoAsync.update(notes[0]);
            return null;
        }
    }

    private static class deleteStockAsync extends AsyncTask<StockEntity, Void, Void> {

        private StockDao mStockDaoAsync;

        deleteStockAsync(StockDao userDao) {
            mStockDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(StockEntity... notes) {
            mStockDaoAsync.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllStockAsync extends AsyncTask<StockEntity, Void, Void> {

        private StockDao mStockDaoAsync;

        deleteAllStockAsync(StockDao StockDao) {
            mStockDaoAsync = StockDao;
        }

        @Override
        protected Void doInBackground(StockEntity... notes) {
            mStockDaoAsync.deleteAll();
            return null;
        }
    }
}