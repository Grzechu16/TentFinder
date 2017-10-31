package thedoctors05.tentfinder;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Navigation extends AppCompatActivity implements SensorEventListener, LocationListener {
    ImageView arrow;
    TextView distance_text, coor;
    EditText tentName;
    Double longitude = null;
    Double latitude = null;

    ImageView arrow;
    TextView distance_text, coor, tentName;

    Double currLon, currLat;
    Double deltaX, deltaY, wbDeltaX, wbDeltaY, fi, azimuthTent, angle, distance;
    float azimuthPhone;

    LocationManager lm;
    Criteria cr;
    String bestProvider;
    Location loc;
    private SensorManager sm;

    private int[] layouts;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        arrow = (ImageView) findViewById(R.id.arrow);
        tentName = (EditText) findViewById(R.id.nazwaNamiotu_et);
        arrow.setVisibility(View.INVISIBLE);
        tentName = (TextView) findViewById(R.id.nazwaNamiotu_tv);
        distance_text = (TextView) findViewById(R.id.distance_text);
        coor = (TextView) findViewById(R.id.coor);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            String title = extras.getString("name");
            longitude = Double.parseDouble(extras.getString("long"));
            latitude = Double.parseDouble(extras.getString("lati"));
            tentName.setText(title);
        }

        arrow.setVisibility(View.INVISIBLE);
        navigationStart();

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        layouts = new int[] {
                R.layout.activity_navigation,
                R.layout.activity_test
        };
    }

    public void navigationStart () {
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ORIENTATION), 0, null);   //Compass

        cr = new Criteria();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        refreshLoc();
        lm.requestLocationUpdates(bestProvider, 1000, 1, this);

        currLon = loc.getLongitude();
        currLat = loc.getLatitude();

        azimuthCalculate();
        rotationCalculate();
        distanceCalculate();    //Order must be keeped

        arrow.setRotation(angle.floatValue());

        distance_text.setText(distance + "");
        coor.setText(azimuthPhone + "");
    }

    public void azimuthCalculate() {
        deltaY = longitude - currLon;
        deltaX = latitude - currLat;

        if (deltaY < 0) {
            wbDeltaY = -deltaY;
        } else {
            wbDeltaY = deltaY;
        }

        if (deltaX < 0) {
            wbDeltaX = -deltaX;
        } else {
            wbDeltaX = deltaX;
        }

        fi = Math.toDegrees(Math.atan(wbDeltaY / wbDeltaX));

        if (deltaY > 0 && deltaX > 0) {
            azimuthTent = fi;
        } else if (deltaY > 0 && deltaX < 0) {
            azimuthTent = 180.0 - fi;
        } else if (deltaY < 0 && deltaX < 0) {
            azimuthTent = 180.0 + fi;
        } else if (deltaY < 0 && deltaX > 0) {
            azimuthTent = 360.0 - fi;
        }
    }

    public void rotationCalculate() {
        angle = azimuthTent - azimuthPhone;
    }

    public void distanceCalculate() {
        distance = Math.sqrt(Math.pow(latitude - currLat, 2.0) + Math.pow(Math.cos(((currLat*Math.PI)/180.0))*(longitude - currLon), 2.0))*(40075.704/360.0);
    }

    private void refreshLoc() {
        bestProvider = lm.getBestProvider(cr, true);
        loc = lm.getLastKnownLocation(bestProvider);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {

            switch (position){
                case 0:
                    arrow.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    arrow.setVisibility(View.INVISIBLE);
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}

        @Override
        public void onPageScrollStateChanged(int arg0) {}
    };

    public void onLocationChanged (Location location) {
        refreshLoc();

        currLon = loc.getLongitude();
        currLat = loc.getLatitude();

        azimuthCalculate();
        rotationCalculate();
        distanceCalculate();    //Order must be keeped

        distance_text.setText(distance + "");
        Log.d("debugging", "Current lon/lat: " + currLon + " / " + currLat);
    }

    @Override
    public void onAccuracyChanged (Sensor arg0, int arg1){
        Toast toast = Toast.makeText(getApplicationContext(), "Accuracy changed to: " + bestProvider, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public  void onSensorChanged(SensorEvent event){
        azimuthPhone = event.values[0];

        rotationCalculate();
        arrow.setRotation(angle.floatValue());
        coor.setText("Phone az.: " + azimuthPhone + "\nTent az.: " + azimuthTent + "\nAngle: " + angle);
    }

    @Override
    public void onProviderDisabled(String provider){}

    @Override
    public void onProviderEnabled(String provider){}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    /**
     * View pager adapter
     */
    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public ViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}