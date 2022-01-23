package com.example.gpsmap.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.File;

public class DbHelper extends SQLiteOpenHelper {
    private static DbHelper mInstance = null;
    private static final int DATABASE_VERSION = 1;
    private static final StringBuilder SQL_CREATE_POINT = new StringBuilder()
            .append("create table ").append(Util.DB_TABLE_POINT).append(" (")
            .append(Util.DB_TABLE_POINT_COLUMN_ID).append(" integer primary key autoincrement,")
            .append(Util.DB_TABLE_POINT_COLUMN_TRACK_ID).append(" integer not null,")
            .append(Util.DB_TABLE_POINT_COLUMN_LATITUDE).append(" double not null,")
            .append(Util.DB_TABLE_POINT_COLUMN_LONGITUDE).append(" double not null,")
            .append(Util.DB_TABLE_POINT_COLUMN_ELEVATION).append(" double null,")
            .append(Util.DB_TABLE_POINT_COLUMN_ACCURACY).append(" double null,")
            .append(Util.DB_TABLE_POINT_COLUMN_TIME).append(" long not null")
            .append(")");
    private static final StringBuilder SQL_CREATE_IDX_POINT = new StringBuilder()
            .append("create index if not exists ")
            .append(Util.DB_TABLE_POINT)
            .append("_idx ON ")
            .append(Util.DB_TABLE_POINT)
            .append("(")
            .append(Util.DB_TABLE_POINT_COLUMN_ID)
            .append(")");
    private static final StringBuilder SQL_CREATE_TRACK = new StringBuilder()
            .append("create table ").append(Util.DB_TABLE_TRACK).append(" (")
            .append(Util.DB_TABLE_TRACK_COLUMN_ID_TRACK).append(" integer primary key autoincrement,")
            .append(Util.DB_TABLE_TRACK_COLUMN_NAME).append(" text,")
            .append(Util.DB_TABLE_TRACK_COLUMN_DESCRIPTION).append(" text,")
            .append(Util.DB_TABLE_TRACK_COLUMN_DATE).append(" long not null")
            .append(")");
    private static final StringBuilder SQL_CREATE_IDX_TRACK = new StringBuilder()
            .append("create index if not exists ")
            .append(Util.DB_TABLE_TRACK)
            .append("_idx ON ")
            .append(Util.DB_TABLE_TRACK)
            .append("(")
            .append(Util.DB_TABLE_TRACK_COLUMN_ID_TRACK)
            .append(")");

    private DbHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, DATABASE_VERSION);
    }

//    public SQLiteDatabase getDB( Context ctx) {
//        File dbFile= ctx.getApplicationContext().getDatabasePath("tracks.db");
//        // SQLiteDatabase myDB =  SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
//        SQLiteDatabase myDB =  SQLiteDatabase.openOrCreateDatabase(dbFile.getAbsolutePath(), null);
//        return myDB;
//    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_POINT.toString());
        sqLiteDatabase.execSQL(SQL_CREATE_IDX_POINT.toString());
        sqLiteDatabase.execSQL(SQL_CREATE_TRACK.toString());
        sqLiteDatabase.execSQL(SQL_CREATE_IDX_TRACK.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static DbHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DbHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }
}
