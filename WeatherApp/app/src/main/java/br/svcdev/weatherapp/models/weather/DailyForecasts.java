package br.svcdev.weatherapp.models.weather;

import com.google.gson.annotations.SerializedName;

public class DailyForecasts {

	@SerializedName("list")
	private DayForecastWeather[] mDailyForecastsWeathers;

	public DayForecastWeather[] getDailyForecastsWeathers() {
		return mDailyForecastsWeathers;
	}

	public void setDailyForecastsWeathers(DayForecastWeather[] mDailyForecastsWeathers) {
		this.mDailyForecastsWeathers = mDailyForecastsWeathers;
	}
}
