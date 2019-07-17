package com.dynasys.appdisoft.Login.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PrecioEntity;

import java.util.List;

@Dao
public interface PedidoDao {

    @Query("SELECT * FROM pedido ORDER BY oanumi DESC")
    LiveData<List<PedidoEntity>> getAllPedidos();
    @Query("SELECT * FROM pedido")
    List<PedidoEntity> getPedidoAll();

    @Query("SELECT * FROM pedido WHERE oanumi=:numi")
    PedidoEntity getPedidoById(String numi);

    @Query("SELECT * FROM pedido WHERE oanumi=:numi")
    LiveData<PedidoEntity> getPedido(int numi);

    @Insert
    long insert(PedidoEntity note);


    @Update
    void update(PedidoEntity note);

    @Update
    void update(PedidoEntity... note);

    @Delete
    void delete(PedidoEntity note);

    @Query("DELETE FROM pedido")
    void deleteAll();
}
