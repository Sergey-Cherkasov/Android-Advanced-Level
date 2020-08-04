package br.svcdev.weatherapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import br.svcdev.weatherapp.Constants;
import br.svcdev.weatherapp.R;
import br.svcdev.weatherapp.databinding.FragmentWeatherCurrentConditionsBinding;
import br.svcdev.weatherapp.models.weather.CurrentWeather;
import br.svcdev.weatherapp.network.HostRequestConstants;
import br.svcdev.weatherapp.network.NetworkUtils;
import br.svcdev.weatherapp.network.SendRequest;
import br.svcdev.weatherapp.network.ServerResponse;

public class WeatherCurrentConditions extends Fragment implements ServerResponse {

    private FragmentWeatherCurrentConditionsBinding mBinding;
    private SharedPreferences sp;
    private String mCityName;
    private float mTemperature;
    private int mAirPressure;
    private int mHumidity;
    private float mWindSpeed;
    private String mIconId;

    private SensorManager mSensorManager;

    /* Сенсоры темературы и влажности*/
    private Sensor mTemperatureSensor;
    private Sensor mHumiditySensor;

    /**
     * Объявление и инициализация слушателя для сенсора TYPE_AMBIENT_TEMPERATURE
     */
    private SensorEventListener mTempListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float tempValue = event.values[0];
            mBinding.componentWeatherCurrentConditionsCurrentTemperature
                    .tvFragmentWeatherCurrentConditionsTemperature.setText(String.valueOf(tempValue));
            Log.d(Constants.TAG_APP, "onSensorChanged: tempValue = " + tempValue);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    /**
     * Объявление и инициализация слушателя для сенсора TYPE_RELATIVE_HUMIDITY
     */
    private SensorEventListener mHumidityListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float humidityValue = event.values[0];
            Log.d(Constants.TAG_APP, "onSensorChanged: humidityValue = " + humidityValue);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        * Если сенсоры температуры и влажности имеются у устройства, то инициализируем их.
        * В противном случае отправляем запрос на погодный сервис.
        */
        if (isSensorsPresent()) {
            mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
            mTemperatureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            mHumiditySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        } else {
            onSendRequest();
        }
    }

    private boolean isSensorsPresent() {
        if (getArguments() != null){
//            return getArguments().getBoolean("Temperature")
//                    && getArguments().getBoolean("Humidity");
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentWeatherCurrentConditionsBinding.inflate(inflater, container,
                false);
        sp = PreferenceManager.getDefaultSharedPreferences(mBinding.getRoot().getContext());
        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        onAssignValuesToFields(mCityName, mTemperature, mAirPressure, mHumidity, mWindSpeed,
                mIconId);
    }

    private void onSendRequest() {
        Map<String, Object> requestParameters = new HashMap<>();
        int locationId = 1503901;
        String units = "metric";
        String languageCode = getResources().getString(R.string.language);
        requestParameters.put("id", locationId);
        requestParameters.put("units", units);
        requestParameters.put("lang", languageCode);

        SendRequest sendRequest = new SendRequest(getParentFragmentManager(),
                HostRequestConstants.REQUEST_CONTROLLER_CURRENT_CONDITIONS,
                requestParameters,
                HostRequestConstants.REQUEST_METHOD,
                HostRequestConstants.REQUEST_CONTROLLER_CURRENT_CONDITIONS);
        sendRequest.execute();
    }

    @Override
    public void onServerResponse(Map<String, String> response) {
        Iterator<String> key = response.keySet().iterator();
        String requestId = key.next();
        String responseString = response.get(requestId);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        if (requestId.equals(HostRequestConstants.REQUEST_CONTROLLER_CURRENT_CONDITIONS)) {
            CurrentWeather currentWeather = gson.fromJson(responseString, CurrentWeather.class);
            mCityName = currentWeather.getCityName();
            mTemperature = currentWeather.getMain().getTemp();
            mAirPressure = currentWeather.getMain().getPressure();
            mHumidity = currentWeather.getMain().getHumidity();
            mWindSpeed = currentWeather.getWind().getSpeed();
            mIconId = currentWeather.getWeathers()[0].getIcon();
            onAssignValuesToFields(mCityName, mTemperature, mAirPressure, mHumidity, mWindSpeed,
                    mIconId);
        }
    }

    private void onAssignValuesToFields(String cityName, float temperature, int airPressure,
                                        int humidity, float windSpeed, String iconId) {
        mBinding.componentWeatherCurrentConditionsTitle
                .tvFragmentWeatherCurrentConditionsTitleCity
                .setText(cityName);
        if (sp.getBoolean("temperature_units", false)) {
            mBinding.componentWeatherCurrentConditionsCurrentTemperature
                    .tvFragmentWeatherCurrentConditionsTemperature
                    .setText(String.format(Locale.ENGLISH, "%.1f",
                            celciusToFahrenheit(temperature)));
            mBinding.componentWeatherCurrentConditionsCurrentTemperature
                    .ivFragmentWeatherCurrentConditionsTemperatureUnits
                    .setImageResource(R.drawable.ic_fahrenheit);
        } else {
            mBinding.componentWeatherCurrentConditionsCurrentTemperature
                    .tvFragmentWeatherCurrentConditionsTemperature
                    .setText(String.format(Locale.ENGLISH, "%.0f", temperature));
            mBinding.componentWeatherCurrentConditionsCurrentTemperature
                    .ivFragmentWeatherCurrentConditionsTemperatureUnits
                    .setImageResource(R.drawable.ic_celsius);
        }

        NetworkUtils.loadImage(iconId, mBinding.componentWeatherCurrentConditionsCurrentTemperature
                .ivFragmentWeatherCurrentConditionsCloudiness);

        mBinding.componentWeatherCurrentConditionsAtmosphericsIndicators
                .tvFragmentWeatherCurrentConditionsAirPressure
                .setText(String.format(Locale.ENGLISH, "%d %s",
                        airPressure, getResources().getString(R.string.air_pressure_units)));
        mBinding.componentWeatherCurrentConditionsAtmosphericsIndicators
                .tvFragmentWeatherCurrentConditionsHumidity
                .setText(String.format(Locale.ENGLISH, "%d %s",
                        humidity, getResources().getString(R.string.percent)));
        String speedUnits = "";
        if (sp.getBoolean("speed_units", false)) {
            mBinding.componentWeatherCurrentConditionsAtmosphericsIndicators
                    .tvFragmentWeatherCurrentConditionsWindSpeed
                    .setText(String.format(Locale.ENGLISH, "%.0f %s",
                            metersPerSecondToMilesPerHour(windSpeed),
                            getResources().getString(R.string.speed_summary_on)));
        } else {
            mBinding.componentWeatherCurrentConditionsAtmosphericsIndicators
                    .tvFragmentWeatherCurrentConditionsWindSpeed
                    .setText(String.format(Locale.ENGLISH, "%.0f %s", windSpeed,
                            getResources().getString(R.string.speed_summary_off)));
        }
    }

    private float celciusToFahrenheit(float temp) {
        return temp * 9 / 5 + 32;
    }

    private float metersPerSecondToMilesPerHour(float windSpeed) {
        return windSpeed * 2.237f;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*
         * Проверяем на наличие сенсоров.
         * Регистрируем слушатели сенсеров: TYPE_AMBIENT_TEMPERATURE, TYPE_RELATIVE_HUMIDITY
         */
        if (mTemperatureSensor != null & mHumiditySensor != null){
            mSensorManager.registerListener(mTempListener, mTemperatureSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(mHumidityListener,mHumiditySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        /*
         * Проверяем на наличие сенсоров.
         * Отключаем слушатели сенсоров: TYPE_AMBIENT_TEMPERATURE, TYPE_RELATIVE_HUMIDITY
         */
        if (mTemperatureSensor != null && mHumiditySensor != null) {
            mSensorManager.unregisterListener(mTempListener);
            mSensorManager.unregisterListener(mHumidityListener);
        }
    }
}
