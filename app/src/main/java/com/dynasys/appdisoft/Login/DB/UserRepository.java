package com.dynasys.appdisoft.Login.DB;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.dynasys.appdisoft.Login.DB.Dao.UserDao;
import com.dynasys.appdisoft.Login.DB.Entity.UserEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Created by ravi on 05/02/18.
 */

public class UserRepository {

    private UserDao mUserDao;
    private LiveData<List<UserEntity>> mAllUsers;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mAllUsers = mUserDao.getAllUsers();
    }

    public LiveData<List<UserEntity>> getAllUsers() {
        return mAllUsers;
    }

    public UserEntity getUser(int noteId) throws ExecutionException, InterruptedException {
        return new getUserAsync(mUserDao).execute(noteId).get();
    }

    public void insertUser(UserEntity user) {
        new insertUsersAsync(mUserDao).execute(user);
    }

    public void updateNote(UserEntity user) {
        new updateUsersAsync(mUserDao).execute(user);
    }

    public void deleteUser(UserEntity user) {
        new deleteUsersAsync(mUserDao).execute(user);
    }

    public void deleteAllUsers() {
        new deleteAllUsersAsync(mUserDao).execute();
    }

    /**
     * NOTE: all write operations should be done in background thread,
     * otherwise the following error will be thrown
     * `java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.`
     */

    private static class getUserAsync extends AsyncTask<Integer, Void, UserEntity> {

        private UserDao mUserDaoAsync;

        getUserAsync(UserDao animalDao) {
            mUserDaoAsync = animalDao;
        }

        @Override
        protected UserEntity doInBackground(Integer... ids) {
            return mUserDaoAsync.getUserById(ids[0]);
        }
    }

    private static class insertUsersAsync extends AsyncTask<UserEntity, Void, Long> {

        private UserDao mUserDaoAsync;

        insertUsersAsync(UserDao userDao) {
            mUserDaoAsync = userDao;
        }

        @Override
        protected Long doInBackground(UserEntity... notes) {
            long id = mUserDaoAsync.insert(notes[0]);
            return id;
        }
    }

    private static class updateUsersAsync extends AsyncTask<UserEntity, Void, Void> {

        private UserDao mUserDaoAsync;

        updateUsersAsync(UserDao userDao) {
            mUserDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(UserEntity... notes) {
            mUserDaoAsync.update(notes[0]);
            return null;
        }
    }

    private static class deleteUsersAsync extends AsyncTask<UserEntity, Void, Void> {

        private UserDao mUserDaoAsync;

        deleteUsersAsync(UserDao userDao) {
            mUserDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(UserEntity... notes) {
            mUserDaoAsync.delete(notes[0]);
            return null;
        }
    }

    private static class deleteAllUsersAsync extends AsyncTask<UserEntity, Void, Void> {

        private UserDao mUserDaoAsync;

        deleteAllUsersAsync(UserDao userDao) {
            mUserDaoAsync = userDao;
        }

        @Override
        protected Void doInBackground(UserEntity... notes) {
            mUserDaoAsync.deleteAll();
            return null;
        }
    }
}
