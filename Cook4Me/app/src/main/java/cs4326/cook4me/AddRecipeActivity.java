package cs4326.cook4me;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddRecipeActivity extends AppCompatActivity {
    private static final String TAG = "AddRecipeActivity";

    // firebase authentication object
    private FirebaseAuth firebaseAuth;

    // firebase database reference
    private DatabaseReference databaseReference;

    private ProgressDialog mineProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        //If not authenticated, leave
        if (firebaseAuth.getCurrentUser() == null){
            Log.d(TAG, "User not authenticated");
            //Build alert!!
            AlertDialog.Builder builder = new AlertDialog.Builder(AddRecipeActivity.this);
            builder.setMessage(R.string.permission_error_msg)
                    .setTitle(R.string.permission_error_title)
                    .setIcon(android.R.drawable.ic_dialog_alert);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    redirectLogin();
                }
            });
            //Add button
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    Log.d(TAG, "User was good, clicked OK");
                }
            });
            //Create and display alert
            builder.create().show();
        }
        else {
            //I am authenticated! Yay!
            final EditText recipeTitleView = (EditText)findViewById(R.id.add_recipe_input1);
            final EditText timeToCookView = (EditText)findViewById(R.id.add_recipe_input2);
            final EditText ingredientsView = (EditText)findViewById(R.id.add_recipe_input3);
            final EditText stepsView = (EditText)findViewById(R.id.add_recipe_input4);

            Button submitButton = (Button)findViewById(R.id.add_recipe_submit_button);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mineProgress = new ProgressDialog(AddRecipeActivity.this);
                    mineProgress.setMessage("Validating data...");
                    mineProgress.show();
                    //Validate data
                    //Is it empty?
                    if(contentIsEmpty(recipeTitleView) || contentIsEmpty(timeToCookView)
                            || contentIsEmpty(ingredientsView) || contentIsEmpty(stepsView)) {
                        //Send message that content is empty
                        Toast msg = Toast.makeText(AddRecipeActivity.this,
                                "Please fill out all fields",
                                Toast.LENGTH_SHORT);
                        mineProgress.hide();
                        msg.show();
                        return;
                    }
                    //Is the time a number?
                    String time = getStringOfInput(timeToCookView);
                    if(!isNumerical(time)) {
                        Toast.makeText(AddRecipeActivity.this,
                                "Please enter a number at the beginning of the time to cook field.",
                                Toast.LENGTH_SHORT).show();
                        mineProgress.hide();
                        return;
                    }
                    //Do the steps start with numbers?
                    String steps = getStringOfInput(stepsView).trim();
                    if(!steps.startsWith("1.")) {
                        Toast.makeText(AddRecipeActivity.this,
                                "Please prefix all steps with a number followed by a period." +
                                        "\nExample: 1. Crack egg into small bowl.",
                                Toast.LENGTH_SHORT).show();
                        mineProgress.hide();
                        return;
                    }
                    mineProgress.setMessage("Storing data...");
                    //OK Data is valid! Time to store it!
                    Recipe newRecipe = new Recipe();
                    newRecipe.setTitle(getStringOfInput(recipeTitleView));
                    newRecipe.setTimeToCook(time);
                    //Parse through Ingredients for commas
                    String[] ingredients = getStringOfInput(ingredientsView).trim().split(",");
                    ArrayList<String> ingredientList = new ArrayList<String>();
                    for(int i = 0; i < ingredients.length; i++) {
                        ingredients[i] = ingredients[i].trim();
                        ingredients[i] = ingredients[i].replace(",","");
                        ingredients[i] = ingredients[i].replaceAll("\\n", " ");
                        ingredientList.add(ingredients[i]);
                    }
                    newRecipe.setIngredients(ingredientList);
                    //Parse through Steps for #.
                    String[] stepArr = steps.split("[0-9]\\.");
                    ArrayList<String> stepsList = new ArrayList<String>();
                    for(int i=0; i < stepArr.length; i++) {
                        stepArr[i] = stepArr[i].trim();
                        stepArr[i] = stepArr[i].replaceAll("\\n", " ");
                        stepsList.add(stepArr[i]);
                    }
                    newRecipe.setSteps(stepsList);

                    //RECIPE COMPLETE! ADD TO DATABASE
                    DatabaseReference ref = databaseReference.child("recipes");
                    Map<String, Object> addedContent = new HashMap<String, Object>();
                    addedContent.put(getStringOfInput(recipeTitleView).replace(" ", ""), newRecipe);
                    ref.updateChildren(addedContent);

                    Toast.makeText(AddRecipeActivity.this, "Data successfully submitted to database.", Toast.LENGTH_LONG).show();

                    //Time to leave!
                    finish();
                    Intent transfer = new Intent(AddRecipeActivity.this, RecipesActivity.class);
                    startActivity(transfer);

                }
            });
        }
        //Initialize Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Back button goes back to recipe page
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void redirectLogin() {
        finish();
        startActivity(new Intent(AddRecipeActivity.this, LoginActivity.class));
    }

    private boolean contentIsEmpty(EditText viewOne) {
        String content = viewOne.getText().toString().trim();
        return TextUtils.isEmpty(content);
    }
    private String getStringOfInput(EditText viewTwo) {
        return viewTwo.getText().toString().trim();
    }
    private boolean isNumerical(String s) {
        boolean isTrue = false;

        for(int i = 1; i < 10; i++) {
            if(s.startsWith(Integer.toString(i))) {
                isTrue = true;
            }
        }

        return isTrue;
    }


}
