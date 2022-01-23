package com.example.gpsmap.db.model;

import android.content.ContentValues;
import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;

import com.example.gpsmap.db.DbHelper;
import com.example.gpsmap.db.Util;

import java.util.Date;

public class TrackDAO {
    public static long saveTrack(@NonNull Context ctx, String name, String des) {
        ContentValues value = new ContentValues();
        value.put(Util.DB_TABLE_TRACK_COLUMN_NAME, name);
        value.put(Util.DB_TABLE_TRACK_COLUMN_DESCRIPTION, des);
        value.put(Util.DB_TABLE_TRACK_COLUMN_DATE, new Date().getTime());

        return DbHelper.getInstance(ctx).getWritableDatabase().insert(Util.DB_TABLE_TRACK, null, value);

    }
}
