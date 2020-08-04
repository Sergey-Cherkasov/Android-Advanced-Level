package br.svcdev.weatherapp.network;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public abstract class NetworkUtils {

    public static void loadImage(String iconId, ImageView imageView) {
        Picasso.get().load(String.format("http://openweathermap.org/img/wn/%s.png", iconId))
                .into(imageView);
    }

}
