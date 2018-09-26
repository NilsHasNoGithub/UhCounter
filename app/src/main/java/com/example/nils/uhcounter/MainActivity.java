package com.example.nils.uhcounter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.Buffer;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private int uhCount;
    final private long gifLength = 1000;
    private boolean gifRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gifRunning = false;

        uhCount = getCountFromSave();

        displayUhCountText();


    }

    private void displayUhCountText() {


        TextView textView = (TextView) findViewById(R.id.uhCount);
        textView.setText("James has said uh "+ uhCount + " times.");
    }

    public void setCustomNumber(View view){
        String customNumberString;
        int customNumber;

        EditText editText = (EditText) findViewById(R.id.setNumberField);

        customNumberString = editText.getText().toString();

        try{
            customNumber = Integer.parseInt(customNumberString);
            uhCount = customNumber;
        } catch (Exception e){}

        saveCount(uhCount);
        displayUhCountText();

    }

    public void uhCountPlus(View view){

        uhCount++;
        saveCount(uhCount);
        if(uhCount >= 0) {
            displayUhCountText();
            if (!gifRunning){
            displayGif();
        }
        }

    }

    private void displayGif() {
        View view = findViewById(R.id.gifDisplay);
        viewGif(gifLength);


    }

    private void viewGif(long gifLength) {

        GifImageView gifImageView = (GifImageView) findViewById(R.id.gifDisplay);
        gifRunning = true;

        Runnable gifDeleter = new Runnable() {
            @Override
            public void run() {
                GifImageView gifImageView = (GifImageView) findViewById(R.id.gifDisplay);
                gifImageView.setImageResource(0);
                gifRunning = false;
            }


        };

        Handler handler = new Handler();

        gifImageView.setImageResource(R.drawable.giphy);
        handler.postDelayed(gifDeleter, gifLength);

    }

    public void returnToMainActivity(View view){
        uhCount = 0;
        setContentView(R.layout.activity_main);
        saveCount(uhCount);
        displayUhCountText();
    }


    private void saveCount(int count) {

        if(count < 0){
            setContentView(R.layout.activity_bucket);
        }else{

            String countString = String.valueOf(count);
            writeToFile(countString);
        }

    }

    private void writeToFile(String data){

        try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("uhCount.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }catch (Exception e){}
    }

    private int getCountFromSave(){
        try{
            InputStream inputStream = openFileInput("uhCount.txt");
            if(inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                return Integer.parseInt(bufferedReader.readLine());
            }
            inputStream.close();

        } catch (Exception e){
            return 0;
        }
        return 0;
    }
}
