package br.svcdev.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomappbar.BottomAppBar;

import br.svcdev.weatherapp.databinding.ActivityMainBinding;
import br.svcdev.weatherapp.fragments.WeatherCurrentConditions;
import br.svcdev.weatherapp.fragments.WeatherDailyForecast;
import br.svcdev.weatherapp.models.WeatherAppSettings;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private BottomAppBar mBottomAppBar;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        loadWeatherAppSettings();
        initBottomAppBar();
        initWeatherCurrentConditionsFragment();
        initWeatherDailyForecastFragment();
    }

    private void loadWeatherAppSettings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        WeatherAppSettings settings = WeatherAppSettings.getWeatherAppSettings();
        settings.setTemperatureUnits(sp.getBoolean("temperature_units", false));
        settings.setWindSpeedUnits(sp.getBoolean("speed_units", false));
    }

    private void initBottomAppBar() {
        mBottomAppBar = mBinding.bottomAppBar;
        mBottomAppBar.setNavigationIcon(R.drawable.ic_location_city);
        setSupportActionBar(mBottomAppBar);
        mBottomAppBar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SearchLocationActivity.class);
            startActivity(intent);
        });
        mActionBar = getSupportActionBar();
    }

    private void initWeatherDailyForecastFragment() {
        Fragment mWeatherDailyForecastFragment = new WeatherDailyForecast();
        FragmentTransaction mWeatherDailyForecastTransaction = getSupportFragmentManager()
                .beginTransaction();
        mWeatherDailyForecastTransaction.replace(R.id.fragment_weather_daily_forecast,
                mWeatherDailyForecastFragment).commit();
    }

    private void initWeatherCurrentConditionsFragment() {
        Fragment mWeatherCurrentConditionsFragment = new WeatherCurrentConditions();
        FragmentTransaction mWeatherCurrentConditionsTransaction = getSupportFragmentManager()
                .beginTransaction();
        mWeatherCurrentConditionsTransaction.replace(R.id.fragment_weather_current_conditions,
                mWeatherCurrentConditionsFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}