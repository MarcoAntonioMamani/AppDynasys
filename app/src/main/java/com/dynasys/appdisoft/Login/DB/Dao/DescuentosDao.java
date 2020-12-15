package com.dynasys.appdisoft.Login.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dynasys.appdisoft.Login.DB.Entity.DescuentosEntity;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;

import java.util.List;

@Dao
public interface DescuentosDao {

    @Query("SELECT * FROM descuentos ORDER BY id DESC")
    LiveData<List<DescuentosEntity>> getAllDescuentos();

    @Query("SELECT * FROM descuentos ORDER BY id DESC")
    List<DescuentosEntity> getAllMDescuentos();



    @Query("SELECT * FROM descuentos WHERE id=:id")
    DescuentosEntity getDescuentoById(int id);

    @Query("SELECT * FROM descuentos WHERE idProducto=:idProducto")
    List<DescuentosEntity> getDescuento(int idProducto);

    @Insert
    long insert(DescuentosEntity note);
    @Insert
    void insertList(List<DescuentosEntity> stock);
    @Update
    void update(DescuentosEntity note);

    @Delete
    void delete(DescuentosEntity note);

    @Query("DELETE FROM descuentos")
    void deleteAll();
}
