package br.svcdev.weatherapp.network;

public abstract class HostRequestConstants {

    private static final String WEATHER_KEY = "2774380a7fbc3859b8bbe154984844b5";

    public static final String DATA_TRANSFER_PROTOCOL = "http://";
    //    public static final String DATA_TRANSFER_PROTOCOL = "https://";
    public static final String OPEN_WEATHER_HOST = "api.openweathermap.org";
    public static final String CONTROLLER_CURRENT_CONDITIONS = "/data/2.5/weather?";
    public static final String CONTROLLER_DAILY_FORECAST = "/data/2.5/forecast?";
    public static final String REQUEST_METHOD = "GET";
    //    public static final String REQUEST_METHOD = "POST";

    public static String getWeatherKey() {
        return WEATHER_KEY;
    }

}
