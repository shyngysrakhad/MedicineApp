package com.a4devspirit.a1.medicineapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Tips extends Activity {
    OkHttpClient client;
    TextView cityName, time, detail, temp, pressure, humidity;
    String currentCity;
    ImageView imageIcon;
    TextView tips;
    int tmp, hmd, pres;
    String weather_description;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference data = FirebaseDatabase.getInstance().getReference();
    private static final String TAG = "Weather";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        initialization();
        SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        currentCity = pref.getString("city","");
        Request request = new Request.Builder()
                .url("https://weather.cit.api.here.com/weather/1.0/report.json?product=observation&name=" + pref.getString("city","") + "&app_id=DemoAppId01082013GAL&app_code=AJKnXv84fjrb0KIHawS0Tg")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Tips.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            JSONObject object = new JSONObject(result);
                            JSONObject obser = object.getJSONObject("observations");
                            JSONArray array = obser.getJSONArray("location");
                            JSONObject last = array.getJSONObject(0);
                            JSONArray info = last.getJSONArray("observation");
                            JSONObject about = info.getJSONObject(0);
                            Log.d(TAG, "logggggg" + about.toString());
                            cityName.setText(about.get("city").toString());
                            time.setText(about.get("utcTime").toString().substring(0,11));
                            detail.setText(about.get("description").toString());
                            temp.setText(about.get("temperature").toString() + " C");
                            humidity.setText("Humidity: " + about.get("humidity").toString() + "%");
                            pressure.setText("Pressure: " + about.get("barometerPressure").toString());
                            pres = Integer.parseInt(about.get("barometerPressure").toString().substring(0,4));
                            char char_a = about.get("temperature").toString().charAt(1);
                            if (char_a == '.') {
                                tmp = Integer.parseInt(about.get("temperature").toString().substring(0, 1));
                            }else tmp = Integer.parseInt(about.get("temperature").toString().substring(0, 2));
                            hmd = Integer.parseInt(about.get("humidity").toString().substring(0, 2));
                            weather_description = about.get("description").toString();
                            tips(tmp, pres, hmd, about.get("description").toString());
                            Icon(40,"clear sky",R.drawable.ava_boy_30_sun, tmp);
                            Icon(40,"rain",R.drawable.ava_boy_30_rain,tmp);
                            Icon(40,"",R.drawable.ava_boy_30,tmp);
                            Icon(30,"clear sky",R.drawable.ava_boy_25_sun,tmp);
                            Icon(30,"rain",R.drawable.ava_boy_25_rain,tmp);
                            Icon(30,"",R.drawable.ava_boy_25,tmp);
                            Icon(25,"clear sky",R.drawable.ava_boy_20_sun,tmp);
                            Icon(25,"rain",R.drawable.ava_boy_20_rain,tmp);
                            Icon(25,"",R.drawable.ava_boy_20,tmp);
                            Icon(18,"clear sky",R.drawable.ava_boy_15_sun,tmp);
                            Icon(18,"rain",R.drawable.ava_boy_15_rain,tmp);
                            Icon(18,"",R.drawable.ava_boy_15,tmp);
                            Icon(15,"clear sky",R.drawable.ava_boy_10_sun,tmp);
                            Icon(15,"rain",R.drawable.ava_boy_10_rain,tmp);
                            Icon(15,"",R.drawable.ava_boy_10,tmp);
                            Icon(10,"rain",R.drawable.ava_boy_5,tmp);
                            Icon(10,"",R.drawable.ava_boy_5,tmp);
                            Icon(5,"rain",R.drawable.ava_boy_0_rain,tmp);
                            Icon(5,"",R.drawable.ava_boy_0,tmp);
                            Icon(-10,"",R.drawable.ava_boy_5_,tmp);
                            Icon(-15,"",R.drawable.ava_boy_10_,tmp);
                            Icon(-20,"",R.drawable.ava_boy_15_,tmp);
                            Icon(-25,"",R.drawable.ava_boy_20_,tmp);
                            Icon(-40,"",R.drawable.ava_boy_30_,tmp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    public void tips(int temp, int humidity, int pressure, String weather_descr){
        if (temp<0){
            text("Do not forget to wear warmly, atmosphere temperature is under 0");
        }
        if (weather_descr.contains("rain")){
            text("Do not forget to take a umbrella\n" +
                    "        _____");
        }
        if (weather_descr.contains("clear sky") && temp > 15){
            text("Do not forget to take a sunglasses to protect the eyes from sunlight and ultraviolet rays\n" +
                    "        _____");
        }
        if (humidity<30){
            text("Humidity is below normal, drink a water!. Be careful bacterias and viruses because of drying of the mucous membranes.\n" +
                    "        _____");
        }
        if (pressure>800){
            text("Atmosphere pressure is above normal. Warning:\n" +
                    "1) Headache\n" +
                    "2) Dizziness\n" +
                    "3) Nausea\n" +
                    "        _____");
        }
        if (pressure<800){
            text("Atmosphere pressure is under normal. Warning:\n" +
                    "1) General weakness\n" +
                    "2) Labored breathing\n" +
                    "3) Sense of lack of air due to a decrease in the amount of oxygen in the atmosphere\n" +
                    "        _____");
        }
    }
    public void Icon(int i, String description, int ic, int c){
        if (c<i && weather_description.contains(description)){
            imageIcon.setImageDrawable(getResources().getDrawable(ic));
        }
    }
    private void initialization(){
        client = new OkHttpClient();
        imageIcon = (ImageView) findViewById(R.id.image_icon);
        tips = (TextView) findViewById(R.id.text_tips);
        cityName = (TextView) findViewById(R.id.city_field);
        time = (TextView) findViewById(R.id.updated_field);
        detail = (TextView) findViewById(R.id.details_field);
        temp = (TextView) findViewById(R.id.current_temperature_field);
        pressure = (TextView) findViewById(R.id.pressure_field);
        humidity = (TextView) findViewById(R.id.humidity_field);
    }
    public void text(String a){
        if (tips.getText().toString().equals("")){
            tips.setText(a + "\n");
        }else tips.setText(tips.getText().toString()+"\n" + a);
    }
}
