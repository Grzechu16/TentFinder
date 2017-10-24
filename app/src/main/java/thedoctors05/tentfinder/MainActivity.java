package thedoctors05.tentfinder;

import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button AddTent;

    TextView provider, longitude, latitude;
    LocationManager lm;
    Criteria cr;
    Location loc;
    String bestProvider;

    Button coordinates;

    public void getLocationFunc (View v){
        //Checking permission for SDK >= 23
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("debugging", "Permission checked: OK");
            cr = new Criteria();    //Provider criteria, e.g. power consumption or cost
            lm = (LocationManager) getSystemService(LOCATION_SERVICE);      //Via getSystemService you can manage other sensors such as temperature sensor, pressure sensor
            bestProvider = lm.getBestProvider(cr, true);    //Name of the best provider considering criteria, true = only active providers
            loc = lm.getLastKnownLocation(bestProvider);
            Log.d("debugging", "Long: " + loc.getLongitude() + " / Lat: " + loc.getLatitude());

            //Setting text
            provider.setText(bestProvider);
            longitude.setText("" + loc.getLongitude());
            latitude.setText("" + loc.getLatitude());
        } else {
            Log.d("debugging", "Permission checked: NOK");
            //Request permission from the user
            if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( this, new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
            Log.d("debugging", "Permission requested.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AddTent = (Button) findViewById(R.id.bAddTent);
        AddTent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddTent.class);
                startActivity(intent);
            }
        });


        provider = (TextView) findViewById(R.id.dostawca_ety);
        longitude = (TextView) findViewById(R.id.dlugosc_ety);
        latitude = (TextView) findViewById(R.id.szerokosc_ety);

        coordinates = (Button) findViewById(R.id.coordinates_button);
        Log.d("debugging", "Variables defined.");
    }
}
