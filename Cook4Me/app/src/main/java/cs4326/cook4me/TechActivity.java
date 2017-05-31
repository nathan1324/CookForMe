package cs4326.cook4me;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class TechActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CookingTech chosenOne = getIntent().getParcelableExtra("techObject");

        //Set term name as title of page
        TextView title = (TextView)findViewById(R.id.tech_toolbar_title);
        title.setText(chosenOne.getTitle());

        //And put down technique description
        TextView textBox = (TextView)findViewById(R.id.tech_description);
        textBox.setText(chosenOne.getDescription());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
