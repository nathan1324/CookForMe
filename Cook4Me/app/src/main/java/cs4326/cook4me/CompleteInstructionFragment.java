package cs4326.cook4me;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CompleteInstructionFragment extends Fragment {
    private static final String TAG = "CompleteInstrFragment";

    private OnFragmentInteractionListener mListener;
    private DatabaseReference databaseReference;
    private Recipe chosenOne;

    public CompleteInstructionFragment() {
        // required empty constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle data = getArguments();
            chosenOne = data.getParcelable("recipe_object");
        }
    }

    public static CompleteInstructionFragment newInstance() {
        CompleteInstructionFragment fragment = new CompleteInstructionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        //Initialize reference to database
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // view created from XML layout
        View view = inflater.inflate(R.layout.fragment_completeinstruction, container, false);

        //Set term name as title of page
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(chosenOne.getTitle());

        //And put down technique description
        TextView ingredients = (TextView) view.findViewById(R.id.ingredients);
        String ingredientString = "";
        for(String ingredient : chosenOne.getIngredients()) {
            if(ingredient != null) {
                ingredientString = ingredientString.concat(ingredient);
                ingredientString = ingredientString.concat("\n");
            }
        }
        ingredients.setText(ingredientString);

        TextView cookTime = (TextView) view.findViewById(R.id.cookTime);
        cookTime.setText(chosenOne.getTimeToCook());

        TextView steps = (TextView) view.findViewById(R.id.steps);
        String stepsString = "";
        int i = 1;
        for(String step : chosenOne.getSteps()) {
            if(step != null) {
                stepsString = stepsString.concat(i+". "+step+"\n");
                i++;
            }
        }
        steps.setText(stepsString);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
