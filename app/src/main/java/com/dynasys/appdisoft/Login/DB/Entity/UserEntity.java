package com.dynasys.appdisoft.Login.DB.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "user")
public class UserEntity {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "cbnumi")
    int cbnumi;
    @ColumnInfo(name = "cbdesc")
    String username;
    @ColumnInfo(name = "cbci")
    String ci;

    public UserEntity( int cbnumi, String username, String ci) {
        this.cbnumi = cbnumi;
        this.username = username;
        this.ci = ci;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCbnumi() {
        return cbnumi;
    }

    public void setCbnumi(int cbnumi) {
        this.cbnumi = cbnumi;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }
}
