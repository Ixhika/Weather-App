package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    EditText place;
    Button b1;
    String url = "https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}";
    String apikey = "2eab68ce4b9d034d358655b7c0654366";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.darkBlue));

        place = findViewById(R.id.searchcity);
        b1 = findViewById(R.id.searchButton);

    }

    public void getWeather(View view){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherapi myapi = retrofit.create(weatherapi.class);
        Call<Root> call = myapi.getweather(place.getText().toString(),apikey);

        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if(response.isSuccessful()){
                    Root root = response.body();
                    if(root !=null){
                        System.out.println("City :"+ root.getName());
                        System.out.println("City :"+ root.getMain().getTemp());
                    }
                    else {
                        System.out.println("WeatherResponse is null.");
                    }
                }
                else {
                    // Handle the error
                    System.out.println("Error: " + response.message());
                }

            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {

            }
        });
    }
}