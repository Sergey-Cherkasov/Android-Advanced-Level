package br.svcdev.weatherapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import br.svcdev.weatherapp.adapters.CitiesAutocompleteAdapter;
import br.svcdev.weatherapp.databinding.ActivitySearchLocationBinding;
import br.svcdev.weatherapp.data.models.City;

public class SearchLocationActivity extends AppCompatActivity {

    private ActivitySearchLocationBinding mBinding;

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

        ActionMode actionMode = startSupportActionMode(mActionModeCallback);

//        String jsonStringCities = loadDataCitiesFromResource();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

//        List<Cities> listCities = Arrays.asList(gson.fromJson(jsonStringCities, Cities[].class));

        mBinding.tvLocation.setThreshold(3);
        mBinding.tvLocation.setAutocompleteDelay(800);
//        mBinding.tvLocation.setAdapter(new LocationAutocompleteAdapter(this, listCities));

        mBinding.tvLocation.setAdapter(new CitiesAutocompleteAdapter(this));

        mBinding.tvLocation.setIndicatorLoading(mBinding.pbLocation);
        mBinding.tvLocation.setOnItemClickListener((adapterView, view, i, l) -> {
            City city = (City) adapterView.getItemAtPosition(i);
            mBinding.tvLocation.setText(city.getCityName());
        });

    }

    private String loadDataCitiesFromResource() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = getResources().openRawResource(R.raw.city_list)) {
            byte[] byteBuffer = new byte[512];
            int len;
            while ((len = is.read(byteBuffer)) != -1){
                baos.write(byteBuffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toString();
    }

}