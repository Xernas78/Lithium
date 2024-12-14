package dev.xernas.lithium.plugin.routes;

import dev.xernas.lithium.request.Request;
import dev.xernas.lithium.response.Response;

public interface RouteHandler {

    Response handle(Request request);

}
