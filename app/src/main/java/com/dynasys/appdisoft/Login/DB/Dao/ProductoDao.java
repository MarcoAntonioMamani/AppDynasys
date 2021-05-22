package com.dynasys.appdisoft.Login.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;

import java.util.List;

@Dao
public interface ProductoDao {

    @Query("SELECT * FROM producto ORDER BY numi DESC")
    LiveData<List<ProductoEntity>> getAllProductos();

    @Query("SELECT * FROM producto ORDER BY numi DESC")
    List<ProductoEntity> getAllMProductos();

    @Query("SELECT distinct p.numi,p.cod,p.producto,p.desccorta,p.idcategoria,p.categoria,precio.chprecio as precio, (" +
            "select r.cantidad  from (select  MAx(st.id),st.cantidad   from stock as st where st.codigoProducto=p.numi) as r ) as stock " +
            ",p.familia,p.conversion " +
            "FROM producto as p inner join precio on precio.chcprod =p.numi " +
            " WHERE precio.chcatcl=:numi and precio.chprecio >0")
    List<ProductoEntity> getProductoByCliente(int numi);

    @Query("SELECT * FROM producto WHERE numi=:numi")
    ProductoEntity getProductoById(int numi);


    @Query("SELECT distinct  p.numi,p.cod,p.producto,p.desccorta,p.idcategoria,p.categoria,0 as precio, (" +
            "select r.cantidad  from (select  MAx(st.id),st.cantidad   from stock as st where st.codigoProducto=p.numi) as r ) as stock " +
            ",p.familia,p.conversion " +
            "FROM producto as p   " +
            " WHERE p.numi=:numi  limit 1")
    ProductoEntity getProductoByStockId(int numi);

    @Query("SELECT * FROM producto WHERE numi=:numi")
    LiveData<ProductoEntity> getProducto(int numi);

    @Insert
    long insert(ProductoEntity note);
    @Insert
    void insertList(List<ProductoEntity> stock);
    @Update
    void update(ProductoEntity note);

    @Delete
    void delete(ProductoEntity note);

    @Query("DELETE FROM producto")
    void deleteAll();
}
