package br.svcdev.weatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import br.svcdev.weatherapp.databinding.ActivityMainBinding;
import br.svcdev.weatherapp.fragments.WeatherCurrentConditions;
import br.svcdev.weatherapp.fragments.WeatherDailyForecast;
import br.svcdev.weatherapp.models.WeatherAppSettings;
import br.svcdev.weatherapp.notifications.Notifications;
import br.svcdev.weatherapp.receivers.AppReceiver;

public class MainActivity extends AppCompatActivity {

	private ActivityMainBinding mBinding;

	private SensorManager mSensorManager;

	private BroadcastReceiver broadcastReceiver = new AppReceiver();

	private Bundle mArgs = new Bundle();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppTheme);
		super.onCreate(savedInstanceState);
		mBinding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(mBinding.getRoot());

		Notifications.initNotificationChannel(this);

		requestSensors();
		loadWeatherAppSettings();
		initAppBar();
		/*
		 * Инициализация слушателя на пункты меню в navigation view. При нажатии на пункт меню,
		 * он остается выделенным.
		 */
		mBinding.navigationView.setNavigationItemSelectedListener(item -> {
			item.setChecked(true);
			return true;
		});

		initWeatherCurrentConditionsFragment();
		initWeatherDailyForecastFragment();
	}

	/**
	 * Метод инициализации сенсоров: TYPE_AMBIENT_TEMPERATURE, TYPE_RELATIVE_HUMIDITY
	 */
	private void requestSensors() {
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) == null &&
				mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) == null) {
			showAlertDialog();
			mArgs.putBoolean("Temperature", false);
			mArgs.putBoolean("Humidity", false);
		} else {
			mArgs.putBoolean("Temperature", true);
			mArgs.putBoolean("Humidity", true);
		}
	}

	private void showAlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Attention").setMessage("There are no temperature" +
				" or humidity sensors on Your device").setCancelable(false)
				.setPositiveButton(R.string.menu_ok, (dialog, id) -> {
				});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	private void loadWeatherAppSettings() {
		SharedPreferences sp = WeatherAppPreferenceUtils.getPreferencesInstance(this);
		WeatherAppSettings settings = WeatherAppSettings.getWeatherAppSettings();
		settings.setTemperatureUnits(sp.getBoolean("temperature_units", false));
		settings.setWindSpeedUnits(sp.getBoolean("speed_units", false));
	}

	/**
	 * Метод инициализирует app bar и подключает слушателя на navigation button, при нажатии
	 * на которую открывается navigation view.
	 */
	private void initAppBar() {
		mBinding.componentAppBar.appBar.setNavigationIcon(R.drawable.ic_menu);
		setSupportActionBar(mBinding.componentAppBar.appBar);
		mBinding.componentAppBar.appBar.setNavigationOnClickListener(view -> mBinding.drawerLayout
				.openDrawer(GravityCompat.START));
	}

	private void initWeatherDailyForecastFragment() {
		Fragment mWeatherDailyForecastFragment = new WeatherDailyForecast();
		mWeatherDailyForecastFragment.setArguments(mArgs);
		FragmentTransaction mWeatherDailyForecastTransaction = getSupportFragmentManager()
				.beginTransaction();
		mWeatherDailyForecastTransaction.replace(R.id.fragment_weather_daily_forecast,
				mWeatherDailyForecastFragment).commit();
	}

	private void initWeatherCurrentConditionsFragment() {
		Fragment mWeatherCurrentConditionsFragment = new WeatherCurrentConditions();
		mWeatherCurrentConditionsFragment.setArguments(mArgs);
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
		switch (item.getItemId()) {
			case R.id.search_location:
				AppbarHandler.onClickItemMenuSearchLocation(this);
				break;
			case R.id.map_activity:
				AppbarHandler.onClickItemMenuMap(this);
				break;
			case R.id.settings:
				AppbarHandler.onClickItemMenuSettings(this);
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
		registerReceiver(broadcastReceiver,
				new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		registerReceiver(broadcastReceiver,
				new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED));
		registerReceiver(broadcastReceiver,
				new IntentFilter(Intent.ACTION_BATTERY_LOW));
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(broadcastReceiver);
	}
}