package com.example.user.forecast_weather;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DailyWeather extends Fragment{

    public TextView days[];
    public ImageView icon[];
    public TextView degrees[];

    public TextView d_m, d_t, d_w, d_th, d_f;
    public ImageView i_m, i_t, i_w, i_th, i_f;
    public TextView de_m, de_t, de_w, de_th, de_f;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.days_fragment,container,false);
        addDays(view);
        addIcons(view);
        addDegrees(view);

        weekWeather();

        return view;
    }

    public void addDays(View v){

        d_m=v.findViewById(R.id.monday);
        d_t=v.findViewById(R.id.tuesday);
        d_w=v.findViewById(R.id.wednesday);
        d_th=v.findViewById(R.id.thursday);
        d_f=v.findViewById(R.id.friday);

        days=new TextView[5];
        days[0]=d_m;
        days[1]=d_t;
        days[2]=d_w;
        days[3]=d_th;
        days[4]=d_f;






    }
    public void addIcons(View v){

        i_m=v.findViewById(R.id.mon_icon);
        i_t=v.findViewById(R.id.tue_icon);
        i_w=v.findViewById(R.id.wed_icon);
        i_th=v.findViewById(R.id.thu_icon);
        i_f=v.findViewById(R.id.fri_icon);

        icon=new ImageView[5];
        icon[0]=i_m;
        icon[1]=i_t;
        icon[2]=i_w;
        icon[3]=i_th;
        icon[4]=i_f;



    }

    public void addDegrees(View v){

        de_m=v.findViewById(R.id.mon_degrees);
        de_t=v.findViewById(R.id.tue_degrees);
        de_w=v.findViewById(R.id.wed_degrees);
        de_th=v.findViewById(R.id.thu_degrees);
        de_f=v.findViewById(R.id.fri_degrees);

        degrees=new TextView[5];
        degrees[0]=de_m;
        degrees[1]=de_t;
        degrees[2]=de_w;
        degrees[3]=de_th;
        degrees[4]=de_f;

    }

    private void weekWeather() {
        String url="https://api.darksky.net/forecast/63978648924e466d3f85271c3b02c11b/4.624335,-74.063644";

        JsonObjectRequest jor=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject mainObject= response.getJSONObject("daily");
                    JSONArray jsonArray=mainObject.getJSONArray("data");
                    evaluateDays(jsonArray);



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

        RequestQueue queue= Volley.newRequestQueue(getContext());
        queue.add(jor);

    }

    private void evaluateDays(JSONArray jsonArray) {
        int accumulator=0;
        for(int i=0; i<jsonArray.length(); i++){
            try {
                String iconValue=jsonArray.getJSONObject(i).getString("icon");
                int minTem=jsonArray.getJSONObject(i).getInt("temperatureMin");
                int maxTem=jsonArray.getJSONObject(i).getInt("temperatureMax");

                int averageTemp=(int)((((minTem+maxTem)/2)-32)*5)/9;

                System.out.println("iconValue: "+iconValue+", averageTemp: "+averageTemp);


                //zone, place and day details
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar= Calendar.getInstance();
                calendar.add(Calendar.DATE,accumulator);
                String dayOfTheWeek = (String) DateFormat.format("EEEE",calendar); // day

                days[accumulator].setText(""+dayOfTheWeek);
                icon[accumulator].setImageResource(evaluateTheWeather(iconValue));
                degrees[accumulator].setText(""+averageTemp+"°");



                accumulator++;
                    if (accumulator==5){
                        break;
                    }

                //dailyWeather[accumulator]=new DailyWeather(iconValue,averageTemp);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
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
