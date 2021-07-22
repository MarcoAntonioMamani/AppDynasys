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

    @Query("SELECT  id,productoid,nombreProducto,cantInicial,cantFinal,precio,(select r.cantidad  from (select  MAx(st.id),st.cantidad   from stock as st where st.codigoProducto=productoId) as r ) as stock,entrada," +
            "xentrada,rebote,aut,saldo" +
            " FROM ProductoView ORDER BY ProductoId DESC ")
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

    @Query("SELECT id,productoid,nombreProducto,cantInicial,cantFinal,precio,(select r.cantidad  from (select  MAx(st.id),st.cantidad   from stock as st where st.codigoProducto=productoId) as r ) as stock,entrada," +
            "xentrada,rebote,aut,saldo FROM ProductoView")
    List<ProductoViewEntity> getProductoViewMAll();
}
