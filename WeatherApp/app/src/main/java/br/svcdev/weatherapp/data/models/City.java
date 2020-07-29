package br.svcdev.weatherapp.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cities")
public class City {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "city_id")
    private int cityId;

    @ColumnInfo(name = "name")
    private String cityName;

    @ColumnInfo(name = "state")
    private String cityState;

    @ColumnInfo(name = "country")
    private String cityCountry;

    public City(int cityId, String cityName, String cityState, String cityCountry){
        this.cityId = cityId;
        this.cityName = cityName;
        this.cityState = cityState;
        this.cityCountry = cityCountry;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityState() {
        return cityState;
    }

    public String getCityCountry() {
        return cityCountry;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
