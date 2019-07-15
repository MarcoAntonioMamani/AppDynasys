package com.dynasys.appdisoft.Login.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.dynasys.appdisoft.Login.DB.Converter.DateConverter;
import com.dynasys.appdisoft.Login.DB.Dao.DetalleDao;
import com.dynasys.appdisoft.Login.DB.Dao.PedidoDao;
import com.dynasys.appdisoft.Login.DB.Dao.PrecioDao;
import com.dynasys.appdisoft.Login.DB.Dao.ProductoDao;
import com.dynasys.appdisoft.Login.DB.Dao.UserDao;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PrecioEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.UserEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.Dao.ClientesDao;


/**
 * Created by ravi on 05/02/18.
 */

@Database(entities = {UserEntity.class, ClienteEntity.class, PrecioEntity.class, ProductoEntity.class, PedidoEntity.class, DetalleEntity.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract ClientesDao clientDao();
    public abstract PrecioDao precioDao();
    public abstract ProductoDao productoDao();
    public abstract PedidoDao pedidoDao();
    public abstract DetalleDao detalleDao();
    private static AppDatabase INSTANCE;

   public  static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "disoft_database")
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}