package com.igalata.bubblepickerdemo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private static String number;
    private static String category;
    private static String category_substring;
    private FirebaseAuth mAuth;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private com.github.nkzawa.socketio.client.Socket mSocket;
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // getActionBar().hide();
        category = getIntent().getStringExtra("hello");
        int index = category.indexOf(":");
        number = category.substring(index);
        category_substring=category.substring(0,index);


        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, category, duration);
        toast.show();

        String message = "Please record a description of your problem!";
        //box.setText(message);

        ImageButton mic = findViewById(R.id.fab);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listen();
            }
        });
    }

    private void listen() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please state your problem.");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView results = findViewById(R.id.textView2);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    results.setText(result.get(0));
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myref = database.getReference();
                    mAuth= FirebaseAuth.getInstance();
                    HashMap<String, String> meMap=new HashMap<String, String>();
                    meMap.put("category",category_substring);
                    meMap.put("text",result.get(0));
                    meMap.put("number",number);
                    myref.child("UniqueKey").push().setValue(meMap);


                }

            }

        }
    }

    }



