package cs4326.cook4me;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Comparator;
import java.util.TreeSet;

public class SearchRecipeActivity extends AppCompatActivity {
    private static final String TAG = "SearchRecActivity";
    private DatabaseReference databaseReference;
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private String theQueriedOne;
    private TreeSet<Recipe> allValidData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.listViewRecipeSearch );
        // Create ArrayAdapter
        listAdapter = new ArrayAdapter<String>(this, R.layout.row_layout);

        // Create object to hold all valid data for passing to recipe site
        allValidData = new TreeSet<Recipe>(new Comparator<Recipe>() {
            @Override
            public int compare(Recipe o1, Recipe o2) {
                return o1.getTitle().compareToIgnoreCase(o2.getTitle());
            }
        });

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchDatabase(query);
        }

        //Up button goes back to recipes
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void searchDatabase(String q) {
        TextView title = (TextView)findViewById(R.id.search_toolbar_title);
        title.append(" - "+q);

        theQueriedOne = q.toLowerCase();

        //Default state is that none were found
        final String noneMessage = "Search Query Not Found";
        listAdapter.add(noneMessage);

        //Initialize reference to database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Create query for recipes
        Query recipesReference = databaseReference.child("recipes").orderByChild("title");
        recipesReference.addChildEventListener(new ChildEventListener() {
            private boolean isFirst = true;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                if(recipe.getTitle().toLowerCase().contains(theQueriedOne)) {
                    //Hit!
                    allValidData.add(recipe);
                    listAdapter.add(recipe.getTitle());
                    //If one is found, then none found message removed
                    if(isFirst) {
                        isFirst = false;
                        listAdapter.remove(noneMessage);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                Log.d(TAG, "onRecipeLoad: data changed from "+s+" to "+recipe.getTitle());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                if(allValidData.contains(recipe)) {
                    allValidData.remove(recipe);
                    listAdapter.remove(recipe.getTitle());
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onRecipeLoad: data moved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Recipe failed, log a message
                Log.w(TAG, "loadRecipe:onCancelled", databaseError.toException());
            }
        });

        //Display what we have found
        mainListView.setAdapter( listAdapter );
    }

}
