package br.svcdev.weatherapp.network;

import java.util.Map;

public interface ServerResponse {

    void onServerResponse(Map<String,String> response);

}
