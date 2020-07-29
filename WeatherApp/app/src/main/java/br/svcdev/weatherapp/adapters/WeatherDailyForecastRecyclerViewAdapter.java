package br.svcdev.weatherapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import br.svcdev.weatherapp.R;
import br.svcdev.weatherapp.models.weather.DailyForecasts;
import br.svcdev.weatherapp.models.weather.DayForecastWeather;

public class WeatherDailyForecastRecyclerViewAdapter
        extends RecyclerView.Adapter<WeatherDailyForecastRecyclerViewAdapter
        .WeatherDailyForecastHolder> {

    public static class WeatherDailyForecastHolder extends RecyclerView.ViewHolder{

        private TextView mDayOfWeek;
        private ImageView mCloudinnes;
        private TextView mTemperature;
        private ImageView mTemperatureUnits;

        public WeatherDailyForecastHolder(@NonNull View itemView) {
            super(itemView);
            mDayOfWeek = itemView.findViewById(R.id.tv_weather_daily_forecast_cardview_day_of_week);
            mTemperature = itemView.findViewById(R.id.tv_weather_daily_forecast_cardview_temperature);
            mCloudinnes = itemView.findViewById(R.id.iv_weather_daily_forecast_cardview_cloudiness);
            mTemperatureUnits = itemView.findViewById(R.id.iv_weather_daily_forecast_cardview_temperature_units);
        }

        public TextView getDayOfWeek() {return mDayOfWeek;}
        public TextView getTemperature() {return mTemperature;}
        public ImageView getCloudinnes() {return mCloudinnes;}
        public ImageView getTemperatureUnits() {return mTemperatureUnits;}

        public void setData(String dayOfWeek, String temperature){
            getDayOfWeek().setText(dayOfWeek);
            getTemperature().setText(temperature);
        }

    }

    private DailyForecasts mDataSource;

    public WeatherDailyForecastRecyclerViewAdapter(DailyForecasts dataSource) {
        this.mDataSource = dataSource;
    }

    @NonNull
    @Override
    public WeatherDailyForecastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_weather_daily_forecast_cardview, parent,
                        false);
        return new WeatherDailyForecastHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherDailyForecastHolder holder, int position) {
        holder.setData(String.valueOf(mDataSource.getDailyForecastsWeathers()[position].getDateTime()),
                String.valueOf(mDataSource.getDailyForecastsWeathers()[position].getMain().getTemp()));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mDataSource == null) return 0;
        return mDataSource.getDailyForecastsWeathers().length;
    }
}
