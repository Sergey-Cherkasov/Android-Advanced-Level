package br.svcdev.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.svcdev.weatherapp.adapters.CitiesAutocompleteAdapter;
import br.svcdev.weatherapp.data.CitiesDatabase;
import br.svcdev.weatherapp.data.dao.CityDao;
import br.svcdev.weatherapp.data.models.City;
import br.svcdev.weatherapp.databinding.ActivitySearchLocationBinding;

public class SearchLocationActivity extends AppCompatActivity {

    private ActivitySearchLocationBinding mBinding;
    private CitiesDatabase db;
    private CityDao dao;
    private City city;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Select location");
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.search_location_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.ok) {
                SharedPreferences sp = PreferenceManager
                        .getDefaultSharedPreferences(mBinding.getRoot().getContext());
                sp.edit()
                        .putInt("cityId", city.getCityId())
                        .putString("cityName", city.getCityName())
                        .apply();
                finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mode = null;
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySearchLocationBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        startSupportActionMode(mActionModeCallback);

        db = CitiesDatabase.getDatabase(this);
        dao = db.getCitiesDao();

        mBinding.tvLocation.setThreshold(3);
        mBinding.tvLocation.setAutocompleteDelay(800);

        mBinding.tvLocation.setAdapter(new CitiesAutocompleteAdapter(this, dao));

        mBinding.tvLocation.setIndicatorLoading(mBinding.pbLocation);
        mBinding.tvLocation.setOnItemClickListener((adapterView, view, i, l) -> {
            city = (City) adapterView.getItemAtPosition(i);
            mBinding.tvLocation.setText(city.getCityName());
        });

    }

}