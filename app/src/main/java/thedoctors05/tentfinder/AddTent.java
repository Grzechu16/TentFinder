package thedoctors05.tentfinder;

import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddTent extends AppCompatActivity {

    EditText tentNameEditText;
    TextView longitudeTextView, latitudeTextView;
    Button getPositionButton, saveButton;
    String positionProvider;
    Criteria criteria;
    LocationManager locationManager;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tent);

        Log.d("debugging", "I'm in AddTent class.");

        tentNameEditText = (EditText) findViewById(R.id.etTentName);
        longitudeTextView = (TextView) findViewById(R.id.tvLongitude);
        latitudeTextView = (TextView) findViewById(R.id.tvLatitude);
        getPositionButton = (Button) findViewById(R.id.bGetPosition);
        saveButton = (Button) findViewById(R.id.bSaveTent);
    }

    public void getPosition(View view) {

        //Checking permission for SDK >= 23
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            criteria = new Criteria();
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            positionProvider = locationManager.getBestProvider(criteria, true);
            location = locationManager.getLastKnownLocation(positionProvider);

            if (location != null) {
                longitudeTextView.setText("Longitude: " + location.getLongitude());
                latitudeTextView.setText("Longitude: " + location.getLatitude());
                Toast toast = Toast.makeText(getApplicationContext(), "Best provider: " + positionProvider, Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {

            //Request permission from the user
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                return ;
            }

        }
    }
}




