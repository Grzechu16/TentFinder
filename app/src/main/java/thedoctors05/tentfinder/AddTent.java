package thedoctors05.tentfinder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Activity which allows to add new tent to list view in main activity
 */
public class AddTent extends AppCompatActivity {

    EditText tentNameEditText, longitudeEditText, latitudeEditText;
    TextView longitudeTextView, latitudeTextView, providerTextView, timeTextView;
    Button getPositionButton, saveButton;
    Criteria criteria;
    LocationManager locationManager;
    public boolean isGPSEnabled = false;
    public boolean isNetworkEnabled = false;
    public boolean isPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tent);

        addElements();
        showKeyboard();
    }
    /**
     * Method adds references to visual elements
     */
    public void addElements() {
        tentNameEditText = (EditText) findViewById(R.id.etTentName);
        longitudeTextView = (TextView) findViewById(R.id.tvLongitude);
        latitudeTextView = (TextView) findViewById(R.id.tvLatitude);
        longitudeEditText = (EditText) findViewById(R.id.etLongitude);
        latitudeEditText = (EditText) findViewById(R.id.etLatitude);
        getPositionButton = (Button) findViewById(R.id.bGetPosition);
        providerTextView = (TextView) findViewById(R.id.tvProvider);
        timeTextView = (TextView) findViewById(R.id.tvTime);
        saveButton = (Button) findViewById(R.id.bSaveTent);
    }
    /**
     * Method checks whether user allows application to get data from GPS
     */
    public void checkPermission() {
        //Checking permission for SDK >= 23
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            //Request permission from the user
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                return;
            }
        }
        isPermission = true;
    }
    /**
     * Method checks whether GPS is enable to get position details
     */
    public void getPosition(View view) {
        checkPermission();
        if (isPermission) {
            criteria = new Criteria();
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled) {
                getLocationFromProvider("gps");
            } else if (isNetworkEnabled) {
                getLocationFromProvider("network");
            } else {
                Toast.makeText(getApplicationContext(), "No provider", Toast.LENGTH_SHORT).show();
            }
        }
        hideKeyboard();
    }
    /**
     * Method gets position details from the best provider
     */
    public void getLocationFromProvider(final String provider) {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitudeEditText.setText(String.valueOf(location.getLongitude()));
                latitudeEditText.setText(String.valueOf(location.getLatitude()));
                providerTextView.setText(getString(R.string.position_provider) + " " + provider);
                getCurrentTime();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        locationManager.requestLocationUpdates(provider, 5000, 0, locationListener);
    }
    /**
     * Method close current activity and pass new tent details to main activity
     */
    public void addNewTent(View view) {

        if ((TextUtils.isEmpty(longitudeEditText.getText())) && (TextUtils.isEmpty(latitudeEditText.getText())) && (TextUtils.isEmpty(tentNameEditText.getText()))) {
            Toast.makeText(getApplicationContext(), "Get location details first!", Toast.LENGTH_SHORT).show();
        } else {
            String title = tentNameEditText.getText().toString();
            String longitude = longitudeEditText.getText().toString();
            String latitude = latitudeEditText.getText().toString();

            Tent tent = new Tent(title, longitude, latitude);

            Intent intent = new Intent();
            intent.putExtra("NewTent", tent);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    /**
     * Show keyboard on activity start
     */
    public void showKeyboard() {
        tentNameEditText.requestFocus();
        tentNameEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(tentNameEditText, 0);
            }
        }, 200);

    }
    /**
     * Hide keyboard when clicking outside of edittext field
     */
    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(tentNameEditText.getWindowToken(), 0);
    }
    /**
     * Get current time
     */
    public void getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String strDate = simpleDateFormat.format(calendar.getTime());
        timeTextView.setText(getString(R.string.position_time) + " " + strDate);
    }



}




