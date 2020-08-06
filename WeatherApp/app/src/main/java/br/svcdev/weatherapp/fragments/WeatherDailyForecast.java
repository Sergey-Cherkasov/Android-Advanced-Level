package br.svcdev.weatherapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.svcdev.weatherapp.R;
import br.svcdev.weatherapp.adapters.WeatherDailyForecastRecyclerViewAdapter;
import br.svcdev.weatherapp.databinding.FragmentWeatherDailyForecastBinding;
import br.svcdev.weatherapp.models.weather.DailyForecasts;
import br.svcdev.weatherapp.models.weather.DayForecastWeather;
import br.svcdev.weatherapp.services.DailyForecastWorker;

public class WeatherDailyForecast extends Fragment {

    private FragmentWeatherDailyForecastBinding mBinding;
    private DailyForecasts mDailyForecasts;

    private WeatherDailyForecastRecyclerViewAdapter mAdapter;
    private WorkManager workManager;
    private WorkRequest workRequest;

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
        onServerResponse();
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

    /**
     * Метод формирует и отправляет запрос в WorkManager
     **/
    private void onSendRequest() {
        workRequest = new OneTimeWorkRequest.Builder(DailyForecastWorker.class)
                .setInputData(createInputData()).build();
        workManager = WorkManager.getInstance(requireContext());
        workManager.enqueue(workRequest);
    }

    /**
     * Метод создает входные данные для запроса
     **/
    private Data createInputData() {
        int locationId = 1503901;
        int countRecords = 8;
        String units = "metric";
        String languageCode = getResources().getString(R.string.language);
        return new Data.Builder()
                .putInt("id", locationId)
                .putInt("cnt", countRecords)
                .putString("units", units)
                .putString("lang", languageCode)
                .build();
    }


    public void onServerResponse() {
        workManager.getWorkInfoByIdLiveData(workRequest.getId()).observe((LifecycleOwner) requireContext(),
                info -> {
                    if (info != null && info.getState().isFinished()) {
                        String response = info.getOutputData().getString("response");
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.create();
                        mDailyForecasts = gson.fromJson(response,
                                DailyForecasts.class);
                        for (DayForecastWeather item : mDailyForecasts.getDailyForecastsWeathers()) {
                            item.getMain().setTempF(celciusToFahrenheit(item.getMain().getTemp()));
                        }
                        mAdapter.setDataSource(mDailyForecasts);
                    }
                });
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
