package br.svcdev.weatherapp.network;

import br.svcdev.weatherapp.models.weather.CurrentWeather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherRequest {

    @GET(HostRequestConstants.REQUEST_CONTROLLER_CUR_CONDITIONS)
    Call<CurrentWeather> loadCurrentWeather(@Query("id") int id,
                                            @Query("units") String units,
                                            @Query("lang") String lang,
                                            @Query("appid") String appId);

    @GET(HostRequestConstants.REQUEST_CONTROLLER_FDTH_FORECAST)
    Call<String> loadFDTHForecast(@Query("id") int id,
                                  @Query("units") String units,
                                  @Query("lang") String lang,
                                  @Query("cnt") int cnt,
                                  @Query("appid") String appId);

}
