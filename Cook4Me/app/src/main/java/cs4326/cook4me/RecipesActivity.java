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

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

public class RecipesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private static final String TAG = "RecipesActivity";
    private DatabaseReference databaseReference;
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private HashMap<String, Recipe> allRecipeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize reference to database
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //Initialize recipe hash map
        allRecipeData = new HashMap<String, Recipe>();
        // Create ArrayAdapter
        listAdapter = new ArrayAdapter<String>(this, R.layout.row_layout);
        //Create query for recipes
        Query recipesReference = databaseReference.child("recipes").orderByChild("title");
        recipesReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Get Recipe object and use the values to update the UI
                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                listAdapter.add(recipe.getTitle());
                allRecipeData.put(recipe.getTitle(), recipe);
                Log.d(TAG, "loadRecipe:onChildAdded");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousName) {
                // Get Recipe object and use the values to update the UI
                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                listAdapter.remove(previousName);
                listAdapter.add(recipe.getTitle());
                Log.d(TAG, "loadRecipe:onChildChanged");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Get Recipe object and use the values to update the UI
                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                listAdapter.remove(recipe.getTitle());
                allRecipeData.remove(recipe.getTitle());
                Log.d(TAG, "loadRecipe:onRemoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String olden) {
                // Get Recipe object and use the values to update the UI
                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                listAdapter.remove(olden);
                listAdapter.add(recipe.getTitle());
                Log.d(TAG, "loadRecipe:onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Recipe failed, log a message
                Log.w(TAG, "loadRecipe:onCancelled", databaseError.toException());
            }
        });

        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.listViewRecipes );
        //Add list items!
        mainListView.setAdapter( listAdapter );

        //clicking / tapping on each item redirects to Recipe
        mainListView.setOnItemClickListener(this);


        //Floating button to add new recipe
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_recipe);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent transfer = new Intent(RecipesActivity.this, AddRecipeActivity.class);
                startActivity(transfer);
            }
        });

        //If you type, it will automatically start searching
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
        //Floating search button
        FloatingActionButton floatingSearch = (FloatingActionButton) findViewById(R.id.search_recipes);
        floatingSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton addFab = (FloatingActionButton) findViewById(R.id.add_recipe);
                addFab.hide();
                onSearchRequested();
            }
        });

        //Redirects back to Main Menu if press up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.d(TAG, "You clicked " + listAdapter.getItem(position));

        String nameOf = listAdapter.getItem(position);
        Recipe selectedRecipe = allRecipeData.get(nameOf.trim());
        Log.d(TAG, "We found "+selectedRecipe.getTitle());
        // Then you start a new Activity via Intent
        Intent specialTransfer = new Intent(RecipesActivity.this, RecipeInstructActivity.class);
        //Pass selected recipe
        specialTransfer.putExtra("recipe_object", selectedRecipe);

        startActivity(specialTransfer);
    }

}
