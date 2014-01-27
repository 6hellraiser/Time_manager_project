package ru.vsu.cs.timemanagement;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
import ru.yandex.yandexmapkit.utils.GeoPoint;

public class Map_activity extends Activity {
    float x;
    float y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        final MapView mMapView = (MapView) findViewById(R.id.map);
        mMapView.showBuiltInScreenButtons(true);
        MapController mMapController = mMapView.getMapController();
      //  mMapController.setPositionAnimationTo(new GeoPoint(60.113337, 55.151317));

        //mMapController.setZoomCurrent(15);
        mMapController.getOverlayManager().addOverlay(new MyOverlay(mMapController));
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

    public class MyOverlay extends Overlay {

        public MyOverlay(MapController mapController) {
            super(mapController);
        }

        @Override
        public boolean onSingleTapUp(float v, float v2) {
            x = v;
            y = v2;
            return true;
        }

        @Override
        public int compareTo(Object another) {
            return 0;
        }
    }

}
