package cs4326.cook4me;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class SteppedInstructionFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Recipe chosenOne;
    private TextView title;
    private TextView timeToCook;
    private Button starTime;

    public SteppedInstructionFragment() {
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

    public static SteppedInstructionFragment newInstance() {
        SteppedInstructionFragment fragment = new SteppedInstructionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle data = getArguments();
            chosenOne = data.getParcelable("recipe_object");
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        // view created from XML layout
        View view = inflater.inflate(R.layout.fragment_steppedinstruction,container, false);



        // could add other customization here for layout components later
        title = (TextView) view.findViewById(R.id.title);
        title.setText(chosenOne.getTitle());
        timeToCook = (TextView) view.findViewById(R.id.cookTime);
        timeToCook.setText(chosenOne.getTimeToCook());
        starTime = (Button) view.findViewById(R.id.startRecipeButton);
        starTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent specialTransfer = new Intent(getActivity(), StepByStepRecipeActivity.class);
                //Pass selected recipe
                Bundle dataBundle = new Bundle();
                dataBundle.putParcelable("recipe_object", chosenOne);
                specialTransfer.putExtra("bundle", dataBundle);
                startActivity(specialTransfer);
                getActivity().finish();
            }
        });



        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

