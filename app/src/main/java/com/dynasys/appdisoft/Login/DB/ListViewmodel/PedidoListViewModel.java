package com.dynasys.appdisoft.Login.DB.ListViewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Repository.PedidoRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PedidoListViewModel extends AndroidViewModel {

    private PedidoRepository mRepository;
    private LiveData<List<PedidoEntity>> pedidos;

    public PedidoListViewModel(@NonNull Application application) {
        super(application);

        mRepository = new PedidoRepository(application);
    }

    public LiveData<List<PedidoEntity>> getPedidos() {
        if (pedidos == null) {
            pedidos = mRepository.getAllPedido();
        }

        return pedidos;
    }
    public LiveData<List<PedidoEntity>> getPedidosEntregados() {
        if (pedidos == null) {
            pedidos = mRepository.getAllPedidoEntregados();
        }

        return pedidos;
    }
    public PedidoEntity getPedido(String id) {
        try {
            return mRepository.getPedido(id);
        } catch (ExecutionException e) {

        } catch (InterruptedException e) {

        }
        return null;
    }
    public PedidoEntity getPedidoCodeGenerado(String id) {
        try {
            return mRepository.getPedidoCodeGenerado(id);
        } catch (ExecutionException e) {

        } catch (InterruptedException e) {

        }
        return null;
    }

    public List<PedidoEntity> getPedidoState(String id) throws ExecutionException, InterruptedException {
        return mRepository.getPedidoState(id);
    }
    public List<PedidoEntity> getPedidobyCliente(String id) throws ExecutionException, InterruptedException {
        return mRepository.getPedidoByClients(id);
    }
    public List<PedidoEntity> getMAllPedido(int code) throws ExecutionException, InterruptedException {
        return mRepository.getMPedidoAll(code);
    }
    public List<PedidoEntity> getMAllPedidoState(int code) throws ExecutionException, InterruptedException {
        return mRepository.getMPedidoAllState(code);
    }
    public List<PedidoEntity> getMAllPedidoSinStock(int code) throws ExecutionException, InterruptedException {
        return mRepository.getMPedidoAllSinStock(code);
    }
    public List<PedidoEntity> getMAllPedidoState02(int code) throws ExecutionException, InterruptedException {
        return mRepository.getMPedidoAllState02(code);
    }
    public void insertPedido(PedidoEntity user) {
        mRepository.insertPedidos(user);
    }
//insertPedidosList

    public void insertPedidosList(List<PedidoEntity> user) {
        mRepository.insertPedidosList(user);
    }

    public void updatePedido(PedidoEntity user) {
        mRepository.updatePedido(user);
    }
    public void updateListPedido(PedidoEntity[] user) {
        mRepository.updateListPedidos(user);
    }
    public void deletePedido(PedidoEntity user) {
        mRepository.deletePedidos(user);
    }

    public void deleteAllPedido() {
        mRepository.deleteAllPedidos();
    }
}