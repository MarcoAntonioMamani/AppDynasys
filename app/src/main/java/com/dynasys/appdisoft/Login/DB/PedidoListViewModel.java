package com.dynasys.appdisoft.Login.DB;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PrecioEntity;

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

    public PedidoEntity getPedido(String id) throws ExecutionException, InterruptedException {
        return mRepository.getPedido(id);
    }
    public List<PedidoEntity> getMAllPedido(int code) throws ExecutionException, InterruptedException {
        return mRepository.getMPedidoAll(code);
    }
    public void insertPedido(PedidoEntity user) {
        mRepository.insertPedidos(user);
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
