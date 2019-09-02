package com.dynasys.appdisoft.SincronizarData.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dynasys.appdisoft.Login.DB.Entity.UserEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;

import java.util.List;

@Dao
public interface ClientesDao {
    @Query("SELECT * FROM clientes ORDER BY id DESC")
    LiveData<List<ClienteEntity>> getAllClientes();

    @Query("SELECT * FROM clientes WHERE id=:id")
    ClienteEntity getClienteById(int id);

    @Query("SELECT * FROM clientes WHERE numi=:id")
    ClienteEntity getClienteByNumi(int id);
    @Query("SELECT * FROM clientes")
    List<ClienteEntity> getClienteAll();


    @Query("SELECT * FROM clientes where estado=0")
    List<ClienteEntity> getClienteAllState();

    @Query("SELECT * FROM clientes where estado=2")
    List<ClienteEntity> getClienteAllStateUpdate();


    @Query("SELECT * FROM clientes WHERE codigogenerado=:code")
    ClienteEntity getClienteByCode(String code);

    @Query("SELECT * FROM clientes WHERE id=:id")
    LiveData<ClienteEntity> getCliente(int id);

    @Insert
    long insert(ClienteEntity note);


    @Update
    void update(ClienteEntity note);
    @Update
    void update(ClienteEntity[] note);

    @Delete
    void delete(ClienteEntity note);

    @Query("DELETE FROM clientes")
    void deleteAll();
}
