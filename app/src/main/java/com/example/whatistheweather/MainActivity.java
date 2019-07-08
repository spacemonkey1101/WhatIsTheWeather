package com.example.whatistheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button getWeatherButton;
    TextView resultTextView;
    EditText cityEditText;


   public void getWeather(View view){

       try {
           InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
           inputMethodManager.hideSoftInputFromWindow(cityEditText.getWindowToken(),0) ;//0 is a flag
                             // to remove keypad after input is taken
                             //to see the result without minizing the keyboard
           DownloadTask downloadTask = new DownloadTask();

           downloadTask.execute("https://api.openweathermap.org/data/2.5/weather?q="+cityEditText.getText().toString()+"&appid=ca4614c2cf916fc18df579f91f85926f");

       } catch (Exception e) {
           e.printStackTrace();
       }
   }
    public class DownloadTask extends AsyncTask<String, Void, String> {

        URL url;
        HttpURLConnection httpURLConnection = null;
        String result = "";

        @Override
        protected String doInBackground(String... strings) {

            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                while (data != -1) {
                    result = result + (char) data;
                    data = inputStreamReader.read();
                }
                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {//Called when doInBackGround() has completed
            super.onPostExecute(s);             //and result is passed to this method as "s"

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weather = jsonObject.getString("weather");

                JSONArray jsonArray = new JSONArray(weather);
                resultTextView.setVisibility(View.VISIBLE);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    if( jsonPart.getString("main") != "" && jsonPart.getString("description") != "")
                    resultTextView.setText( jsonPart.getString("main") + ":" + jsonPart.getString("description"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultTextView.setText( "Please enter a valid city name");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWeatherButton = findViewById(R.id.button);
        resultTextView = findViewById(R.id.resultTextView);
        cityEditText = findViewById(R.id.editText);



    }
}
