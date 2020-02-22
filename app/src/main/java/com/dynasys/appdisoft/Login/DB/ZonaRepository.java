package com.dynasys.appdisoft.Login.DB;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dynasys.appdisoft.Login.DB.Dao.DetalleDao;
import com.dynasys.appdisoft.Login.DB.Dao.zonasDao;
import com.dynasys.appdisoft.Login.DB.Entity.DetalleEntity;
import com.dynasys.appdisoft.Login.DB.Entity.ZonasEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ZonaRepository {

    private zonasDao mZonaDao;
    private LiveData<List<ZonasEntity>> mAllZona;

    public ZonaRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mZonaDao = db.ZonasDao();
        mAllZona = mZonaDao.getAllZonas();
    }


    public List<ZonasEntity> getZonaById(Integer id) throws ExecutionException, InterruptedException {
        return new ZonaRepository.getZonasByIdAsync(mZonaDao).execute(id).get();
    }



    public void insertZonas(ZonasEntity user) {
        new ZonaRepository.insertZonaAsync(mZonaDao).execute(user);
    }

    public void updateZona(ZonasEntity user) {
        new ZonaRepository.updateZonaAsync(mZonaDao).execute(user);
    }


    public void deleteZona(ZonasEntity user) {
        new ZonaRepository.deleteZonaAsync(mZonaDao).execute(user);
    }

    public void deleteAllDetalles() {
        new ZonaRepository.deleteAllZonasAsync(mZonaDao).execute();
    }



    private static class getZonasByIdAsync extends AsyncTask<Integer, Void, List<ZonasEntity>> {

        private zonasDao mZonasDaoAsync;

        getZonasByIdAsync(zonasDao animalDao) {
            mZonasDaoAsync = animalDao;
        }

        @Override
        protected List<ZonasEntity> doInBackground(Integer... ids) {
            return mZonasDaoAsync.getZonasById(ids[0]);
        }


    }

    private static class insertZonaAsync extends AsyncTask<ZonasEntity, Void, Long> {

        private zonasDao mZonasDaoAsync;

        insertZonaAsync(zonasDao userDao) {
            mZonasDaoAsync = userDao;
        }

        @Override
        protected Long doInBackground(ZonasEntity... notes) {
            long id = mZonasDaoAsync.insert(notes[0]);
            return id;
        }
    }

    private static class updateZonaAsync extends AsyncTask<ZonasEntity, Void, Void> {

        private zonasDao mZonasDaoAsync;

        updateZonaAsync(zonasDao userDao) {
            mZonasDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(ZonasEntity... notes) {
            mZonasDaoAsync.update(notes[0]);
            return null;
        }
    }

    private static class deleteZonaAsync extends AsyncTask<ZonasEntity, Void, Void> {

        private zonasDao mZonasDaoAsync;

        deleteZonaAsync(zonasDao userDao) {
            mZonasDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(ZonasEntity... notes) {
            mZonasDaoAsync.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllZonasAsync extends AsyncTask<ZonasEntity, Void, Void> {

        private zonasDao mZonasDaoAsync;

        deleteAllZonasAsync(zonasDao productoDao) {
            mZonasDaoAsync = productoDao;
        }

        @Override
        protected Void doInBackground(ZonasEntity... notes) {
            mZonasDaoAsync.deleteAll();
            return null;
        }
    }
}
