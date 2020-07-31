package br.svcdev.weatherapp.models.weather;

import com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("temp")
    private float temp;
    @SerializedName("feels_like")
    private float feelsLike;
    @SerializedName("temp_min")
    private float tempMin;
    @SerializedName("temp_max")
    private float tempMax;
    @SerializedName("pressure")
    private int pressure;
    @SerializedName("humidity")
    private int humidity;

    private float tempF;

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(float feelsLike) {
        this.feelsLike = feelsLike;
    }

    public float getTempMin() {
        return tempMin;
    }

    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getTempF() {
        return tempF;
    }

    public void setTempF(float tempF) {
        this.tempF = tempF;
    }
}
