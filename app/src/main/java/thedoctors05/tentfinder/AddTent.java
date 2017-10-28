package thedoctors05.tentfinder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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

public class AddTent extends AppCompatActivity {

    EditText tentNameEditText, longitudeEditText, latitudeEditText;
    TextView longitudeTextView, latitudeTextView, providerTextView, timeTextView;
    Button getPositionButton, saveButton;
    String positionProvider;
    Criteria criteria;
    LocationManager locationManager;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tent);

        addElements();
        showKeyboard();

    }

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
                longitudeEditText.setText(String.valueOf(location.getLongitude()));
                latitudeEditText.setText(String.valueOf(location.getLatitude()));
                getProviderDetails(positionProvider);
            }

            setSaveButtonEnable();

        } else {

            //Request permission from the user
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                return;
            }
        }
        hideKeyboard();
        getCurrentTime();
    }

    public void setSaveButtonEnable() {
        if (TextUtils.isEmpty(longitudeEditText.getText().toString()) && (TextUtils.isEmpty(latitudeEditText.getText().toString()))) {
            Toast.makeText(this, "Get location details first! ", Toast.LENGTH_LONG).show();
            return;
        } else {
            saveButton.setEnabled(true);
        }
    }

    public void addNewTent(View view) {

        String title = tentNameEditText.getText().toString();
        String longitude = longitudeEditText.getText().toString();
        String latitude = latitudeEditText.getText().toString();

        Tent tent = new Tent(title, longitude, latitude);

        Intent intent = new Intent();
        intent.putExtra("NewTent", tent);
        setResult(RESULT_OK, intent);
        finish();
    }

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

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(tentNameEditText.getWindowToken(), 0);
    }

    public void getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String strDate = simpleDateFormat.format(calendar.getTime());
        timeTextView.setText(getString(R.string.position_time) + " " + strDate);
    }

    public void getProviderDetails(String provider) {
        providerTextView.setText(getString(R.string.position_provider)+ " " + provider);
    }

}




