package thedoctors05.tentfinder;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddTent extends AppCompatActivity {

    EditText tentNameEditText;
    TextView longitudeTextView, latitudeTextView;
    Button getPositionButton, saveButton;
    int longitude, latitude;
    String positionProvider;
    Criteria criteria;
    LocationManager locationManager;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tent);

        tentNameEditText = (EditText) findViewById(R.id.etTentName);
        longitudeTextView = (TextView) findViewById(R.id.tvLongitude);
        latitudeTextView = (TextView) findViewById(R.id.tvLatitude);
        getPositionButton = (Button) findViewById(R.id.bGetPosition);
        saveButton = (Button) findViewById(R.id.bSaveTent);

        getPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPositon(longitudeTextView, latitudeTextView);
            }
        });

    }


    private void getPositon(TextView view1, TextView view2) {
        criteria = new Criteria();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        positionProvider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(positionProvider);
        view1.setText("Longitude: " + location.getLongitude());
        view2.setText("Longitude: " + location.getLatitude());
    }

}
