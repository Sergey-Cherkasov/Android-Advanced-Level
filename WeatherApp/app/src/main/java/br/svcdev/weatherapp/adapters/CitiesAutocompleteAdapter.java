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

import br.svcdev.weatherapp.data.dao.CityDao;
import br.svcdev.weatherapp.data.models.City;

public class CitiesAutocompleteAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private List<City> mCityList;

    private CityDao dao;

    public CitiesAutocompleteAdapter(Context context, CityDao dao) {
        this.mContext = context;
        this.dao = dao;
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
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (charSequence != null) {
                    List<City> tempList = getFilterResults(charSequence);
                    filterResults.values = tempList;
                    filterResults.count = tempList.size();
                }
                return filterResults;
            }

            private List<City> getFilterResults(CharSequence charSequence) {
                if (charSequence.equals("")) {
                    return dao.getAllCities();
                } else {
                    String tmpString = charSequence.toString().substring(0,1).toUpperCase() +
                            charSequence.toString().substring(1);
                    return dao.getListCities(String.format("%s%%",tmpString));
                }
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
