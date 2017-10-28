package thedoctors05.tentfinder;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Navigation extends AppCompatActivity implements SensorEventListener, LocationListener {

    Double longitude = 19.965433;
    Double latitude = 50.057397;

    ImageView arrow;
    Button bNavigate;
    TextView distance_text, coor;
    EditText tentName;

    Double currLon, currLat;
    Double deltaX, deltaY, wbDeltaX, wbDeltaY, fi, azimuthTent, angle, distance;
    float azimuthPhone;

    LocationManager lm;
    Criteria cr;
    String bestProvider;
    Location loc;

    private SensorManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        arrow = (ImageView) findViewById(R.id.arrow);
        arrow.setVisibility(View.INVISIBLE);
        tentName = (EditText) findViewById(R.id.nazwaNamiotu_et);

        distance_text = (TextView) findViewById(R.id.distance_text);
        coor = (TextView) findViewById(R.id.coor);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            String title = extras.getString("name");
            tentName.setText(title);
        }

        navigationStart();
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

        rotationCalculate();
        distanceCalculate();    //Order must be keeped
        Log.d("debugging", "                             ");
        Log.d("debugging", "azimuthTent: " + azimuthTent);
        Log.d("debugging", "angle: " + angle);
        Log.d("debugging", "distance: " + distance);

        arrow.setRotation(angle.floatValue());
        arrow.setVisibility(View.VISIBLE);

        distance_text.setText(distance + "");
        coor.setText(azimuthPhone + "");
    }

    public void azimuthCalculate() {
        deltaY = latitude - currLat;
        deltaX = longitude - currLon;

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

        fi = Math.atan(wbDeltaY / wbDeltaX);

        if (deltaY > 0 && deltaX > 0) {
            azimuthTent = fi;
        } else if (deltaY > 0 && deltaX < 0) {
            azimuthTent = 180.0 - fi;
        } else if (deltaY < 0 && deltaX < 0) {
            azimuthTent = 180. + fi;
        } else if (deltaY < 0 && deltaX > 0) {
            azimuthTent = 360.0 - fi;
        }
    }

    public void rotationCalculate() {
        azimuthCalculate();
        angle = azimuthTent - azimuthPhone;
    }

    public void distanceCalculate() {
        distance = Math.sqrt(Math.pow(longitude - currLon, 2.0) + Math.pow(Math.cos(((currLon*Math.PI)/180.0))*(latitude - currLat), 2.0))*(40075.704/360.0);
    }

    private void refreshLoc() {
        bestProvider = lm.getBestProvider(cr, true);
        loc = lm.getLastKnownLocation(bestProvider);
    }

    public void onLocationChanged (Location location) {
        refreshLoc();

        currLon = loc.getLongitude();
        currLat = loc.getLatitude();
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
        coor.setText(azimuthPhone + "");
    }

    @Override
    public void onProviderDisabled(String provider){}

    @Override
    public void onProviderEnabled(String provider){}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
