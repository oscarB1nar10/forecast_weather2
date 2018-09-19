package com.example.user.forecast_weather;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class Weather extends AppCompatActivity {

    private TextView temp, day_information;
    private ImageView imageWeather;

    UserLocation usl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //binding= DataBindingUtil.setContentView(this,R.layout.main_activity);

        temp=findViewById(R.id.temperature);
        day_information= findViewById(R.id.day_information);
        imageWeather=findViewById(R.id.image_weather);
        usl=new UserLocation(this);
        findWeather();
    }



    private void findWeather() {
        String latitude=""+usl.latitude;
        String longitud=""+usl.longitude;

        System.out.println("latitude: "+latitude+",longitude: "+longitud);
        String url="https://api.darksky.net/forecast/63978648924e466d3f85271c3b02c11b/"+latitude+","+longitud;

        JsonObjectRequest jor=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject mainObject= response.getJSONObject("currently");

                    double  temperature=mainObject.getDouble("temperature");
                    temperature=((temperature-32)*5)/9;
                    int ftem=(int)Math.round(temperature);
                    temp.setText(ftem+"°");


                    //zone, place and day details
                    Calendar calendar= Calendar.getInstance();
                    String dayOfTheWeek = (String) DateFormat.format("EEEE",calendar); // day
                    String dayNumber  = (String) DateFormat.format("dd",   calendar); // day number
                    day_information.setText(response.getString("timezone")+"\n"+dayOfTheWeek+": "+dayNumber);

                    //weather
                    imageWeather.setImageResource(evaluateTheWeather(mainObject.getString("icon")));

                    /*DailyWeather dw=new DailyWeather(evaluateTheWeather(mainObject.getString("icon")),ftem,
                            response.getString("timezone")+"\n"+dayOfTheWeek+": "+dayNumber);

                    binding.setItem(dw);*/


                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Error: "+e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(jor);
    }

    public int evaluateTheWeather(String weatherExpression){


        if(weatherExpression.contains("clear")){
            return R.drawable.clear;
        }else if(weatherExpression.contains("cloudy")){
            return  R.drawable.cloudy;
        }else{
            return R.drawable.rain;
        }


    }

}
