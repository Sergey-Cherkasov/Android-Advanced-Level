package br.svcdev.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import br.svcdev.weatherapp.data.CitiesDatabase;
import br.svcdev.weatherapp.data.dao.CityDao;
import br.svcdev.weatherapp.data.models.City;
import br.svcdev.weatherapp.databinding.ActivityMainBinding;
import br.svcdev.weatherapp.fragments.WeatherCurrentConditions;
import br.svcdev.weatherapp.fragments.WeatherDailyForecast;
import br.svcdev.weatherapp.models.WeatherAppSettings;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private Toolbar mAppBar;
    private ActionBar mActionBar;

    CitiesDatabase db;
    CityDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

//        TODO: Разобраться в причине ошибки Wrong schema 'cities'
//        db = CitiesDatabase.getDatabase(this);
//        dao = db.getCitiesDao();
//        City[] listCities = dao.getAllCities();

        loadWeatherAppSettings();
        initAppBar();

        /*
         * Инициализован слушатель на пункты меню в navigation view. При нажатии на пункт меню,
         * он остается выделенным.
         */
        mBinding.navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.about:
                    item.setChecked(true);
                    break;
                case R.id.feedback:
                    item.setChecked(true);
                    break;
                default:
                    return false;
            }
            return true;
        });

        initWeatherCurrentConditionsFragment();
        initWeatherDailyForecastFragment();
    }

    private void loadWeatherAppSettings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        WeatherAppSettings settings = WeatherAppSettings.getWeatherAppSettings();
        settings.setTemperatureUnits(sp.getBoolean("temperature_units", false));
        settings.setWindSpeedUnits(sp.getBoolean("speed_units", false));
    }

    /** Метод инициализирует app bar и подключает слушателя на navigation button, при нажатии
     * на которую открывается navigation view.
     */
    private void initAppBar() {
        mAppBar = mBinding.componentAppBar.appBar;
        mAppBar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(mAppBar);
        mAppBar.setNavigationOnClickListener(view -> mBinding.drawerLayout
                .openDrawer(GravityCompat.START));
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
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Реализован метод выбора действий при нажатии на пункт меню app bar.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.search_location:
                intent = new Intent(this, SearchLocationActivity.class);
                startActivity(intent);
                break;
            case R.id.settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


}