package br.svcdev.weatherapp.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import br.svcdev.weatherapp.BuildConfig;
import br.svcdev.weatherapp.Constants;
import br.svcdev.weatherapp.R;
import br.svcdev.weatherapp.databinding.ActivityMapsBinding;
import br.svcdev.weatherapp.models.weather.CurrentWeather;
import br.svcdev.weatherapp.network.NetworkUtils;
import br.svcdev.weatherapp.network.OpenWeatherRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

	private static final int PERMISSION_REQUEST_CODE = 100;
	private GoogleMap mMap;
	private Marker currentMarker;
	private ActivityMapsBinding mBinding;
	private float mTemp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = ActivityMapsBinding.inflate(getLayoutInflater());
		setContentView(mBinding.getRoot());
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		requestPermissions();
	}

	public void requestPermissions() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				== PackageManager.PERMISSION_GRANTED ||
				ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
						== PackageManager.PERMISSION_GRANTED) {
			requestLocation();
		} else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
			ActivityCompat.requestPermissions(this, new String[]{
					Manifest.permission.ACCESS_FINE_LOCATION,
					Manifest.permission.ACCESS_COARSE_LOCATION
			}, PERMISSION_REQUEST_CODE);
		}

	}

	private void requestLocation() {
		if (ActivityCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				ActivityCompat.checkSelfPermission(this,
						Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		String provider = locationManager.getBestProvider(criteria, true);
		if (provider != null) {
			locationManager.requestLocationUpdates(provider, 10000, 50,
					new LocationListener() {
						@Override
						public void onLocationChanged(Location location) {
							double lattitude = location.getLatitude();
							double longitude = location.getLongitude();
							Log.d(Constants.TAG_APP, String.format("onLocationChanged: lon = %s",
									lattitude));
							LatLng currentPosition = new LatLng(lattitude, longitude);
							currentMarker.setPosition(currentPosition);
							mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,
									(float) 12));
							requestWeatherData(currentPosition);
						}

						@Override
						public void onStatusChanged(String provider, int status, Bundle extras) {

						}

						@Override
						public void onProviderEnabled(String provider) {

						}

						@Override
						public void onProviderDisabled(String provider) {

						}
					});
		}
	}

	private void requestWeatherData(LatLng currentPosition) {
		OpenWeatherRequest request = NetworkUtils
				.onRetrofitCreate(GsonConverterFactory.create())
				.create(OpenWeatherRequest.class);
		request.loadCurrentWeather(currentPosition.latitude, currentPosition.longitude,
				"metric", getResources().getString(R.string.language), BuildConfig.API_KEY)
				.enqueue(new Callback<CurrentWeather>() {
					@Override
					public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
						if (response.body() != null) {
							mTemp = response.body().getMain().getTemp();
							Toast.makeText(MapsActivity.this,
									String.format(Locale.ENGLISH, "Current temperature: %.0f",
											mTemp), Toast.LENGTH_SHORT).show();
							mBinding.mapsCityName.setText(response.body().getCityName());
							mBinding.mapsCurrentTemperature.setText(
									String.format(Locale.ENGLISH, "%.0f", mTemp));
							mBinding.mapsCurrentUnits.setImageResource(R.drawable.ic_celsius);
							NetworkUtils.loadImage(response.body().getWeathers()[0].getIcon(),
									mBinding.mapsCurrentCloudiness);
						}
					}

					@Override
					public void onFailure(Call<CurrentWeather> call, Throwable t) {

					}
				});
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		if (requestCode == PERMISSION_REQUEST_CODE) {
			if (grantResults.length == 2 && (grantResults[0] == PackageManager.PERMISSION_GRANTED ||
					grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
			}
		}
	}

	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;

		// Add a marker in Sydney and move the camera
		LatLng sydney = new LatLng(-34, 151);
		currentMarker = mMap.addMarker(new MarkerOptions().position(sydney).title("Current position"));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

		mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				requestWeatherData(latLng);
			}
		});

		mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				requestWeatherData(marker.getPosition());
				return true;
			}
		});
	}


}