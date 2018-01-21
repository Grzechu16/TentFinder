package thedoctors05.tentfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Button buttonAddTent, buttonUpdateDatabase, buttonExit;
    ArrayList<Tent> lista = new ArrayList();
    TentAdapter adapter;
    public static final int REQUEST_CODE = 123;
    String tentName, longitude, latitude;
    public static final String NAME = "name";
    public static final String LONG = "long";
    public static final String LATI = "lati";
    RestAdapter retrofit;
    RetrofitWebService retrofitWebService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addElements();
        GetData();

        adapter = new TentAdapter(this, R.layout.custom_row, lista);
        listView.setAdapter(adapter);

        setListeners();
    }

    /**
     * Method adds references to visual elements
     */
    public void addElements() {
        listView = (ListView) findViewById(R.id.listView);
        buttonAddTent = (Button) findViewById(R.id.bAddTent);
        buttonUpdateDatabase = (Button) findViewById(R.id.bUpdateDatabase);
        buttonExit = (Button) findViewById(R.id.bExit);
    }

    /**
     * Method sets listeners for buttons and listView
     */
    public void setListeners() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                tentName = ((TextView) view.findViewById(R.id.tvTitleRow)).getText().toString();
                longitude = ((TextView) view.findViewById(R.id.tvLongitudeRow)).getText().toString();
                latitude = ((TextView) view.findViewById(R.id.tvLatitudeRow)).getText().toString();

                Intent intent = new Intent(getApplicationContext(), Navigation.class);
                intent.putExtra(NAME, tentName);
                intent.putExtra(LONG, longitude);
                intent.putExtra(LATI, latitude);
                startActivity(intent);
            }
        });

        buttonUpdateDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < lista.size(); i++) {
                    postData(lista.get(i).getTitle(), lista.get(i).getLongitude(), lista.get(i).getLatitude());
                }
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < lista.size(); i++) {
                    postData(lista.get(i).getTitle(), lista.get(i).getLongitude(), lista.get(i).getLatitude());
                }
                finish();
            }
        });

        buttonAddTent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTentActivity();
            }
        });
    }
    /**
     * Method which allows to open new activity and add new tent to list
     */
    public void addTentActivity() {
        Intent intent = new Intent(this, AddTent.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Tent tent = data.getParcelableExtra("NewTent");
                lista.add(tent);
                adapter.notifyDataSetChanged();
            }
        }
    }
    /**
     * Method which initialize retrofit api to generate get and post requests
     */
    public void initializeRetrofit() {
        retrofit = new RestAdapter.Builder()
                .setEndpoint("http://10.0.2.2:8080")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        retrofitWebService = retrofit.create(RetrofitWebService.class);
    }
    /**
     * Method which allows to get list of tents from database
     */
    public void GetData() {
        initializeRetrofit();
        retrofitWebService.getData(new Callback<List<Tent>>() {
            @Override
            public void success(List<Tent> Tent, Response response) {
                lista.addAll(Tent);
                Toast.makeText(getApplicationContext(), R.string.dataFromDatabaseInfo, Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), R.string.dataFromDatabaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * Method which allows to update database with new list of tents
     */
    public void postData(String Title, String Longitude, String Latitude) {
        initializeRetrofit();
        retrofitWebService.postData(Title, Longitude, Latitude, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Toast.makeText(getApplicationContext(), R.string.dataSaved, Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), R.string.dataNotSaved, Toast.LENGTH_LONG).show();
            }
        });
    }

}