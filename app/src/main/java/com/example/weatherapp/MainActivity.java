package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;

import android.view.Window;


import com.airbnb.lottie.LottieAnimationView;
import com.example.weatherapp.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    //    String url = "https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}";
//    String apikey = "2eab68ce4b9d034d358655b7c0654366";
    LottieAnimationView lottieAnimationView;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.darkBlue));
        fetchWeatherdata("Jaipur");
        searchCity();

    }

    private void searchCity() {
        SearchView searchView = binding.searchView;
        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null) {
                    fetchWeatherdata(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text change if needed
                return true;
            }
        });
        searchView.setQueryHint("Enter a city");
    }

    private void fetchWeatherdata(String cityName) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .build();

        weatherapi api = retrofit.create(weatherapi.class);

        Call<Root> call = api.getWeatherData(cityName, "2eab68ce4b9d034d358655b7c0654366", "metric");
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (response.isSuccessful()) {
                    Root responseBody = response.body();
                    if (responseBody != null) {
                        double temperature = responseBody.getMain().getTemp();
                        int humdity= responseBody.getMain().getHumidity();
                        int pressure = responseBody.getMain().getPressure();
                        int sunrise = responseBody.getSys().sunrise;
                        int sunset = responseBody.getSys().sunset;
                        double windspeed = responseBody.getWind().speed;
                        double minTemp = responseBody.getMain().getTemp_min();
                        double maxTemp = responseBody.getMain().getTemp_max();
                        String condition;
                        if (responseBody.getWeather() != null && !responseBody.getWeather().isEmpty()) {
                            condition = responseBody.getWeather().get(0).getMain();
                        } else {
                            condition = "unknown";
                        }
                        binding.textView3.setText(String.valueOf(temperature)+"°C");
                        binding.textView4.setText(String.valueOf(condition));
                        binding.textView5.setText(String.valueOf(maxTemp)+"°C");
                        binding.textView6.setText(String.valueOf(minTemp)+"°C");
                        binding.humid.setText(String.valueOf(humdity)+"%");
                        binding.sunRise.setText(String.valueOf(sunrise));
                        binding.sunSet.setText(String.valueOf(sunset));
                        binding.wspeed.setText(String.valueOf(windspeed)+"m/s");
                        binding.cond.setText(String.valueOf(condition));
                        long currentTimeMillis = System.currentTimeMillis();
                        String dayName = getDayName(currentTimeMillis);
                        binding.textView9.setText(dayName);
                        String dateName = getDateName(currentTimeMillis);
                        binding.date.setText(dateName);
                        binding.textView.setText(cityName);

                        changeImageAnimations(condition);

                        // Process temperature or other weather data here
                    }
                } else {
                    // Handle the error
                    System.out.println("Request failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                // Handle the failure
                t.printStackTrace();
            }
        });
    }

    private void changeImageAnimations(String conditons) {
        if(conditons.equals("Haze") || conditons.equals("Clouds")){
            binding.getRoot().setBackgroundResource(R.drawable.img_2);
            binding.lottieAnimationView.setAnimation(R.raw.cloud);
        }
        if(conditons.equals("Sunny") || conditons.equals("Clear")){
            binding.getRoot().setBackgroundResource(R.drawable.img);
            binding.lottieAnimationView.setAnimation(R.raw.sun);
        }
        if(conditons.equals("Rainy") || conditons.equals("Mist")){
            binding.getRoot().setBackgroundResource(R.drawable.img_1);
            binding.lottieAnimationView.setAnimation(R.raw.rain);
        }
        if(conditons.equals("Light Snow") || conditons.equals("Heavy Snow")){
            binding.getRoot().setBackgroundResource(R.drawable.img_3);
            binding.lottieAnimationView.setAnimation(R.raw.snow);
        }

        binding.lottieAnimationView.playAnimation();
    }

    public String getDayName(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    public String getDateName(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

}

