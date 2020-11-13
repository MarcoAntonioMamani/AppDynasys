package com.dynasys.appdisoft.Login.DB.Dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;
import com.dynasys.appdisoft.Login.DB.Entity.UserEntity;

import java.util.List;

@Dao
public interface StockDao {

    @Query("SELECT * FROM stock ORDER BY codigoProducto DESC")
    LiveData<List<StockEntity>> getAllStock();

    @Query("SELECT * FROM stock WHERE codigoProducto=:id")
    StockEntity getStockProducto(int id);



    @Insert
    long insert(StockEntity stock);

    @Insert
    void insertList(List<StockEntity> stock);

    @Update
    void update(StockEntity stock);


    @Delete
    void delete(StockEntity stock);

    @Query("DELETE FROM stock")
    void deleteAll();

    @Query("SELECT * FROM stock")
    List<StockEntity> getStockMAll();
}
