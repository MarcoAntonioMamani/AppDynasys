package com.dynasys.appdisoft.Login.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dynasys.appdisoft.Login.DB.Entity.DeudaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;

import java.util.List;

@Dao
public interface DeudadDao {


    @Query("SELECT id,PedidoId,ClienteId,cliente,direccion,telefono,limiteCliente,PersonalId,vendedor,FechaPedido," +
            "totalfactura,pendiente,1 as estado,t.EstadoCredito,t.Mora,t.Factura,0 as totalAPagar FROM deuda as t   ORDER BY PedidoId  DESC")
    LiveData<List<DeudaEntity>> getAllDeuda();

    @Query("SELECT * FROM deuda  WHERE PedidoId =:id")
    DeudaEntity getDeudaPedido(int id);

    @Insert
    long insert(DeudaEntity stock);

    @Update
    void update(DeudaEntity stock);


    @Delete
    void delete(DeudaEntity stock);

    @Query("DELETE FROM deuda")
    void deleteAll();

    @Query("SELECT * FROM deuda where pendiente>0")
    List<DeudaEntity> getDeudaMAll();
}
