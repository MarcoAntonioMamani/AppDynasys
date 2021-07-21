package com.dynasys.appdisoft.Login.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dynasys.appdisoft.Login.DB.Entity.ProductoViewEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoViewEntity;

import java.util.List;

@Dao
public interface ProductoViewDao {

    @Query("SELECT * FROM ProductoView ORDER BY ProductoId DESC")
    LiveData<List<ProductoViewEntity>> getAllProductoView();

    @Query("SELECT * FROM ProductoView WHERE ProductoId=:id")
    ProductoViewEntity getProductoViewProducto(int id);



    @Insert
    long insert(ProductoViewEntity ProductoView);

    @Insert
    void insertList(List<ProductoViewEntity> ProductoView);

    @Update
    void update(ProductoViewEntity ProductoView);


    @Delete
    void delete(ProductoViewEntity ProductoView);

    @Query("DELETE FROM ProductoView")
    void deleteAll();

    @Query("SELECT * FROM ProductoView")
    List<ProductoViewEntity> getProductoViewMAll();
}
