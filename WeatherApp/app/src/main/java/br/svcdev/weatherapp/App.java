package br.svcdev.weatherapp;

import android.app.Application;

import androidx.room.Room;

import br.svcdev.weatherapp.data.CitiesDatabase;
import br.svcdev.weatherapp.data.dao.CityDao;

public class App extends Application {

    private static App mInstance;

    private CitiesDatabase mDb;

    public static App getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mDb = Room.databaseBuilder(getApplicationContext(),
                CitiesDatabase.class,
                "cities_list").build();
    }

    public CityDao getLocationDao(){
        return mDb.getCitiesDao();
    }
}
