package com.dynasys.appdisoft.Login.DB.Dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dynasys.appdisoft.Login.DB.Entity.VisitaEntity;

import java.util.List;

@Dao
public interface VisitaDao {

    @Query("SELECT * FROM Visita ORDER BY id DESC")
    LiveData<List<VisitaEntity>> getAllVisita();

    @Query("SELECT * FROM Visita WHERE id=:id")
    VisitaEntity getVisitaProducto(int id);

    @Query("SELECT * FROM visita WHERE IdSincronizacion=:code")
    VisitaEntity getVisitaByCode(String code);

    @Query("SELECT * FROM visita WHERE PedidoId=:code")
    VisitaEntity getVisitaByPedidoId(String code);

    @Query("SELECT * FROM visita WHERE ClienteId=:numi")
    List<VisitaEntity> getVisitaByIdCliente(String numi);


    @Query("SELECT * FROM visita where Sincronizado=2")
    List<VisitaEntity> getVisitaAllStateUpdate();

    @Query("SELECT * FROM visita where Sincronizado=0")
    List<VisitaEntity> getVisitaAllState();

    @Query("SELECT * FROM visita ")
    List<VisitaEntity> getVisitaAll();

    @Insert
    long insert(VisitaEntity Visita);

    @Insert
    void insertList(List<VisitaEntity> Visita);

    @Update
    void update(VisitaEntity Visita);


    @Delete
    void delete(VisitaEntity Visita);

    @Query("DELETE FROM Visita")
    void deleteAll();

    @Query("SELECT * FROM Visita")
    List<VisitaEntity> getVisitaMAll();
}
