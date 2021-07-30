package com.dynasys.appdisoft.Login.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dynasys.appdisoft.Login.DB.Entity.PointEntity;


import java.util.List;

@Dao
public interface PointDao {
    @Query("SELECT * FROM point ORDER BY idzona DESC")
    LiveData<List<PointEntity>> getAllpoint();

    @Query("SELECT * FROM point WHERE idzona=:id order by id asc")
    List<PointEntity> getpointProducto(int id);



    @Insert
    long insert(PointEntity point);

    @Insert
    void insertList(List<PointEntity> point);

    @Update
    void update(PointEntity point);


    @Delete
    void delete(PointEntity point);

    @Query("DELETE FROM point")
    void deleteAll();

    @Query("SELECT * FROM point")
    List<PointEntity> getpointMAll();
}
