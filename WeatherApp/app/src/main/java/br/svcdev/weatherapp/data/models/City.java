package br.svcdev.weatherapp.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// @Entity - это признак табличного объекта, то есть объект будет сохраняться
// в базе данных в виде строки
// indices указывает на индексы в таблице
@Entity(tableName = "Cities")
public class City {

    // @PrimaryKey - указывает на ключевую запись,
    // autoGenerate = true - автоматическая генерация ключа
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "city_id")
    private long cityId;

    @ColumnInfo(name = "name")
    private String cityName;

    @ColumnInfo(name = "state")
    private String cityState;

    @ColumnInfo(name = "country")
    private String cityCountry;

    public City(long cityId, String cityName, String cityState, String cityCountry){
        this.cityId = cityId;
        this.cityName = cityName;
        this.cityState = cityState;
        this.cityCountry = cityCountry;
    }

    public long getCityId() {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
