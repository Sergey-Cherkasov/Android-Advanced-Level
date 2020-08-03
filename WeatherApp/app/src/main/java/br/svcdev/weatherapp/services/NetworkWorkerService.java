package br.svcdev.weatherapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import br.svcdev.weatherapp.BuildConfig;
import br.svcdev.weatherapp.ExternalUtils;
import br.svcdev.weatherapp.R;
import br.svcdev.weatherapp.network.HostRequestConstants;

public class NetworkWorkerService extends Worker {

    private Map<String, Object> mData;
    private String mRequestMethod;
    private String mRequestId;

    private Context mContex;
    private NotificationManager mNotificationManager;

    public NetworkWorkerService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContex = context;
        mData = new HashMap<>();
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @NonNull
    @Override
    public Result doWork() {
        URL url = null;
        try {
            url = buildRequestURL();
            ExternalUtils.printDebugLog(getClass().getSimpleName(), url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection;
        try {
            if (url != null) {
                connection = (HttpURLConnection) url.openConnection();
            } else {
                throw new NullPointerException("Url failed");
            }
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
            Data response = new Data.Builder().putString(mRequestId, responseString).build();
            return Result.success(response);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failure();
        }
    }

    private URL buildRequestURL() throws MalformedURLException {
        for (Map.Entry<String, Object> entry : getInputData().getKeyValueMap().entrySet()) {
            mData.put(entry.getKey(), entry.getValue());
        }
        String dataTransferProtocol = HostRequestConstants.DATA_TRANSFER_PROTOCOL;
        String host = HostRequestConstants.OPEN_WEATHER_HOST;
        String requestController = mData.get(HostRequestConstants.KEY_REQUEST_CONTROLLER).toString();
        mRequestMethod = mData.get(HostRequestConstants.KEY_REQUEST_METHOD).toString();
        mRequestId = mData.get(HostRequestConstants.KEY_REQUEST_ID).toString();
        mData.remove(HostRequestConstants.KEY_REQUEST_CONTROLLER);
        mData.remove(HostRequestConstants.KEY_REQUEST_METHOD);
        mData.remove(HostRequestConstants.KEY_REQUEST_ID);
        String requestParameters = getRequestParameters(mData);
        return new URL(String.format(Locale.ENGLISH, "%s%s%s%s",
                dataTransferProtocol,
                host,
                requestController,
                requestParameters));
    }

    private String getRequestParameters(Map<String, Object> data) {
        Uri.Builder builder = new Uri.Builder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue().toString());
        }
        builder.appendQueryParameter("apikey", BuildConfig.API_KEY);
        return builder.build().getEncodedQuery();
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
