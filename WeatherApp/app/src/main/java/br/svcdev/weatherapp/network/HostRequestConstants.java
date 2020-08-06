package br.svcdev.weatherapp.network;

public abstract class HostRequestConstants {

    public static final String DATA_TRANSFER_PROTOCOL = "http://";
    //    public static final String DATA_TRANSFER_PROTOCOL = "https://";

    public static final String OPEN_WEATHER_HOST = "api.openweathermap.org";
    public static final String OPENWEATHER_HOST = "api.openweathermap.org/";

    public static final String REQUEST_CONTROLLER_CURRENT_CONDITIONS = "/data/2.5/weather?";
    public static final String REQUEST_CONTROLLER_CUR_CONDITIONS = "data/2.5/weather";
    public static final String REQUEST_CONTROLLER_DAILY_FORECAST = "/data/2.5/forecast/daily?";
    public static final String REQUEST_CONTROLLER_FIVE_DAYS_PER_THREE_HOURS_FORECAST = "/data/2.5/forecast?";
    public static final String REQUEST_CONTROLLER_FDTH_FORECAST = "data/2.5/forecast";

    public static final String REQUEST_METHOD = "GET";
    //    public static final String REQUEST_METHOD = "POST";

    public static final String KEY_REQUEST_CONTROLLER = "KEY_REQUEST_CONTROLLER";
    public static final String KEY_REQUEST_METHOD = "KEY_REQUEST_METHOD";
    public static final String KEY_REQUEST_ID = "KEY_REQUEST_ID";
}
