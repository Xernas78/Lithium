package dev.xernas.lithium.plugin.listeners;

import dev.xernas.lithium.request.Request;
import dev.xernas.lithium.response.Response;

public interface Listener {

    void onRequest(Request request);

    void onResponse(Response response);

}
