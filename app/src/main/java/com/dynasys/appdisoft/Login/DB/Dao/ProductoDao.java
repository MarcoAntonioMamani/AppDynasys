package com.dynasys.appdisoft.Login.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;

import java.util.List;

@Dao
public interface ProductoDao {

    @Query("SELECT * FROM producto ORDER BY numi DESC")
    LiveData<List<ProductoEntity>> getAllProductos();

    @Query("SELECT * FROM producto ORDER BY numi DESC")
    List<ProductoEntity> getAllMProductos();

    @Query("SELECT * FROM producto WHERE numi=:numi")
    ProductoEntity getProductoById(int numi);

    @Query("SELECT * FROM producto WHERE numi=:numi")
    LiveData<ProductoEntity> getProducto(int numi);

    @Insert
    long insert(ProductoEntity note);

    @Update
    void update(ProductoEntity note);

    @Delete
    void delete(ProductoEntity note);

    @Query("DELETE FROM producto")
    void deleteAll();
}
