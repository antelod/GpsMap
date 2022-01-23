package com.example.gpsmap.db.model;

import android.content.ContentValues;
import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;

import com.example.gpsmap.db.DbHelper;
import com.example.gpsmap.db.Util;

import java.util.Date;

public class PointDAO {

    public static long savePoint(@NonNull Context ctx, @NonNull Location location, @NonNull Long idTrack) {
        ContentValues value = new ContentValues();
        value.put(Util.DB_TABLE_POINT_COLUMN_LATITUDE, location.getLatitude());
        value.put(Util.DB_TABLE_POINT_COLUMN_LONGITUDE, location.getLongitude());
        value.put(Util.DB_TABLE_POINT_COLUMN_ELEVATION, location.getAltitude());
        value.put(Util.DB_TABLE_POINT_COLUMN_TIME,  location.getTime());
        value.put(Util.DB_TABLE_POINT_COLUMN_ACCURACY,  location.getAccuracy());
        value.put(Util.DB_TABLE_POINT_COLUMN_TRACK_ID, idTrack != null ? idTrack.intValue() : 0);

        return DbHelper.getInstance(ctx).getWritableDatabase().insert(Util.DB_TABLE_POINT, null, value);

    }
}
