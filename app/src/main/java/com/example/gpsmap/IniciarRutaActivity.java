package com.example.gpsmap;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gpsmap.db.model.TrackDAO;
import com.example.gpsmap.services.MiLocalizacionGPS;
import com.example.gpsmap.utils.CargarMapa;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.util.Utils;
import org.mapsforge.map.android.graphics.AndroidBitmap;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.layers.MyLocationOverlay;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Circle;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.core.graphics.Paint;
import java.io.FileInputStream;

public class IniciarRutaActivity extends AppCompatActivity {

    private MapView mapView;
    private MiLocalizacionGPS gps;
    private Long idTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_iniciar_ruta);
        mapView = (MapView) findViewById(R.id.mapView);

        idTrack = crearTrack();

        gps = new MiLocalizacionGPS(getApplicationContext(), idTrack, mapView);
        gps.getActivarGPS();
        gps.getLocationGPS();

        openMap();
        gps.setMyLocationOverlay(miLocalizacionMarker());
        gps.setLineaRuta(crearLineaRuta());
        //miLocalizacionMarker();

    }

    private Polyline crearLineaRuta() {
        // linea ruta
        Polyline polyline = new Polyline(IniciarRutaActivity.getPaint(
                AndroidGraphicFactory.INSTANCE.createColor(Color.BLUE),
                (int) (8 * mapView.getModel().displayModel.getScaleFactor()),
                Style.STROKE), AndroidGraphicFactory.INSTANCE);
        mapView.getLayerManager().getLayers().add(polyline);
        return polyline;
    }


    private Long crearTrack() {
        return TrackDAO.saveTrack(getApplicationContext(), "name", "descripcion");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gps.onDestroy();
    }
    public void iniciarRuta(View view) {
        gps.setSAVE(Boolean.TRUE);
    }

    public void pararRuta(View view) {
        gps.setSAVE(Boolean.FALSE);
    }

    private void openMap() {
        try {
            /*
             * To avoid redrawing all the tiles all the time, we need to set up a tile cache with an
             * utility method.
             */
            TileCache tileCache = AndroidUtil.createTileCache(this, "mapcache",
                    mapView.getModel().displayModel.getTileSize(), 1f,
                    mapView.getModel().frameBufferModel.getOverdrawFactor());

            /*
             * Now we need to set up the process of displaying a map. A map can have several layers,
             * stacked on top of each other. A layer can be a map or some visual elements, such as
             * markers. Here we only show a map based on a mapsforge map file. For this we need a
             * TileRendererLayer. A TileRendererLayer needs a TileCache to hold the generated map
             * tiles, a map file from which the tiles are generated and Rendertheme that defines the
             * appearance of the map.
             */
            FileInputStream fis = (FileInputStream) getContentResolver().openInputStream(CargarMapa.getInstance().getUriFile());
            MapDataStore mapDataStore = new MapFile(fis);


            TileRendererLayer tileRendererLayer = AndroidUtil.createTileRendererLayer(tileCache,
                    mapView.getModel().mapViewPosition, mapDataStore, InternalRenderTheme.DEFAULT, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);

            /*
             * On its own a tileRendererLayer does not know where to display the map, so we need to
             * associate it with our mapView.
             */
            mapView.getLayerManager().getLayers().add(tileRendererLayer);
            /*
             * The map also needs to know which area to display and at what zoom level.
             * Note: this map position is specific to Berlin area.
             */
            mapView.setCenter(new LatLong(gps.getLocationGPS().getLatitude(), gps.getLocationGPS().getLongitude()));
            mapView.setZoomLevel((byte) 12);
        } catch (Exception e) {
            /*
             * In case of map file errors avoid crash, but developers should handle these cases!
             */
            e.printStackTrace();
        }
    }

    private MyLocationOverlay miLocalizacionMarker() {
        // marker to show at the location
        Bitmap bitmap = new AndroidBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_maps_indicator_current_position));
        Marker marker = new Marker(new LatLong(gps.getLocationGPS().getLatitude(), gps.getLocationGPS().getLongitude() ), bitmap, 0, 0);

        // circle to show the location accuracy (optional)
        Circle circle = new Circle(null, 0,
                getPaint(AndroidGraphicFactory.INSTANCE.createColor(48, 0, 0, 255), 0, Style.FILL),
                getPaint(AndroidGraphicFactory.INSTANCE.createColor(160, 0, 0, 255), 2, Style.STROKE));

        // create the overlay
        MyLocationOverlay myLocationOverlay = new MyLocationOverlay(marker, circle);
        this.mapView.getLayerManager().getLayers().add(myLocationOverlay);

        return myLocationOverlay;
    }

    public static Paint getPaint(int color, int strokeWidth, Style style) {
        Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(style);
        return paint;
    }
}
