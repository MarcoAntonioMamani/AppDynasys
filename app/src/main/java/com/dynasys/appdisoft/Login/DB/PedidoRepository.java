package com.dynasys.appdisoft.Login.DB;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dynasys.appdisoft.Login.DB.Dao.PedidoDao;
import com.dynasys.appdisoft.Login.DB.Dao.PrecioDao;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PrecioEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class PedidoRepository {

    private PedidoDao mPedidoDao;
    private LiveData<List<PedidoEntity>> mAllPrecio;
    private LiveData<List<PedidoEntity>> mAllPedidoEntregados;

    public PedidoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mPedidoDao = db.pedidoDao();
        mAllPrecio = mPedidoDao.getAllPedidos();
        mAllPedidoEntregados = mPedidoDao.getAllPedidosEntregados();
    }
    public List<PedidoEntity> getMPedidoAll(int clienteId) throws ExecutionException, InterruptedException {
        return new getMPedidoAllAsync(mPedidoDao).execute(clienteId).get();
    }
    public List<PedidoEntity> getMPedidoAllState(int clienteId) throws ExecutionException, InterruptedException {
        return new getMPedidoAllStateAsync(mPedidoDao).execute(clienteId).get();
    }
    public List<PedidoEntity> getMPedidoAllSinStock(int clienteId) throws ExecutionException, InterruptedException {
        return new getMPedidoAllSinStockAsync(mPedidoDao).execute(clienteId).get();
    }
    public List<PedidoEntity> getMPedidoAllState02(int clienteId) throws ExecutionException, InterruptedException {
        return new getMPedidoAllState02Async(mPedidoDao).execute(clienteId).get();
    }
    public LiveData<List<PedidoEntity>> getAllPedido() {
        return mAllPrecio;
    }
    public LiveData<List<PedidoEntity>> getAllPedidoEntregados() {
        return mAllPedidoEntregados;
    }
    public PedidoEntity getPedido(String noteId) throws ExecutionException, InterruptedException {
        return new getPedidosAsync(mPedidoDao).execute(noteId).get();
    }

    public List<PedidoEntity> getPedidoState(String noteId) throws ExecutionException, InterruptedException {
        return new getPedidosStateAsync(mPedidoDao).execute(noteId).get();
    }
    public List<PedidoEntity> getPedidoByClients(String noteId) throws ExecutionException, InterruptedException {
        return new getPedidosByClienteAsync(mPedidoDao).execute(noteId).get();
    }
    public void insertPedidos(PedidoEntity user) {
        new insertPedidoAsync(mPedidoDao).execute(user);
    }

    public void updatePedido(PedidoEntity user) {
        new updatePedidoAsync(mPedidoDao).execute(user);
    }
    public void updateListPedidos(PedidoEntity[] user) {
        new updateListPedidoAsync(mPedidoDao).execute(user);
    }

    public void deletePedidos(PedidoEntity user) {
        new deletePedidoAsync(mPedidoDao).execute(user);
    }

    public void deleteAllPedidos() {
       // new deleteAllPedidoAsync(mPedidoDao).execute();
try {
    new deleteAllPedidoAsync(mPedidoDao).execute();
}catch (Exception e){
    Object obj=e;
}

    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */
        private static class getMPedidoAllAsync extends AsyncTask<Integer, Void, List<PedidoEntity>> {

        private PedidoDao mPedidoDaoAsync;

        getMPedidoAllAsync(PedidoDao clienteDao) {
            mPedidoDaoAsync = clienteDao;
        }

        @Override
        protected List<PedidoEntity> doInBackground(Integer... ids) {
            return mPedidoDaoAsync.getPedidoAll();
        }
    }

    private static class getMPedidoAllStateAsync extends AsyncTask<Integer, Void, List<PedidoEntity>> {

        private PedidoDao mPedidoDaoAsync;

        getMPedidoAllStateAsync(PedidoDao clienteDao) {
            mPedidoDaoAsync = clienteDao;
        }

        @Override
        protected List<PedidoEntity> doInBackground(Integer... ids) {
            return mPedidoDaoAsync.getPedidoAllState();
        }
    }
    private static class getMPedidoAllSinStockAsync extends AsyncTask<Integer, Void, List<PedidoEntity>> {

        private PedidoDao mPedidoDaoAsync;

        getMPedidoAllSinStockAsync(PedidoDao clienteDao) {
            mPedidoDaoAsync = clienteDao;
        }

        @Override
        protected List<PedidoEntity> doInBackground(Integer... ids) {
            return mPedidoDaoAsync.getPedidoAllStateSinStock();
        }
    }
    private static class getMPedidoAllState02Async extends AsyncTask<Integer, Void, List<PedidoEntity>> {

        private PedidoDao mPedidoDaoAsync;

        getMPedidoAllState02Async(PedidoDao clienteDao) {
            mPedidoDaoAsync = clienteDao;
        }

        @Override
        protected List<PedidoEntity> doInBackground(Integer... ids) {
            return mPedidoDaoAsync.getPedidoAllState02();
        }
    }
    private static class getPedidosAsync extends AsyncTask<String, Void, PedidoEntity> {

        private PedidoDao mPedidoDaoAsync;

        getPedidosAsync(PedidoDao animalDao) {
            mPedidoDaoAsync = animalDao;
        }

        @Override
        protected PedidoEntity doInBackground(String... ids) {
            return mPedidoDaoAsync.getPedidoById(ids[0]);
        }
    }

    private static class getPedidosStateAsync extends AsyncTask<String, Void, List<PedidoEntity>> {

        private PedidoDao mPedidoDaoAsync;

        getPedidosStateAsync(PedidoDao animalDao) {
            mPedidoDaoAsync = animalDao;
        }

        @Override
        protected List<PedidoEntity> doInBackground(String... ids) {
            return mPedidoDaoAsync.getPedidoByCodeClienteForstate(ids[0]);
        }
    }
    private static class getPedidosByClienteAsync extends AsyncTask<String, Void, List<PedidoEntity>> {

        private PedidoDao mPedidoDaoAsync;

        getPedidosByClienteAsync(PedidoDao animalDao) {
            mPedidoDaoAsync = animalDao;
        }

        @Override
        protected List<PedidoEntity> doInBackground(String... ids) {
            return mPedidoDaoAsync.getPedidoByIdCliente(ids[0]);
        }
    }
    private static class insertPedidoAsync extends AsyncTask<PedidoEntity, Void, Long> {

        private PedidoDao mPedidoDaoAsync;

        insertPedidoAsync(PedidoDao userDao) {
            mPedidoDaoAsync = userDao;
        }

        @Override
        protected Long doInBackground(PedidoEntity... notes) {
            long id = mPedidoDaoAsync.insert(notes[0]);
            return id;
        }
    }

    private static class updatePedidoAsync extends AsyncTask<PedidoEntity, Void, Void> {

        private PedidoDao mPedidoDaoAsync;

        updatePedidoAsync(PedidoDao userDao) {
            mPedidoDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(PedidoEntity... notes) {
            mPedidoDaoAsync.update(notes[0]);
            return null;
        }
    }
    private static class updateListPedidoAsync extends AsyncTask<PedidoEntity, Void, Void> {

        private PedidoDao mPedidoDaoAsync;

        updateListPedidoAsync(PedidoDao userDao) {
            mPedidoDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(PedidoEntity[] notes) {
            mPedidoDaoAsync.update(notes);
            return null;
        }
    }
    private static class deletePedidoAsync extends AsyncTask<PedidoEntity, Void, Void> {

        private PedidoDao mPedidoDaoAsync;

        deletePedidoAsync(PedidoDao userDao) {
            mPedidoDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(PedidoEntity... notes) {
            mPedidoDaoAsync.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllPedidoAsync extends AsyncTask<PedidoEntity, Void, Void> {

        private PedidoDao mPedidoDaoAsync;

        deleteAllPedidoAsync(PedidoDao productoDao) {
            mPedidoDaoAsync = productoDao;
        }

        @Override
        protected Void doInBackground(PedidoEntity... notes) {
            mPedidoDaoAsync.deleteAll();
            return null;
        }
    }
}
