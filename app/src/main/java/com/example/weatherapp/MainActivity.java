package com.example.weatherapp;

import androidx.core.view.accessibility.AccessibilityWindowInfoCompat;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telecom.Call;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import com.example.weatherapp.Models.WeatherData;
import com.example.weatherapp.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Callback;

import retrofit2.Callback;
import retrofit2.Response;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//d37e2fc136297f98e697fc5f72f3cb07
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.darkBlue));

        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        String currentdate = format.format(new Date());

        binding.date.setText(currentdate);
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(binding.searchcity.getText().toString())){
                    binding.searchcity.setError("Enter City Name ");
                    return;
                }
                fetchWeather("hi");
            }
        });

    }
    void fetchWeather(String cityname){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InterfaceApi interfaceApi = retrofit.create(InterfaceApi.class);

        Call<WeatherData> call = interfaceApi.getData(cityname, "d37e2fc136297f98e697fc5f72f3cb07", "metric");
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    // Handle a successful response here, such as updating UI with weather data
                    WeatherData weatherData = response.body();
                    if (weatherData != null) {
                        // Update your UI with weather data here
                    }
                } else {
                    // Handle an unsuccessful response here, such as showing an error message
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                // Handle network failure here, such as showing an error message
            }
        });


    }
}