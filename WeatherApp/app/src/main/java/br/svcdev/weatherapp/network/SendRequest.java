package br.svcdev.weatherapp.network;

import android.net.Uri;
import android.os.AsyncTask;

import androidx.fragment.app.FragmentManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import br.svcdev.weatherapp.BuildConfig;
import br.svcdev.weatherapp.R;

public class SendRequest extends AsyncTask<Void, Map<String, String>, Map<String, String>> {

    private FragmentManager mContext;
    private String mRequestController;
    private Map<String, Object> mRequestParameters;
    private String mRequestMethod;
    private String mRequestId;

    private URL mUrlRequest;

    public SendRequest(FragmentManager context, String requestController,
                       Map<String, Object> requestParameters, String requestMethod,
                       String requestId) {
        this.mContext = context;
        this.mRequestController = requestController;
        this.mRequestParameters = requestParameters;
        this.mRequestMethod = requestMethod;
        this.mRequestId = requestId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mUrlRequest = buildRequestUrl();
    }

    @Override
    protected Map<String, String> doInBackground(Void... voids) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) mUrlRequest.openConnection();
            connection.setRequestMethod(mRequestMethod);
            connection.setReadTimeout(10000);
            InputStream inputStream;
            if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }
            BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
            String responseString = getStringResponse(input);
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put(mRequestId, responseString);
            return responseMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Map<String, String> responseMap) {
        super.onPostExecute(responseMap);
        switch (responseMap.keySet().iterator().next()) {
            case HostRequestConstants.REQUEST_CONTROLLER_CURRENT_CONDITIONS:
                ((ServerResponse) mContext.findFragmentById(R.id.fragment_weather_current_conditions))
                        .onServerResponse(responseMap);
                break;
            case HostRequestConstants.REQUEST_CONTROLLER_FIVE_DAYS_PER_THREE_HOURS_FORECAST:
                ((ServerResponse) mContext
                        .findFragmentById(R.id.fragment_weather_daily_forecast))
                        .onServerResponse(responseMap);
                break;
            case HostRequestConstants.REQUEST_CONTROLLER_DAILY_FORECAST:
                break;
            default:
        }
    }

    private URL buildRequestUrl() {
        Uri.Builder builder = new Uri.Builder();
        for (Map.Entry<String, Object> entry : mRequestParameters.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue().toString());
        }
        builder.appendQueryParameter("apikey", BuildConfig.API_KEY);
        String dataTransferProtocol = HostRequestConstants.DATA_TRANSFER_PROTOCOL;
        String host = HostRequestConstants.OPEN_WEATHER_HOST;
        String requestParameters = builder.build().getEncodedQuery();
        URL result = null;
        try {
            result = new URL(dataTransferProtocol + host + mRequestController +
                    requestParameters);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getStringResponse(BufferedReader input) {
        String tmpString;
        StringBuilder builder = new StringBuilder();
        try {
            while ((tmpString = input.readLine()) != null) {
                builder.append(tmpString);
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
