package br.svcdev.weatherapp.data.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import br.svcdev.weatherapp.data.models.City;

// Описание объекта, обрабатывающего данные
// @Dao - доступ к данным
// В этом классе описывается, как будет происходить обработка данных
@Dao
public interface CityDao {

    // Выбираем все записи
    @Query("SELECT * FROM cities")
    List<City> getAllCities();

    // Выбираем записи удовлетворяющие условию
    @Query("SELECT * FROM cities WHERE name LIKE :name LIMIT 5")
    List<City> getListCities(String name);

    // Количество записей в БД
    @Query("SELECT COUNT() FROM cities")
    long getCountCities();

    @Query("DELETE FROM cities")
    void deleteAll();
}
