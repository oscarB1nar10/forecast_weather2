package com.example.user.forecast_weather;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    private TextView temp, day_information, windSpeed, daySummary;
    private ImageView imageWeather;


    int vw=0;
    UserLocation2 usl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //binding= DataBindingUtil.setContentView(this,R.layout.main_activity);

        temp=findViewById(R.id.temperature);
        day_information= findViewById(R.id.day_information);
        imageWeather=findViewById(R.id.image_weather);
        windSpeed=findViewById(R.id.wind_speed);
        daySummary=findViewById(R.id.day_summary);

        usl=new UserLocation2(this);
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
                    temp.setText(""+ftem+"°");

                    windSpeed.setText(""+((int)mainObject.getDouble("windSpeed")*1.852)+" K/H");

                    //zone, place and day details
                    Calendar calendar= Calendar.getInstance();
                    String dayOfTheWeek = (String) DateFormat.format("EEEE",calendar); // day
                    String dayNumber  = (String) DateFormat.format("dd",   calendar); // day number
                    day_information.setText(response.getString("timezone")+"\n"+dayOfTheWeek+": "+dayNumber);

                    //weather
                    imageWeather.setImageResource(evaluateTheWeather(mainObject.getString("icon")));

                    String daySummarys=getDaySummary();
                    daySummary.setText("Humidity: "+(mainObject.getDouble("humidity")*100)+"%"+"\n"+daySummarys);

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
            vw=1;
            return R.drawable.clear;
        }else if(weatherExpression.contains("cloudy")){
            vw=2;
            return  R.drawable.cloudy;
        }else{
            vw=3;
            return R.drawable.rain;
        }


    }

    public  String getDaySummary(){
        switch (vw){
            case 1:{
                return "Dia despejado";
            }
            case 2:{
                return "Dia nublado";
            }
            case  3:{
                return "Dia lluvioso";
            }
            default: {
                return "Dia nublado";
            }
        }
    }

}
