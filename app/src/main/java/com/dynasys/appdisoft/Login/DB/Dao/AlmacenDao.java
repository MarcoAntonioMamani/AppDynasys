package com.dynasys.appdisoft.Login.DB.Dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dynasys.appdisoft.Login.DB.Entity.AlmacenEntity;

import java.util.List;

@Dao
public interface AlmacenDao {

    @Query("SELECT * FROM almacen ORDER BY ProductoId DESC")
    LiveData<List<AlmacenEntity>> getAllAlmacen();

    @Query("SELECT * FROM almacen WHERE ProductoId=:id")
    AlmacenEntity getAlmacenProducto(int id);



    @Insert
    long insert(AlmacenEntity almacen);

    @Update
    void update(AlmacenEntity almacen);


    @Delete
    void delete(AlmacenEntity almacen);

    @Query("DELETE FROM almacen")
    void deleteAll();

    @Query("SELECT * FROM almacen")
    List<AlmacenEntity> getAlmacenMAll();
}
