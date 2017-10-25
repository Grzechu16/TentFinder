package thedoctors05.tentfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Button addTent, navigate;

    public void navigationActivity (View v) {
        Intent i = new Intent(this, Navigation.class);
        startActivity(i);
    }

    public void addTentActivity (View v) {
        Intent i = new Intent(this, AddTent.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addTent = (Button) findViewById(R.id.bAddTent);
        navigate = (Button) findViewById(R.id.bNavigate);
    }
}