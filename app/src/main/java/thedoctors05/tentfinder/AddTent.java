package thedoctors05.tentfinder;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddTent extends AppCompatActivity {

    EditText tentNameEditText, longitudeEditText, latitudeEditText;
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

        tentNameEditText = (EditText) findViewById(R.id.etTentName);
        longitudeTextView = (TextView) findViewById(R.id.tvLongitude);
        latitudeTextView = (TextView) findViewById(R.id.tvLatitude);
        longitudeEditText = (EditText) findViewById(R.id.etLongitude);
        latitudeEditText = (EditText) findViewById(R.id.etLatitude);
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
                longitudeEditText.setText(String.valueOf(location.getLongitude()));
                latitudeEditText.setText(String.valueOf(location.getLatitude()));
                Toast toast = Toast.makeText(getApplicationContext(), "Best provider: " + positionProvider, Toast.LENGTH_SHORT);
                toast.show();
            }

            setButtonEnable();

        } else {

            //Request permission from the user
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                return ;
            }
        }
    }

    public void setButtonEnable(){
        if(TextUtils.isEmpty(longitudeEditText.getText().toString()) && (TextUtils.isEmpty(latitudeEditText.getText().toString())))
        {
            Toast.makeText(this, "ERROR ", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            saveButton.setEnabled(true);
        }
    }

    public void addNewTent(View view){

        String title = tentNameEditText.getText().toString();
        String longitude = longitudeEditText.getText().toString();
        String latitude = latitudeEditText.getText().toString();

        RowBean rowBean = new RowBean(title,longitude,latitude);

        Intent intent = new Intent();
        intent.putExtra("NewTent", rowBean);
        setResult(RESULT_OK,intent);
        finish();
    }


}




