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
    Button addTent, updateDatabase, exit;
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
        lista.add(new Tent("Namiot3", "141", "1411"));
        adapter.notifyDataSetChanged();
       // postData(updateDatabase, "namiotSuper", "4", "1");
        setListeners();
    }

    public void addElements() {
        listView = (ListView) findViewById(R.id.listView);
        addTent = (Button) findViewById(R.id.bAddTent);
        updateDatabase = (Button) findViewById(R.id.bUpdateDatabase);
        exit = (Button) findViewById(R.id.bExit);
    }

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

        updateDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < lista.size(); i++) {
                    postData(lista.get(i).getTitle(), lista.get(i).getLongitude(), lista.get(i).getLatitude());
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < lista.size(); i++) {
                    postData(lista.get(i).getTitle(), lista.get(i).getLongitude(), lista.get(i).getLatitude());
                }
                finish();
            }
        });

        addTent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTentActivity();
            }
        });
    }

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

    public void initializeRetrofit() {
        retrofit = new RestAdapter.Builder()
                .setEndpoint("http://192.168.2.105:8080")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        retrofitWebService = retrofit.create(RetrofitWebService.class);
    }

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