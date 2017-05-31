package cs4326.cook4me;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class CookingTechniqueActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "CookingTechActivity";
    private DatabaseReference databaseReference;
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private HashMap<String, CookingTech> allTechData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_technique);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize reference to database
        databaseReference = FirebaseDatabase.getInstance().getReference();
        // Create ArrayAdapter
        listAdapter = new ArrayAdapter<String>(this, R.layout.row_layout);
        allTechData = new HashMap<String, CookingTech>();
        //Create query for recipes
        Query techRef = databaseReference.child("techniques").orderByChild("title");
        techRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CookingTech technique = dataSnapshot.getValue(CookingTech.class);
                listAdapter.add(technique.getTitle());
                allTechData.put(technique.getTitle(), technique);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String oldOne) {
                CookingTech technique = dataSnapshot.getValue(CookingTech.class);
                listAdapter.remove(oldOne);
                listAdapter.add(technique.getTitle());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                CookingTech technique = dataSnapshot.getValue(CookingTech.class);
                listAdapter.remove(technique.getTitle());
                allTechData.remove(technique.getTitle());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                CookingTech technique = dataSnapshot.getValue(CookingTech.class);
                listAdapter.remove(s);
                listAdapter.add(technique.getTitle());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Term failed, log a message
                Log.w(TAG, "loadTech:onCancelled", databaseError.toException());
            }
        });

        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.listViewCookTech );
        //Add list items!
        mainListView.setAdapter( listAdapter );

        mainListView.setOnItemClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent transfer = new Intent(CookingTechniqueActivity.this, LoginActivity.class);
                startActivity(transfer);
            }
        });

        //Up button returns to home page
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.d(TAG, "You clicked " + listAdapter.getItem(position));
        String nameOf = listAdapter.getItem(position);
        // Then you start a new Activity via Intent
        Intent specialTransfer = new Intent(CookingTechniqueActivity.this, TechActivity.class);
        specialTransfer.putExtra("techObject", allTechData.get(nameOf));
        //intent.putExtra("position", position);
        startActivity(specialTransfer);
    }

}
