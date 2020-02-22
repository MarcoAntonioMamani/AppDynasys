package com.dynasys.appdisoft.Login.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dynasys.appdisoft.Login.DB.Entity.UserEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ZonasEntity;

import java.util.List;

@Dao
public interface zonasDao {

    @Query("SELECT * FROM zona ORDER BY lanumi DESC")
    LiveData<List<ZonasEntity>> getAllZonas();

    @Query("SELECT * FROM zona WHERE idRepartidor=:idRepartidor")
    List<ZonasEntity> getZonasById(int idRepartidor);

    @Query("SELECT * FROM zona WHERE lanumi=:id")
    LiveData<ZonasEntity> getZona(int id);

    @Insert
    long insert(ZonasEntity note);

    @Update
    void update(ZonasEntity note);


    @Delete
    void delete(ZonasEntity note);

    @Query("DELETE FROM zona")
    void deleteAll();
}
