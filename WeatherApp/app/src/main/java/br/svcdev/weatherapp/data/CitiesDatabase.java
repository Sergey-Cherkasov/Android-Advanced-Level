package br.svcdev.weatherapp.data;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.svcdev.weatherapp.Constants;
import br.svcdev.weatherapp.data.dao.CityDao;
import br.svcdev.weatherapp.data.models.City;

@Database(entities = {City.class}, version = 1, exportSchema = false)
public abstract class CitiesDatabase extends RoomDatabase {

    public abstract CityDao getCitiesDao();

    private static volatile CitiesDatabase sInstance;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CitiesDatabase getDatabase(final Context context){
        if (sInstance == null) {
            synchronized (CitiesDatabase.class) {
//                sInstance = Room.databaseBuilder(context.getApplicationContext(),
//                        CitiesDatabase.class, "cities_list.db")
//                        .createFromFile(new File(context.getExternalFilesDir("/"), "cities_list.sqlite"))
//                        .build();

                Log.d(Constants.TAG_APP, context.getExternalFilesDir("/").toString());
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        CitiesDatabase.class, "cities_list.db").build();
            }
        }
        return sInstance;
    }

    private static RoomDatabase.Callback sCitiesDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

}
