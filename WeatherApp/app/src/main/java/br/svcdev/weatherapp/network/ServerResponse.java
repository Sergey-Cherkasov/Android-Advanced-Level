package br.svcdev.weatherapp.network;

import java.util.Map;

/** Интерфейс для реализации обработки ответа от сервера в UI.*/
public interface ServerResponse {

    void onServerResponse(Map<String,String> response);

}
