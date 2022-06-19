package com.dynasys.appdisoft.Login.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dynasys.appdisoft.Login.DB.Entity.CobranzaDetalleEntity;

import java.util.List;

@Dao
public interface CobranzaDetalleDao {


    @Query("SELECT * FROM cobranzaDetalle ORDER BY tdnumi DESC")
    LiveData<List<CobranzaDetalleEntity>> getAllcobranzaDetalle();

    @Query("SELECT * FROM cobranzaDetalle WHERE cobranzaId=:id")
    List<CobranzaDetalleEntity> getcobranzaDetalleByPedido(String id);



    @Insert
    long insert(CobranzaDetalleEntity cobranzaDetalle);

    @Update
    void update(CobranzaDetalleEntity cobranzaDetalle);


    @Delete
    void delete(CobranzaDetalleEntity cobranzaDetalle);

    @Query("DELETE FROM cobranzaDetalle")
    void deleteAll();

    @Query("SELECT * FROM cobranzaDetalle")
    List<CobranzaDetalleEntity> getcobranzaDetalleMAll();
}
