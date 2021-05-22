package com.dynasys.appdisoft.Login.DB.Dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dynasys.appdisoft.Login.DB.Entity.CategoriaPrecioEntity;

import java.util.List;

@Dao
public interface CategoriaPrecioDao {

    @Query("SELECT * FROM CategoriaPrecio ORDER BY id Asc")
    LiveData<List<CategoriaPrecioEntity>> getAllCategoriaPrecio();

    @Query("SELECT * FROM CategoriaPrecio WHERE id=:id ")
    CategoriaPrecioEntity getCategoriaPrecioProducto(int id);



    @Insert
    long insert(CategoriaPrecioEntity CategoriaPrecio);

    @Insert
    void insertList(List<CategoriaPrecioEntity> CategoriaPrecio);

    @Update
    void update(CategoriaPrecioEntity CategoriaPrecio);


    @Delete
    void delete(CategoriaPrecioEntity CategoriaPrecio);

    @Query("DELETE FROM CategoriaPrecio")
    void deleteAll();

    @Query("SELECT * FROM CategoriaPrecio order by id asc")
    List<CategoriaPrecioEntity> getCategoriaPrecioMAll();
}
