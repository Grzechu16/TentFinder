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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    private Sensor gsensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        bNavigate = (Button) findViewById(R.id.bNavigate);
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
    }

    public void navigationStart(View v) {
        sm = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        gsensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sm.registerListener(this, gsensor, SensorManager.SENSOR_DELAY_GAME);

        cr = new Criteria();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        bestProvider = lm.getBestProvider(cr, true);
        loc = lm.getLastKnownLocation(bestProvider);
        currLon = loc.getLongitude();
        currLat = loc.getLatitude();

        azimuthCalculate();
        rotationCalculate();
        distanceCalculate();
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
        distance = Math.sqrt(Math.pow(deltaX, 2.0) + Math.pow(deltaY, 2.0));
    }

    public void onLocationChanged(Location location) {
        bestProvider = lm.getBestProvider(cr, true);
        loc = lm.getLastKnownLocation(bestProvider);

        currLon = loc.getLongitude();
        currLat = loc.getLatitude();
    }


    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        azimuthPhone = event.values[0];
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
}
