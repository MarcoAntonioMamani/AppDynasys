package com.dynasys.appdisoft.Login.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dynasys.appdisoft.Login.DB.Entity.DeudaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;

import java.util.List;

@Dao
public interface DeudadDao {


    @Query("SELECT * FROM deuda  ORDER BY PedidoId  DESC")
    LiveData<List<DeudaEntity>> getAllDeuda();

    @Query("SELECT * FROM deuda  WHERE PedidoId =:id")
    DeudaEntity getDeudaPedido(int id);

    @Insert
    long insert(DeudaEntity stock);

    @Update
    void update(DeudaEntity stock);


    @Delete
    void delete(DeudaEntity stock);

    @Query("DELETE FROM deuda")
    void deleteAll();

    @Query("SELECT * FROM deuda")
    List<DeudaEntity> getDeudaMAll();
}
