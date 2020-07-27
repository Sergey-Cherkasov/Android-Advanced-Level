package br.svcdev.weatherapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

import br.svcdev.weatherapp.data.CitiesDatabase;
import br.svcdev.weatherapp.data.dao.CityDao;
import br.svcdev.weatherapp.data.models.City;

public class CitiesAutocompleteAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private List<City> mCityList;

    private CitiesDatabase mDb;
    private CityDao mDao;

    public CitiesAutocompleteAdapter(Context context){
        this.mContext = context;
//        mDb = CitiesDatabase.getDatabase(mContext);
//        mDao = mDb.getCitiesDao();
    }

    @Override
    public int getCount() {
        if (mCityList != null) {
            return mCityList.size();
        }
        return 0;
    }

    @Override
    public City getItem(int index) {
        return mCityList.get(index);
    }

    @Override
    public long getItemId(int itemId) {
        return itemId;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext)
                    .inflate(android.R.layout.simple_dropdown_item_1line, viewGroup,
                            false);
        }
        City city = getItem(position);
        String cityString = String.format("%s, %s", city.getCityName(), city.getCityCountry());
        ((TextView) view.findViewById(android.R.id.text1)).setText(cityString);
        return null;
    }

    void setCitiesList(List<City> cityList){
        mCityList = cityList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (charSequence != null) {
                    List<City> tempCityList = getFilterResults(charSequence);
                    filterResults.values = tempCityList;
                    filterResults.count = tempCityList.size();
                }
                return filterResults;
            }

            private List<City> getFilterResults(CharSequence charSequence) {
//                if (charSequence != null){
//                    if (charSequence.equals("")){
//                        mDao.getAllCities();
//                    } else {
//                        mDao.getListCities(charSequence.toString());
//                    }
//                }
                return mCityList;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults != null && filterResults.count > 0) {
                    mCityList = (List<City>) filterResults.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
