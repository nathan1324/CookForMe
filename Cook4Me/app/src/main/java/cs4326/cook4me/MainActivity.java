package cs4326.cook4me;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Firebase imports for Database stuff

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    
    //Reference to Firebase database
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize reference to database
        databaseReference = FirebaseDatabase.getInstance().getReference();
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set up buttons to redirect
        findViewById(R.id.menu_label_recipes).setOnClickListener(new Button.OnClickListener() {
            @Override public void onClick(View arg0) {
                Intent transfer = new Intent(MainActivity.this, RecipesActivity.class);
                startActivity(transfer);
            }
        });
        findViewById(R.id.menu_label_settings).setOnClickListener(new Button.OnClickListener() {
            @Override public void onClick(View arg0) {
                Intent transfer = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(transfer);
            }
        });
        findViewById(R.id.menu_label_cookterm).setOnClickListener(new Button.OnClickListener() {
            @Override public void onClick(View arg0) {
                Intent transfer = new Intent(MainActivity.this, CookingTerminologyActivity.class);
                startActivity(transfer);
            }
        });
        findViewById(R.id.menu_label_cooktech).setOnClickListener(new Button.OnClickListener() {
            @Override public void onClick(View arg0) {
                Intent transfer = new Intent(MainActivity.this, CookingTechniqueActivity.class);
                startActivity(transfer);
            }
        });

        //Floating login button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent transfer = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(transfer);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return onOptionsItemSelected(item);
    }

}
