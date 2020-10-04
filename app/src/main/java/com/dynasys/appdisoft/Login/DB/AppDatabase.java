package com.dynasys.appdisoft.Login.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dynasys.appdisoft.Login.DB.Converter.DateConverter;
import com.dynasys.appdisoft.Login.DB.Dao.CobranzaDao;
import com.dynasys.appdisoft.Login.DB.Dao.CobranzaDetalleDao;
import com.dynasys.appdisoft.Login.DB.Dao.DescuentosDao;
import com.dynasys.appdisoft.Login.DB.Dao.DetalleDao;
import com.dynasys.appdisoft.Login.DB.Dao.DeudadDao;
import com.dynasys.appdisoft.Login.DB.Dao.PedidoDao;
import com.dynasys.appdisoft.Login.DB.Dao.PrecioDao;
import com.dynasys.appdisoft.Login.DB.Dao.ProductoDao;
import com.dynasys.appdisoft.Login.DB.Dao.StockDao;
import com.dynasys.appdisoft.Login.DB.Dao.UserDao;
import com.dynasys.appdisoft.Login.DB.Dao.zonasDao;
import com.dynasys.appdisoft.Login.DB.Entity.CobranzaDetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.CobranzaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DescuentosEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.DeudaEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PedidoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.PrecioEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ProductoEntity;
import com.dynasys.appdisoft.Login.DB.Entity.StockEntity;
import com.dynasys.appdisoft.Login.DB.Entity.UserEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ZonasEntity;
import com.dynasys.appdisoft.SincronizarData.DB.ClienteEntity;
import com.dynasys.appdisoft.SincronizarData.DB.Dao.ClientesDao;


/**
 * Created by ravi on 05/02/18.
 */

@Database(entities = {UserEntity.class, ClienteEntity.class, PrecioEntity.class, CobranzaDetalleEntity.class, ProductoEntity.class, PedidoEntity.class, DetalleEntity.class, StockEntity.class, ZonasEntity.class, DescuentosEntity.class, DeudaEntity.class, CobranzaEntity.class}, version = 14)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract ClientesDao clientDao();
    public abstract PrecioDao precioDao();
    public abstract ProductoDao productoDao();
    public abstract PedidoDao pedidoDao();
    public abstract DetalleDao detalleDao();
    public abstract StockDao stockDao();
    public abstract zonasDao ZonasDao();
    public abstract DescuentosDao DescuentoDao();
    public abstract DeudadDao DeudaDao();
    public abstract CobranzaDao CobranzaDao();
    public abstract CobranzaDetalleDao CobranzaDetalleDao();
    public static AppDatabase INSTANCE;

   public  static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "disoftCF_database")
                            .fallbackToDestructiveMigration()
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
       INSTANCE.clearAllTables();
        INSTANCE = null;
    }

}