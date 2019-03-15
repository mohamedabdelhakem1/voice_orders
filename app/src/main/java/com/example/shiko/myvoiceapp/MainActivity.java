package com.example.shiko.myvoiceapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextToSpeech mytts;
    private SpeechRecognizer speechRecognizer ;
    private final static String TAG = "a7a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intializeTextToSpeech();
        intailizeSpeechRecognizer();
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,true);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"say something");
                Log.wtf(TAG,"button clicked");
                speechRecognizer.startListening(intent);
                Log.wtf(TAG,"button 5555");
            }
        });

    }

    private void intailizeSpeechRecognizer() {
        Log.wtf(TAG,"ntailizeSpeechRecognize");

            Log.wtf(TAG,"available");

            speechRecognizer =  SpeechRecognizer.createSpeechRecognizer(this);
             Log.wtf(TAG,"2");

            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    List<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    Log.wtf(TAG,"onresults");
                    processResults(results.get(0));

                }

                @Override
                public void onPartialResults(Bundle bundle) {
                    List<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    Log.wtf(TAG,"onPartialResults");
                    processResults(results.get(0));
                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
        }


    private void processResults(String command) {
        command = command.toLowerCase();
        if(command.indexOf("what") != -1){
            if (command.indexOf("your name")!= -1){
                speak("my name is time");
            }
            else if (command.indexOf("time")!= -1){
                Date date = new Date();
                String time = DateUtils.formatDateTime(this,date.getTime(),DateUtils.FORMAT_SHOW_TIME);
                speak("the time now is"+ time);
            }
        }else if (command.indexOf("open") !=-1){
            if(command.indexOf("browser")!=-1){

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
                startActivity(intent);

            }

        }

    }

    private void intializeTextToSpeech() {
        mytts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (mytts.getEngines().size()==0){
                    Toast.makeText(MainActivity.this,"there is no ttds on your device",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    mytts.setLanguage(Locale.US);
                    speak("hello i am ready");
                    Log.wtf(TAG,"hello iam ready");
                }
            }
        });
    }

    private void speak(String message) {
        if (Build.VERSION.SDK_INT>=21){
            mytts.speak(message ,TextToSpeech.QUEUE_FLUSH,null,null);
            Log.wtf(TAG,"Build.VERSION.SDK_INT>=21");
        } else{
            mytts.speak(message ,TextToSpeech.QUEUE_FLUSH,null);
            Log.wtf(TAG,"Build.VERSION.SDK_INT<21");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mytts.shutdown();
        Log.wtf(TAG,"tts paused");
    }
}
