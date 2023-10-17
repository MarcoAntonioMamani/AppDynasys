package com.dynasys.appdisoft.Login.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dynasys.appdisoft.Login.DB.Entity.PrecioEntity;

import java.util.List;

@Dao
public interface PrecioDao {

    @Query("SELECT * FROM precio ORDER BY chnumi DESC")
    LiveData<List<PrecioEntity>> getAllPrecios();
    @Query("SELECT * FROM precio")
    List<PrecioEntity> getPrecioAll();

    @Query("SELECT * FROM precio WHERE chnumi=:numi")
    PrecioEntity getPrecioById(int numi);

    @Query("SELECT * FROM precio WHERE chnumi=:numi")
    LiveData<PrecioEntity> getPrecio(int numi);

    @Insert
    long insert(PrecioEntity note);

    @Insert
    void insertList(List<PrecioEntity> stock);
    @Update
    void update(PrecioEntity note);

    @Update
    void update(PrecioEntity... note);

    @Delete
    void delete(PrecioEntity note);

    @Query("DELETE FROM precio")
    void deleteAll();
}
