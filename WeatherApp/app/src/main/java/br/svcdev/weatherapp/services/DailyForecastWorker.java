package br.svcdev.weatherapp.services;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;

import br.svcdev.weatherapp.BuildConfig;
import br.svcdev.weatherapp.ExternalUtils;
import br.svcdev.weatherapp.network.NetworkUtils;
import br.svcdev.weatherapp.network.OpenWeatherRequest;
import retrofit2.Response;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DailyForecastWorker extends Worker {

    public DailyForecastWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        OpenWeatherRequest openWeatherRequest =
                NetworkUtils.onRetrofitCreate(ScalarsConverterFactory.create())
                        .create(OpenWeatherRequest.class);

        try {
            Response<String> response = requestRetrofit(openWeatherRequest);
            Data data = new Data.Builder().putString("response", response.body()).build();
            return Result.success(data);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failure();
        }
    }

    private Response<String> requestRetrofit(OpenWeatherRequest openWeatherRequest)
            throws IOException {
        int id = getInputData().getInt("id", 0);
        int cnt = getInputData().getInt("cnt", 0);
        String units = getInputData().getString("units");
        String lang = getInputData().getString("lang");
        return openWeatherRequest
                .loadFDTHForecast(id, units, lang, cnt, BuildConfig.API_KEY)
                .execute();
    }

}
