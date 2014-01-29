package ru.vsu.cs.timemanagement;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Toast;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;
import ru.yandex.yandexmapkit.utils.ScreenPoint;

public class Map_activity extends Activity {
    float x;
    float y;
    MapController mMapController;
    MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle b = getIntent().getExtras();

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.showBuiltInScreenButtons(true);
        mMapController = mMapView.getMapController();
      //  mMapController.setPositionAnimationTo(new GeoPoint(60.113337, 55.151317));

        //mMapController.setZoomCurrent(15);
        /*Overlay overlay = new Overlay(mMapController);
        OverlayItem item = new OverlayItem(new GeoPoint(51.68 , 39.257), getResources().getDrawable(R.drawable.shop));
        overlay.addOverlayItem(item);
        mMapController.getOverlayManager().addOverlay(overlay);*/
        mMapController.getOverlayManager().addOverlay(new MyOverlay(mMapController));

        boolean ok = b.getBoolean("draw");
        if (ok) {
            x = b.getFloat("coordX");
            y = b.getFloat("coordY");
            Log.e("Map", String.format("Create %s, %s",x ,y));
            Draw();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.place_selected:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("x", x);
                returnIntent.putExtra("y", y);
                setResult(RESULT_OK,returnIntent);
                finish();
        }
        return false;
    }

    public void Draw() {
        //final OverlayItem item = new OverlayItem(new GeoPoint(new ScreenPoint(x, y)), getResources().getDrawable(R.drawable.shop));

        Log.e("Map", String.format("Draw %s, %s",x ,y));
        final OverlayItem item = new OverlayItem(new GeoPoint(x,y), getResources().getDrawable(R.drawable.shop));
        Overlay overlay = new Overlay(mMapController);
        overlay.addOverlayItem(item);
        mMapController.getOverlayManager().addOverlay(overlay);
    }

    public class MyOverlay extends Overlay {

        public MyOverlay(MapController mapController) {
            super(mapController);
        }

        @Override
        public boolean onSingleTapUp(float v, float v2) {
            GeoPoint geoPoint = mMapController.getGeoPoint(new ScreenPoint(v, v2));
            x = ((Double)geoPoint.getLat()).floatValue();
            y = ((Double)geoPoint.getLon()).floatValue();
           /* OverlayItem item = new OverlayItem(new GeoPoint(v , v2), getResources().getDrawable(R.drawable.shop));
            Overlay overlay = new Overlay(getMapController());
            overlay.addOverlayItem(item);
            getMapController().getOverlayManager().addOverlay(overlay);*/
            //addOverlayItem(item);
            Log.e("Map", String.format("Tap %s, %s",x ,y));
            Draw();
            return true;
        }

        @Override
        public int compareTo(Object another) {
            return 0;
        }
    }

}
