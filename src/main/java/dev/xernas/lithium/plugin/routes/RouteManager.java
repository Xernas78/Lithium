package dev.xernas.lithium.plugin.routes;

import java.util.HashMap;
import java.util.Map;

public class RouteManager {

    private final Map<String, RouteHandler> routes = new HashMap<>();

    public void registerRoute(String path, RouteHandler handler) {
        if (!isValidPath(path)) throw new IllegalArgumentException("Invalid path: " + path);
        String finalPath = path.endsWith("/") ? path : path + "/";
        routes.put(finalPath, handler);
        routes.put(finalPath.substring(0, finalPath.length() - 1), handler);
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

}
