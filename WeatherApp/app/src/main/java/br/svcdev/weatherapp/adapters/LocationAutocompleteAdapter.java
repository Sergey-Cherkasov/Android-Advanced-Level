package br.svcdev.weatherapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.svcdev.weatherapp.R;
import br.svcdev.weatherapp.models.location.Cities;

public class LocationAutocompleteAdapter extends BaseAdapter implements Filterable {

    /**
     * Максимальное количество элементов в выпадающем списке
     */
    private final static int MAX_RESULT = 5;

    private final Context mContext;
    private final List<Cities> mListCities;
    private List<Cities> mResult;

    public LocationAutocompleteAdapter(Context context, List<Cities> listCities) {
        this.mContext = context;
        this.mListCities = listCities;
        this.mResult = new ArrayList<>();
    }

    @Override
    public int getCount() {
        if (mResult.size() < MAX_RESULT){
            return mResult.size();
        }
        else {
            return MAX_RESULT;
        }
    }

    @Override
    public Object getItem(int i) {
        return mResult.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(mContext)
                    .inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }
        Cities item = (Cities) getItem(position);
        String cityAndCountry = String.format("%s, %s, %s", item.getName(),
                item.getState(), item.getCodeCountry());
        ((TextView) view.findViewById(android.R.id.text1)).setText(cityAndCountry);

        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if (charSequence != null) {
                    List<Cities> listCities = findCities(charSequence.toString());
                    filterResults.values = listCities;
                    filterResults.count = listCities.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults != null && filterResults.count > 0) {
                    mResult = (List<Cities>) filterResults.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

            private List<Cities> findCities(String partOfNameCity) {
                List<Cities> result = new ArrayList<>();
                for (Cities city : mListCities) {
                    if (city.getName().toLowerCase().contains(partOfNameCity.toLowerCase())){
                        result.add(city);
                    }
                }
                return result;
            }

        };
    }
}
