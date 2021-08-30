package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity
{

    EditText getCityName_EditText;
    TextView resultTextView;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCityName_EditText = (EditText) findViewById(R.id.editText);
        resultTextView = (TextView) findViewById(R.id.resultTextView);


    }// end onCreate()

    public void getweather (View view)
    {

        DownloadTask task = new DownloadTask();

        try
        {
            String encodedCityName = URLEncoder.encode(getCityName_EditText.getText().toString(), "UTF-8");

            if ( ! encodedCityName.equals(""))
            {
                task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName +"&appid=f863d5e4e1f90a7a799c134629a4064d");
            }
            else
            {
                Toast.makeText(MainActivity.this, "Could not find weather :(", Toast.LENGTH_LONG).show();
            }

            //        task.execute("https://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=f863d5e4e1f90a7a799c134629a4064d");


            //// Hiding keyboard after the button pressed
            InputMethodManager hideKeyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            hideKeyboard.hideSoftInputFromWindow(getCityName_EditText.getWindowToken(), 0);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Could not find weather :(", Toast.LENGTH_LONG).show();

        }

    }// end GetWeather_Click()

    public class DownloadTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground (String... urls)
        {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try
            {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);


                int data = reader.read();

                while (data != -1)
                {
                    char current = (char) data;
                    result += current;

                    data = reader.read();

                }// end while

                return result;

            }
            catch (Exception e)
            {
                e.printStackTrace();

//                Toast.makeText(Weather.this, "Could not find weather :(", Toast.LENGTH_LONG).show();

                return null;
            }

        }// end doInBackground()

        @Override
        protected void onPostExecute (String s)
        {

            super.onPostExecute(s);

            if(s!=null){

                try
                {
                    JSONObject jsonObject = new JSONObject(s);
                    String weatherInfo = jsonObject.getString("weather");
                    String windInfo = jsonObject.getString("wind");
                    String mainInfo = jsonObject.getString("main");
                    String timezoneInfo = jsonObject.getString("timezone");


                    Double visibility;
                    Log.i("Weather content", weatherInfo);
                    Log.i("wind info", windInfo);

                    Log.i("Weather JSON", weatherInfo);

                    JSONArray arr = new JSONArray(weatherInfo);

                    String main = "";
                    String description = "";
                    String message = "";
                    String speed = "";
                    String deg = "";
                    String TextoTotal = "";
                    String temp = "";
                    String humidity = "";

                    for (int i = 0; i < arr.length(); i++)
                    {
                        JSONObject jsonPart = arr.getJSONObject(i);

                         main = jsonPart.getString("main");
                         description = jsonPart.getString("description");

                        if ( ! main.equals("") && ! description.equals(""))
                        {
                            message += main + " : " + description + "\r\n";
                        } else {
                            resultTextView.setText(" your search was not found check the name ");
                        }

                        Log.i("main", main);
                        Log.i("description", description);


                    }// end for

                    JSONObject windPart = new JSONObject((windInfo));
                    speed = windPart.getString("speed");
                    deg = windPart.getString("deg");

                    JSONObject mainpart = new JSONObject((mainInfo));
                    temp = mainpart.getString("temp");
                    humidity = mainpart.getString("humidity");

                    TextoTotal = message + "\n Speed: " + speed + "\n Degree : " + deg + "\n Temperatura : " + temp + "\n Humidity : " + humidity + "\n Timezone : " + timezoneInfo;

                    if (! TextoTotal.equals(""))
                    {
                        resultTextView.setText(TextoTotal);

                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Could not find weather :(", Toast.LENGTH_LONG).show();
                    }// end if

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Could not find weather :(", Toast.LENGTH_LONG).show();
                }
            }

        }

            }
// end DownloadTask()
}