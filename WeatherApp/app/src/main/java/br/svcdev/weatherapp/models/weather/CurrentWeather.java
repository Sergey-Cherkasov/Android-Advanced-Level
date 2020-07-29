package br.svcdev.weatherapp.models.weather;

import com.google.gson.annotations.SerializedName;

import br.svcdev.weatherapp.models.location.CoordinatesLocation;

public class CurrentWeather {

    @SerializedName("coord")
    private CoordinatesLocation coord;
    @SerializedName("weather")
    private Weather[] weathers;
    @SerializedName("main")
    private Main main;
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("name")
    private String cityName;


    public CoordinatesLocation getCoord() {
        return coord;
    }

    public void setCoord(CoordinatesLocation coord) {
        this.coord = coord;
    }

    public Weather[] getWeathers() {
        return weathers;
    }

    public void setWeathers(Weather[] weathers) {
        this.weathers = weathers;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
