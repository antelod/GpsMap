package com.example.gpsmap.services;


import static android.location.LocationManager.GPS_PROVIDER;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.gpsmap.IniciarRutaActivity;
import com.example.gpsmap.db.model.PointDAO;

import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.layers.MyLocationOverlay;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.overlay.Polyline;


public class MiLocalizacionGPS extends Service implements LocationListener {
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 3000; // 3s

    private final Context mContext;
    private Location locationGPS;
    private LocationManager locationManager;
    private Boolean SAVE;
    private Long idTrack;
    private MapView mapView;

    private MyLocationOverlay myLocationOverlay;
    private Polyline polyline;

    public MiLocalizacionGPS(Context ctx, Long idTrack, MapView mapView) {
        this.mContext = ctx;
        this.SAVE = Boolean.FALSE;
        this.idTrack = idTrack;
        this.mapView = mapView;

    }

    public void getActivarGPS() {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        this.locationGPS = locationManager.getLastKnownLocation(GPS_PROVIDER);
        // Location locationNet = locationManager.getLastKnownLocation(NETWORK_PROVIDER);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(SAVE) {
            savePoint(location);
        }
        if(getMyLocationOverlay() != null) {
            polyline.addPoint(new LatLong(location.getLatitude(), location.getLongitude()));
            getMyLocationOverlay().setPosition(location.getLatitude(), location.getLongitude(), location.getAccuracy());
            this.mapView.setCenter(new LatLong(location.getLatitude(), location.getLongitude()));
        }
        this.locationGPS = location;
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        this.stopSelf();
        // Unregister listener
        locationManager.removeUpdates(this);
        super.onDestroy();
    }

    private long savePoint(@NonNull Location location) {
        return PointDAO.savePoint(mContext,location, idTrack);
    }

    public Location getLocationGPS() {
        return locationGPS;
    }

    public void setLocationGPS(Location locationGPS) {
        this.locationGPS = locationGPS;
    }

    public Boolean getSAVE() {
        return SAVE;
    }

    public void setSAVE(Boolean SAVE) {
        this.SAVE = SAVE;
    }

    public Long getIdTrack() {
        return idTrack;
    }

    public void setIdTrack(Long idTrack) {
        this.idTrack = idTrack;
    }

    public MyLocationOverlay getMyLocationOverlay() {
        return myLocationOverlay;
    }

    public void setMyLocationOverlay(MyLocationOverlay myLocationOverlay) {
        this.myLocationOverlay = myLocationOverlay;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setLineaRuta(Polyline polyline) {
        this.polyline = polyline;
    }
}
