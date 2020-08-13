package br.svcdev.weatherapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

import br.svcdev.weatherapp.R;
import br.svcdev.weatherapp.models.weather.DailyForecasts;
import br.svcdev.weatherapp.network.NetworkUtils;

public class WeatherDailyForecastRecyclerViewAdapter
		extends RecyclerView.Adapter<WeatherDailyForecastRecyclerViewAdapter
		.WeatherDailyForecastHolder> {

	public static class WeatherDailyForecastHolder extends RecyclerView.ViewHolder {

		private TextView mDate;
		private TextView mTime;
		private ImageView mCloudinnes;
		private TextView mTemperature;
		private ImageView mTemperatureUnits;

		public WeatherDailyForecastHolder(@NonNull View itemView) {
			super(itemView);
			mDate = itemView.findViewById(R.id.tv_weather_daily_forecast_cardview_date);
			mTime = itemView.findViewById(R.id.tv_weather_daily_forecast_cardview_time);
			mTemperature = itemView.findViewById(R.id.tv_weather_daily_forecast_cardview_temperature);
			mCloudinnes = itemView.findViewById(R.id.iv_weather_daily_forecast_cardview_cloudiness);
			mTemperatureUnits = itemView.findViewById(R.id.iv_weather_daily_forecast_cardview_temperature_units);
		}

		public TextView getDate() {
			return mDate;
		}

		public TextView getTime() {
			return mTime;
		}

		public TextView getTemperature() {
			return mTemperature;
		}

		public ImageView getCloudinnes() {
			return mCloudinnes;
		}

		public ImageView getTemperatureUnits() {
			return mTemperatureUnits;
		}

		public void setData(String date, String time, String temperature, int imageResourceId) {
			getDate().setText(date);
			getTime().setText(time);
			getTemperature().setText(temperature);
			getTemperatureUnits().setImageResource(imageResourceId);
		}

	}

	private DailyForecasts mDataSource;
	private SharedPreferences sp;

	public WeatherDailyForecastRecyclerViewAdapter(Context context, DailyForecasts dataSource) {
		this.mDataSource = dataSource;
		this.sp = PreferenceManager.getDefaultSharedPreferences(context);
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
		if (sp.getBoolean("temperature_units", false)) {
			holder.setData(mDataSource.getDailyForecastsWeathers()[position].getDate(),
					mDataSource.getDailyForecastsWeathers()[position].getTime(),
					String.format(Locale.ENGLISH, "%.1f", mDataSource
							.getDailyForecastsWeathers()[position].getMain().getTempF()),
					R.drawable.ic_fahrenheit);
		} else {
			holder.setData(mDataSource.getDailyForecastsWeathers()[position].getDate(),
					mDataSource.getDailyForecastsWeathers()[position].getTime(),
					String.format(Locale.ENGLISH, "%.1f", mDataSource
							.getDailyForecastsWeathers()[position].getMain().getTemp()),
					R.drawable.ic_celsius);
		}
		NetworkUtils.loadImage(mDataSource.getDailyForecastsWeathers()[position]
				.getWeather()[0].getIcon(), holder.mCloudinnes);
	}

	public void setDataSource(DailyForecasts dataSource) {
		this.mDataSource = dataSource;
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		if (mDataSource == null) return 0;
		return mDataSource.getDailyForecastsWeathers().length;
	}
}
