package br.svcdev.weatherapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import br.svcdev.weatherapp.Constants;
import br.svcdev.weatherapp.R;
import br.svcdev.weatherapp.adapters.WeatherDailyForecastRecyclerViewAdapter;
import br.svcdev.weatherapp.databinding.FragmentWeatherDailyForecastBinding;
import br.svcdev.weatherapp.models.weather.DailyForecasts;
import br.svcdev.weatherapp.models.weather.DayForecastWeather;
import br.svcdev.weatherapp.network.HostRequestConstants;
import br.svcdev.weatherapp.network.SendRequest;
import br.svcdev.weatherapp.network.ServerResponse;

public class WeatherDailyForecast extends Fragment implements ServerResponse {

    private FragmentWeatherDailyForecastBinding mBinding;
    private DailyForecasts mDailyForecasts;
    private WeatherDailyForecastRecyclerViewAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onSendRequest();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentWeatherDailyForecastBinding.inflate(inflater, container,
                false);
        initRecyclerView();
        return mBinding.getRoot();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mBinding.getRoot().getContext(),
                RecyclerView.HORIZONTAL, false);
        mAdapter = new WeatherDailyForecastRecyclerViewAdapter(requireContext(), mDailyForecasts);
        mBinding.recyclerViewFragmentWeatherDailyForecast.setHasFixedSize(true);
        mBinding.recyclerViewFragmentWeatherDailyForecast.setLayoutManager(layoutManager);
        mBinding.recyclerViewFragmentWeatherDailyForecast.setAdapter(mAdapter);
    }

    private void onSendRequest() {
        Map<String, Object> requestParameters = new HashMap<>();
        int locationId = 1503901;
        String units = "metric";
        String languageCode = getResources().getString(R.string.language);
        requestParameters.put("id", locationId);
        requestParameters.put("cnt", "8");
        requestParameters.put("units", units);
        requestParameters.put("lang", languageCode);

        SendRequest sendRequest = new SendRequest(getParentFragmentManager(),
                HostRequestConstants.CONTROLLER_FIVE_DAYS_PER_THREE_HOURS_FORECAST,
                requestParameters,
                HostRequestConstants.REQUEST_METHOD,
                HostRequestConstants.CONTROLLER_FIVE_DAYS_PER_THREE_HOURS_FORECAST);
        sendRequest.execute();
    }

    @Override
    public void onServerResponse(Map<String, String> response) {
        Iterator<String> key = response.keySet().iterator();
        String requestId = key.next();
        String responseString = response.get(requestId);
        Log.d(Constants.TAG_APP, "onServerResponse: " + responseString);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        if (requestId.equals(HostRequestConstants.CONTROLLER_FIVE_DAYS_PER_THREE_HOURS_FORECAST)) {
            mDailyForecasts = gson.fromJson(responseString,
                    DailyForecasts.class);
            for (DayForecastWeather item : mDailyForecasts.getDailyForecastsWeathers()) {
                item.getMain().setTempF(celciusToFahrenheit(item.getMain().getTemp()));
            }
            mAdapter.setDataSource(mDailyForecasts);
        }
    }

    private float celciusToFahrenheit(float temp) {
        return temp * 9 / 5 + 32;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.setDataSource(mDailyForecasts);
    }
}
