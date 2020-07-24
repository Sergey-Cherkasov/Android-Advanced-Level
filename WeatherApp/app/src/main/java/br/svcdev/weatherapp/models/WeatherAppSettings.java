package br.svcdev.weatherapp.models;

public class WeatherAppSettings {

    private boolean mTemperatureUnits;
    private boolean mWindSpeedUnits;

    private static WeatherAppSettings mWeatherAppSettings;

    public static WeatherAppSettings getWeatherAppSettings(){
        if (mWeatherAppSettings == null) {
            mWeatherAppSettings = new WeatherAppSettings();
        }
        return mWeatherAppSettings;
    }

    public boolean isTemperatureUnits() {
        return mTemperatureUnits;
    }

    public void setTemperatureUnits(boolean temperatureUnits) {
        this.mTemperatureUnits = temperatureUnits;
    }

    public boolean isWindSpeedUnits() {
        return mWindSpeedUnits;
    }

    public void setWindSpeedUnits(boolean windSpeedUnits) {
        this.mWindSpeedUnits = windSpeedUnits;
    }
}
