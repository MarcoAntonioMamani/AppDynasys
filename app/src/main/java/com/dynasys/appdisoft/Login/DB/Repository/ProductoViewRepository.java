package com.dynasys.appdisoft.Login.DB.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.os.Handler;

import com.dynasys.appdisoft.Login.DB.AppDatabase;
import com.dynasys.appdisoft.Login.DB.Dao.ProductoViewDao;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoViewEntity;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProductoViewRepository {
    private ProductoViewDao mProductoViewDao;
    private LiveData<List<ProductoViewEntity>> mAllProductoView;

    public ProductoViewRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mProductoViewDao = db.productoViewDao();
        mAllProductoView = mProductoViewDao.getAllProductoView();
    }

    public LiveData<List<ProductoViewEntity>> getAllProductoView() {
        return mAllProductoView;
    }

    public ProductoViewEntity getProductoView(int noteId) throws ExecutionException, InterruptedException {
        return new ProductoViewRepository.getProductoViewAsync(mProductoViewDao).execute(noteId).get();
    }
    public List<ProductoViewEntity> getMProductoViewAll(int clienteId) throws ExecutionException, InterruptedException {
        return new ProductoViewRepository.getMProductoViewAllAsync(mProductoViewDao).execute(clienteId).get();
    }

    public void insertProductoView(ProductoViewEntity user) {
        new ProductoViewRepository.insertProductoViewAsync(mProductoViewDao).execute(user);
    }

    public void updateProductoView(ProductoViewEntity user) {
        new ProductoViewRepository.updateProductoViewAsync(mProductoViewDao).execute(user);
    }
    public void InsertProductoViewList(List<ProductoViewEntity > list){

        new ProductoViewRepository.insertListProductoViewAsync(mProductoViewDao).execute(list);
    }
    public void deleteProductoView(ProductoViewEntity user) {
        new ProductoViewRepository.deleteProductoViewAsync(mProductoViewDao).execute(user);
    }

    public void deleteAllProductoView() {
        new ProductoViewRepository.deleteAllProductoViewAsync(mProductoViewDao).execute();
    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */
    private class InsertarListaAsync extends AsyncTask<List<ProductoViewEntity>, String, String> {

        private ProductoViewDao mProductoViewDaoAsync;
        InsertarListaAsync(ProductoViewDao animalDao) {
            mProductoViewDaoAsync = animalDao;
        }



        @Override
        protected String doInBackground(List<ProductoViewEntity>... lists) {
            List<ProductoViewEntity> st=new ArrayList<>();
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
    private static class getProductoViewAsync extends AsyncTask<Integer, Void, ProductoViewEntity> {

        private ProductoViewDao mProductoViewDaoAsync;

        getProductoViewAsync(ProductoViewDao animalDao) {
            mProductoViewDaoAsync = animalDao;
        }

        @Override
        protected ProductoViewEntity doInBackground(Integer... ids) {
            return mProductoViewDaoAsync.getProductoViewProducto(ids[0]);
        }
    }


    private static class getMProductoViewAllAsync extends AsyncTask<Integer, Void, List<ProductoViewEntity>> {

        private ProductoViewDao mPedidoDaoAsync;

        getMProductoViewAllAsync(ProductoViewDao clienteDao) {
            mPedidoDaoAsync = clienteDao;
        }

        @Override
        protected List<ProductoViewEntity> doInBackground(Integer... ids) {
            return mPedidoDaoAsync.getProductoViewMAll();
        }
    }
    private static class insertProductoViewAsync extends AsyncTask<ProductoViewEntity, Void, Long> {

        private ProductoViewDao mProductoViewDaoAsync;

        insertProductoViewAsync(ProductoViewDao userDao) {
            mProductoViewDaoAsync = userDao;
        }

        @Override
        protected Long doInBackground(ProductoViewEntity... notes) {
            long id = mProductoViewDaoAsync.insert(notes[0]);
            return id;
        }
    }
    private static class insertListProductoViewAsync extends AsyncTask<List<ProductoViewEntity>, Void, Void> {

        private ProductoViewDao mProductoViewDaoAsync;

        insertListProductoViewAsync(ProductoViewDao userDao) {
            mProductoViewDaoAsync = userDao;
        }



        @Override
        protected Void doInBackground(List<ProductoViewEntity>... lists) {
            List<ProductoViewEntity> st=new ArrayList<>();
            st=lists[0];
            mProductoViewDaoAsync.insertList(st);
            return null;
        }
    }
    private static class updateProductoViewAsync extends AsyncTask<ProductoViewEntity, Void, Void> {

        private ProductoViewDao mProductoViewDaoAsync;

        updateProductoViewAsync(ProductoViewDao userDao) {
            mProductoViewDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(ProductoViewEntity... notes) {
            mProductoViewDaoAsync.update(notes[0]);
            return null;
        }
    }

    private static class deleteProductoViewAsync extends AsyncTask<ProductoViewEntity, Void, Void> {

        private ProductoViewDao mProductoViewDaoAsync;

        deleteProductoViewAsync(ProductoViewDao userDao) {
            mProductoViewDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(ProductoViewEntity... notes) {
            mProductoViewDaoAsync.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllProductoViewAsync extends AsyncTask<ProductoViewEntity, Void, Void> {

        private ProductoViewDao mProductoViewDaoAsync;

        deleteAllProductoViewAsync(ProductoViewDao ProductoViewDao) {
            mProductoViewDaoAsync = ProductoViewDao;
        }

        @Override
        protected Void doInBackground(ProductoViewEntity... notes) {
            mProductoViewDaoAsync.deleteAll();
            return null;
        }
    }
}
