package br.svcdev.weatherapp.network;

import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public abstract class NetworkUtils {

    public static void loadImage(String iconId, ImageView imageView) {
        Uri uri = Uri.parse(String.format("http://openweathermap.org/img/wn/%s@2x.png", iconId));
        Picasso.get().load(uri).into(imageView);
    }

    public static String getBaseUrl() {
        return String.format("%s%s", HostRequestConstants.DATA_TRANSFER_PROTOCOL,
                HostRequestConstants.OPENWEATHER_HOST);
    }

    public static Retrofit onRetrofitCreate(GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder().baseUrl(NetworkUtils.getBaseUrl())
                .addConverterFactory(gsonConverterFactory).build();
    }

    public static Retrofit onRetrofitCreate(ScalarsConverterFactory scalarsConverterFactory) {
        return new Retrofit.Builder().baseUrl(NetworkUtils.getBaseUrl())
                .addConverterFactory(scalarsConverterFactory).build();
    }

}
