package com.dynasys.appdisoft.Login.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;

import java.util.List;

@Dao
public interface DetalleDao {

    @Query("SELECT * FROM detalle ORDER BY obnumi DESC")
    LiveData<List<DetalleEntity>> getAllDetalle();
    @Query("SELECT * FROM detalle")
    List<DetalleEntity> getDetalleAll();


    @Query("SELECT * FROM detalle where estado=0")
    List<DetalleEntity> getDetalleAllState();

    @Query("SELECT id,obnumi,obcprod,cadesc,obpcant," +
            "obpbase,obptot,descuento,total,familia,estado,obupdate,((select r.cantidad  from (select  MAx(st.id),st.cantidad   " +
            "from stock as st where st.codigoProducto=obcprod) as r )+(Case  When detalle.estado=0 Then 0 Else obpcant end))as stock,cajas,conversion  FROM detalle WHERE obnumi=:numi")
    List<DetalleEntity> getDetalleById(String numi);

    @Query("SELECT * FROM detalle WHERE obnumi=:numi and obcprod=:producto")
    DetalleEntity getDetalleByIdProducto(int numi,int producto);

    @Query("SELECT * FROM detalle WHERE obnumi=:numi")
    LiveData<DetalleEntity> getDetalle(int numi);

    @Insert
    long insert(DetalleEntity note);

    @Insert
    void insertList(List<DetalleEntity> stock);
    @Update
    void update(DetalleEntity note);

    @Update
    void update(DetalleEntity... note);

    @Delete
    void delete(DetalleEntity note);

    @Query("DELETE FROM detalle")
    void deleteAll();
}
