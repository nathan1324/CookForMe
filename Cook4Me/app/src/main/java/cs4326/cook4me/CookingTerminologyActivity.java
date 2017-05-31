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

public class CookingTerminologyActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "CookingTermActivity";
    private DatabaseReference databaseReference;
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private HashMap<String, String> dataDictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_terminology);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize reference to database
        databaseReference = FirebaseDatabase.getInstance().getReference();
        // Create ArrayAdapter
        listAdapter = new ArrayAdapter<String>(this, R.layout.row_layout);
        dataDictionary = new HashMap<String, String>();
        //Create query for recipes
        Query recipesReference = databaseReference.child("terms").orderByChild("name");
        recipesReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Get Term object and use the values to update the UI
                CookingTerm term = dataSnapshot.getValue(CookingTerm.class);
                listAdapter.add(term.getName());
                dataDictionary.put(term.getName(), term.getDefinition());
                Log.d(TAG, "loadTerm:onChildAdded");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousName) {
                // Get Term object and use the values to update the UI
                CookingTerm term = dataSnapshot.getValue(CookingTerm.class);
                listAdapter.remove(previousName);
                dataDictionary.remove(previousName);
                listAdapter.add(term.getName());
                dataDictionary.put(term.getName(), term.getDefinition());
                Log.d(TAG, "loadTerm:onChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Get Term object and use the values to update the UI
                CookingTerm term = dataSnapshot.getValue(CookingTerm.class);
                listAdapter.remove(term.getName());
                dataDictionary.remove(term.getName());
                Log.d(TAG, "loadTerm:onRemoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String olden) {
                // Get Term object and use the values to update the UI
                CookingTerm term = dataSnapshot.getValue(CookingTerm.class);
                listAdapter.remove(olden);
                dataDictionary.remove(olden);
                listAdapter.add(term.getName());
                dataDictionary.put(term.getName(), term.getDefinition());
                Log.d(TAG, "loadTerm:onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Term failed, log a message
                Log.w(TAG, "loadTerm:onCancelled", databaseError.toException());
            }
        });

        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.listViewTerms );
        //Add list items!
        mainListView.setAdapter( listAdapter );

        mainListView.setOnItemClickListener(this);

        //Floating login button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent transfer = new Intent(CookingTerminologyActivity.this, LoginActivity.class);
                startActivity(transfer);
            }
        });
        //Up  button returns you to Main menu
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.d(TAG, "You clicked " + listAdapter.getItem(position));
        String nameOf = listAdapter.getItem(position);
        // Then you start a new Activity via Intent
        Intent specialTransfer = new Intent(CookingTerminologyActivity.this, TermActivity.class);
        specialTransfer.putExtra("name", nameOf);
        specialTransfer.putExtra("def", dataDictionary.get(nameOf));
        //intent.putExtra("position", position);
        startActivity(specialTransfer);

    }

}
