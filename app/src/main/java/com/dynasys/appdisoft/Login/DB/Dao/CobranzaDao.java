package com.dynasys.appdisoft.Login.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.dynasys.appdisoft.Login.DB.Entity.CobranzaEntity;

import java.util.List;

@Dao
public interface CobranzaDao {

    @Query("SELECT * FROM cobranza ORDER BY tenumi DESC")
    LiveData<List<CobranzaEntity>> getAllcobranza();

    @Query("SELECT * FROM cobranza WHERE tenumi=:id")
    CobranzaEntity getcobranza(int id);



    @Insert
    long insert(CobranzaEntity cobranza);

    @Update
    void update(CobranzaEntity cobranza);


    @Delete
    void delete(CobranzaEntity cobranza);

    @Query("DELETE FROM cobranza")
    void deleteAll();

    @Query("SELECT * FROM cobranza")
    List<CobranzaEntity> getcobranzaMAll();
}
