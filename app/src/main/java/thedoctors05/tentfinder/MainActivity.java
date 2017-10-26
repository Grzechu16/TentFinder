package thedoctors05.tentfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Button addTent, navigate;
    ArrayList<RowBean> lista = new ArrayList();
    RowAdapter adapter;
    public static final int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //lista.add(new RowBean("Namiot mr gruszeckiego", "Longitude", "Latitude"));


        adapter = new RowAdapter(this, R.layout.custom_row, lista);

        listView = (ListView) findViewById(R.id.listView);
        addTent = (Button) findViewById(R.id.bAddTent);
        navigate = (Button) findViewById(R.id.bNavigate);

        listView.setAdapter(adapter);

    }

    public void navigationActivity(View v) {
        Intent intent = new Intent(this, Navigation.class);
        startActivity(intent);
    }

    public void addTentActivity(View v) {
        Intent intent = new Intent(this, AddTent.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                RowBean rowBean = data.getParcelableExtra("NewTent");
                lista.add(rowBean);
                adapter.notifyDataSetChanged();
            }
        }


    }


}