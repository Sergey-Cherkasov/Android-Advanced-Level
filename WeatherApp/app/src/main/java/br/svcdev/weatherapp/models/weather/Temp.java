package br.svcdev.weatherapp.models.weather;

import com.google.gson.annotations.SerializedName;

public class Temp {
	@SerializedName("day")
	private long dayTemp;

	public long getDayTemp() {
		return dayTemp;
	}

	public void setDayTemp(long dayTemp) {
		this.dayTemp = dayTemp;
	}
}
