package br.svcdev.weatherapp.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.io.File;
import java.lang.reflect.Method;

import br.svcdev.weatherapp.Constants;
import br.svcdev.weatherapp.ExternalUtils;
import br.svcdev.weatherapp.data.dao.CityDao;
import br.svcdev.weatherapp.data.models.City;

@Database(entities = {City.class}, version = 1, exportSchema = false)
public abstract class CitiesDatabase extends RoomDatabase {

    public abstract CityDao getCitiesDao();
    private static volatile CitiesDatabase sDatabaseInstance;

    public static CitiesDatabase getDatabase(final Context context){
        if (sDatabaseInstance == null) {
            synchronized (CitiesDatabase.class) {
                if (!new File(context.getDatabasePath("cities_list.db").toString()).exists()) {
                    ExternalUtils.printDebugLog("getDatabase: Database file not exists. " +
                            "Pre-populated in and connecting to the database.");
                    sDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            CitiesDatabase.class, "cities_list.db")
                            .createFromAsset("databases/cities_list.sqlite").build();
                } else {
                    ExternalUtils.printDebugLog("getDatabase: Database file exists. " +
                            "Connecting to the database.");
                    sDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            CitiesDatabase.class, "cities_list.db").build();
                }
            }
        }
        return sDatabaseInstance;
    }
}
