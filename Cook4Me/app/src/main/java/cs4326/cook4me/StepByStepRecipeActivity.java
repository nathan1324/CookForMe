package cs4326.cook4me;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Set;

public class StepByStepRecipeActivity extends AppCompatActivity {
    private static final String TAG = "StepRecipeActivity";
    private TextView content;
    private SeekBar starGuide;
    private ArrayList<String> steps;
    private int currentPosition;
    private TextToSpeech speakerTTS;
    private Button dictationButton;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_by_step_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get the data from the Intent
        Bundle dataBundle = getIntent().getExtras();
        recipe = dataBundle.getBundle("bundle").getParcelable("recipe_object");
        Log.d(TAG, recipe.getTitle());
        steps = recipe.getSteps();
        if (steps.get(0) == null){
            steps.remove(0);
        }

        //Get the content box
        content = (TextView)findViewById(R.id.stepInstructionText);
        //Get the star seek bar
        starGuide = (SeekBar)findViewById(R.id.starBar);
        starGuide.setMax(steps.size());
        //Initialize content
        currentPosition = starGuide.getProgress();
        content.setText(steps.get(currentPosition));
        //Make listener for star seek bar
        starGuide.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress >= steps.size()) {
                    //Recipe is finished! Yay!
                    CharSequence winningText = "You've successfully created "+recipe.getTitle()+"!";
                    content.setText(winningText);
                }
                else {
                    //Recipe still ongoing; change on screen text to next step
                    content.setText(steps.get(progress));
                    currentPosition = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "User started touching the StarBar");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "User stopped touching the StarBar");
            }
        });
        // Text To Speech logic
        speakerTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                speakerTTS.setLanguage(Locale.US);
                //speakerTTS.setPitch(1.0);
                //speakerTTS.setSpeechRate((float)1.0);

                // only for lollipop and newer versions
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //by default use the default voice
                    Voice chosenVoice = speakerTTS.getDefaultVoice();
                    Voice secondChoice = speakerTTS.getDefaultVoice();
                    // Get the voices available for this device
                    for(Voice v : speakerTTS.getVoices()) {
                        // Select the highest quality voice if available
                        if(v.getQuality() == Voice.QUALITY_VERY_HIGH) {
                            chosenVoice = v;
                        }
                        if(v.getQuality() == Voice.QUALITY_HIGH) {
                            secondChoice = v;
                        }
                    }
                    // If didn't find very high quality voice, use high quality voice
                    if(!chosenVoice.equals(speakerTTS.getDefaultVoice())) {
                        chosenVoice = secondChoice;
                    }
                    // Set voice for our TTS speaker
                    speakerTTS.setVoice(chosenVoice);
                }
            }
        });
        //Dictate button logic
        dictationButton = (Button) findViewById(R.id.cueDictation);
        dictationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(speakerTTS.isSpeaking()) {
                    return;
                }
                else {
                    //If SDK version lollipop or newer
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        String speechId = "recipeDictation";
                        speakerTTS.speak(content.getText(), TextToSpeech.QUEUE_FLUSH, null, speechId);
                    }
                    else {
                        speakerTTS.speak((String)content.getText(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
        });


        //Floating buttons for next and previous
        FloatingActionButton floatingPrev = (FloatingActionButton) findViewById(R.id.prev_button);
        floatingPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starGuide.incrementProgressBy(-1);
            }
        });
        FloatingActionButton floatingNext = (FloatingActionButton) findViewById(R.id.next_button);
        floatingNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starGuide.incrementProgressBy(1);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        speakerTTS.shutdown();
    }

}