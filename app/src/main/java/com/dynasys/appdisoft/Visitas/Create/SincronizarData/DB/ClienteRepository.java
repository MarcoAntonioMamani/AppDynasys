package com.dynasys.appdisoft.Visitas.Create.SincronizarData.DB;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dynasys.appdisoft.Login.DB.AppDatabase;
import com.dynasys.appdisoft.Visitas.Create.SincronizarData.DB.Dao.ClientesDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClienteRepository {

    private ClientesDao mClienteDao;
    private LiveData<List<ClienteEntity>> mAllClientes;

    public ClienteRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mClienteDao = db.clientDao();
        mAllClientes = mClienteDao.getAllClientes();
    }

    public LiveData<List<ClienteEntity>> getAllClientes() {
        return mAllClientes;
    }

    public ClienteEntity getCliente(int clienteId) throws ExecutionException, InterruptedException {
        return new getUserAsync(mClienteDao).execute(clienteId).get();
    }
    public ClienteEntity getClienteNumi(int clienteId) throws ExecutionException, InterruptedException {
        return new getUserNumiAsync(mClienteDao).execute(clienteId).get();
    }
    public List<ClienteEntity> getMClienteAll(int clienteId) throws ExecutionException, InterruptedException {
        return new getCustomerAllAsync(mClienteDao).execute(clienteId).get();
    }
    public List<ClienteEntity> getMClienteAllState(int clienteId) throws ExecutionException, InterruptedException {
        return new getCustomerAllStateAsync(mClienteDao).execute(clienteId).get();
    }

    public List<ClienteEntity> getMClienteAllStateUpdate(int clienteId) throws ExecutionException, InterruptedException {
        return new getCustomerAllStateUpdateAsync(mClienteDao).execute(clienteId).get();
    }
    public ClienteEntity getClientebyCode(String code) throws ExecutionException, InterruptedException {
        return new getUserCodeAsync(mClienteDao).execute(code).get();
    }

    public void insertCliente(ClienteEntity user) {
        new insertClientesAsync(mClienteDao).execute(user);
    }
public void insertClienteList(List<ClienteEntity> cl){
        new insertListClienteAsync(mClienteDao).execute(cl);
}

    public void updateCliente(ClienteEntity user) {
        new updateClientesAsync(mClienteDao).execute(user);
    }

    public void updateListCliente(ClienteEntity[] user) {
        new updateListClientesAsync(mClienteDao).execute(user);
    }

    public void deleteCliente(ClienteEntity user) {
        new deleteClienteAsync(mClienteDao).execute(user);
    }

    public void deleteAllClientes() {
        new deleteAllClientesAsync(mClienteDao).execute();
    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */

    private static class getUserAsync extends AsyncTask<Integer, Void, ClienteEntity> {

        private ClientesDao mClienteDaoAsync;

        getUserAsync(ClientesDao clienteDao) {
            mClienteDaoAsync = clienteDao;
        }

        @Override
        protected ClienteEntity doInBackground(Integer... ids) {
            return mClienteDaoAsync.getClienteById(ids[0]);
        }
    }
    private static class getUserNumiAsync extends AsyncTask<Integer, Void, ClienteEntity> {

        private ClientesDao mClienteDaoAsync;

        getUserNumiAsync(ClientesDao clienteDao) {
            mClienteDaoAsync = clienteDao;
        }

        @Override
        protected ClienteEntity doInBackground(Integer... ids) {
            return mClienteDaoAsync.getClienteByNumi(ids[0]);
        }
    }
    private static class getCustomerAllAsync extends AsyncTask<Integer, Void, List<ClienteEntity>> {

        private ClientesDao mClienteDaoAsync;

        getCustomerAllAsync(ClientesDao clienteDao) {
            mClienteDaoAsync = clienteDao;
        }

        @Override
        protected List<ClienteEntity> doInBackground(Integer... ids) {
            return mClienteDaoAsync.getClienteAll();
        }
    }

    private static class getCustomerAllStateAsync extends AsyncTask<Integer, Void, List<ClienteEntity>> {

        private ClientesDao mClienteDaoAsync;

        getCustomerAllStateAsync(ClientesDao clienteDao) {
            mClienteDaoAsync = clienteDao;
        }

        @Override
        protected List<ClienteEntity> doInBackground(Integer... ids) {
            return mClienteDaoAsync.getClienteAllState();
        }
    }


    private static class getCustomerAllStateUpdateAsync extends AsyncTask<Integer, Void, List<ClienteEntity>> {

        private ClientesDao mClienteDaoAsync;

        getCustomerAllStateUpdateAsync(ClientesDao clienteDao) {
            mClienteDaoAsync = clienteDao;
        }

        @Override
        protected List<ClienteEntity> doInBackground(Integer... ids) {
            return mClienteDaoAsync.getClienteAllStateUpdate();
        }
    }
    private static class getUserCodeAsync extends AsyncTask<String, Void, ClienteEntity> {

        private ClientesDao mClienteDaoAsync;

        getUserCodeAsync(ClientesDao clienteDao) {
            mClienteDaoAsync = clienteDao;
        }

        @Override
        protected ClienteEntity doInBackground(String... code) {
            return mClienteDaoAsync.getClienteByCode(code[0]);
        }
    }
    private static class insertClientesAsync extends AsyncTask<ClienteEntity, Void, Long> {

        private ClientesDao mClienteDaoAsync;

        insertClientesAsync(ClientesDao userDao) {
            mClienteDaoAsync = userDao;
        }

        @Override
        protected Long doInBackground(ClienteEntity... notes) {
            long id = mClienteDaoAsync.insert(notes[0]);
            return id;
        }
    }
    private static class insertListClienteAsync extends AsyncTask<List<ClienteEntity>, Void, Void> {

        private ClientesDao mClienteDaoAsync;

        insertListClienteAsync(ClientesDao userDao) {
            mClienteDaoAsync = userDao;
        }



        @Override
        protected Void doInBackground(List<ClienteEntity>... lists) {
            List<ClienteEntity> st=new ArrayList<>();
            st=lists[0];
            mClienteDaoAsync.insertList(st);
            return null;
        }
    }

    private static class updateClientesAsync extends AsyncTask<ClienteEntity, Void, Void> {

        private ClientesDao mClienteDaoAsync;

        updateClientesAsync(ClientesDao clienteDao) {
            mClienteDaoAsync = clienteDao;
        }

        @Override
        protected Void doInBackground(ClienteEntity... clientes) {
            mClienteDaoAsync.update(clientes[0]);
            return null;
        }
    }
    private static class updateListClientesAsync extends AsyncTask<ClienteEntity, Void, Void> {

        private ClientesDao mClienteDaoAsync;

        updateListClientesAsync(ClientesDao clienteDao) {
            mClienteDaoAsync = clienteDao;
        }

        @Override
        protected Void doInBackground(ClienteEntity[] clientes) {
            mClienteDaoAsync.update(clientes);
            return null;
        }
    }
    private static class deleteClienteAsync extends AsyncTask<ClienteEntity, Void, Void> {

        private ClientesDao mClienteDaoAsync;

        deleteClienteAsync(ClientesDao userDao) {
            mClienteDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(ClienteEntity... clientes) {
            mClienteDaoAsync.delete(clientes[0]);
            return null;
        }
    }

    private static class deleteAllClientesAsync extends AsyncTask<ClienteEntity, Void, Void> {

        private ClientesDao mClienteDaoAsync;

        deleteAllClientesAsync(ClientesDao clientesDao) {
            mClienteDaoAsync = clientesDao;
        }

        @Override
        protected Void doInBackground(ClienteEntity... clientes) {
            mClienteDaoAsync.deleteAll();
            return null;
        }
    }
}
