package cs4326.cook4me;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;


public class RecipeInstructActivity extends AppCompatActivity implements
        CompleteInstructionFragment.OnFragmentInteractionListener,
        SteppedInstructionFragment.OnFragmentInteractionListener {

    private static final String TAG = "RecipesActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static final String KEY = "recipe_object";
    private Recipe theRecipe;

    /**
     * The ViewPager that hosts the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_recipe_instruct);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bund = getIntent().getExtras();
        theRecipe = bund.getParcelable("recipe_object");
        Log.d(TAG, "Parcelable " + theRecipe.getTitle());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //Redirects back to Recipe Menu if press up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Bundle data = new Bundle();
            data.putParcelable(KEY, theRecipe);
            switch (position) {
                case 0: {
                    fragment = new CompleteInstructionFragment();
                    break;
                }
                case 1: {
                    fragment = new SteppedInstructionFragment();
                    break;
                }
            }
            try {
                fragment.setArguments(data);
            } catch(Exception e) {
                Log.e(TAG, e.getMessage());
                finish();
            }

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Full Recipe";
                case 1:
                    return "Step-by-Step";
            }
            return null;
        }
    }
}
