package com.example.gpsmap.utils;

import android.app.Activity;
import android.net.Uri;

public class CargarMapa extends Activity {

    private static CargarMapa mInstance = null;
    private static Uri uriFile;

    private CargarMapa() {
    }

    public static CargarMapa getInstance() {
        if (mInstance == null) {
            // TODO cambiar lo de cargar el mapa
            // https://github.com/mapsforge/mapsforge/blob/ab35bf48b91ccb966dd439d300809a51765207b1/mapsforge-map-android/src/main/java/org/mapsforge/map/android/util/MapViewerTemplate.java#L43
            //    protected MapDataStore getMapFile() {
            mInstance = new CargarMapa();
        }
        return mInstance;
    }

    public Uri getUriFile() {
        return uriFile;
    }

    public void setUriFile(Uri uriFile) {
        this.uriFile = uriFile;
    }

}
