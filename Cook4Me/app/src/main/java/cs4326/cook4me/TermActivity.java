package cs4326.cook4me;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


public class TermActivity extends AppCompatActivity {
    private static final String TAG = "TermActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get proper data
        Bundle dictionary = getIntent().getExtras();
        String term = dictionary.getString("name");
        String definition = dictionary.getString("def");

        //Set technique name as title of page
        TextView title = (TextView)findViewById(R.id.term_toolbar_title);
        title.setText(term);

        //And put down terminology definition
        TextView textBox = (TextView)findViewById(R.id.term_definition);
        textBox.setText(definition);


        //Back button sends back to CookingTerminologyActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
