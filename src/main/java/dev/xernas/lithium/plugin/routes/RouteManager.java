package dev.xernas.lithium.plugin.routes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RouteManager {

    private final Map<String, RouteHandler> routes = new HashMap<>();

    public void registerRoute(RouteHandler handler, String path) {
        if (!isValidPath(path)) throw new IllegalArgumentException("Invalid path: " + path);
        String finalPath = path.endsWith("/") ? path : path + "/";
        routes.put(finalPath, handler);
        routes.put(finalPath.substring(0, finalPath.length() - 1), handler);
    }

    public void registerRoute(RouteHandler handler, String firstPath, String... alias) {
        registerRoute(handler, firstPath);
        for (String path : alias) registerRoute(handler, path);
    }

    public Map<String, RouteHandler> getRoutes() {
        return routes;
    }

    private boolean isValidPath(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        return path.charAt(0) == '/';
    }

    public void clearRoutes() {
        routes.clear();
    }

}
