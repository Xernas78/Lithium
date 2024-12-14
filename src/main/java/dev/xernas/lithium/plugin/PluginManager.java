package dev.xernas.lithium.plugin;

import dev.xernas.lithium.io.http.Status;
import dev.xernas.lithium.plugin.listeners.ListenerManager;
import dev.xernas.lithium.plugin.routes.RouteHandler;
import dev.xernas.lithium.plugin.routes.RouteManager;
import dev.xernas.lithium.request.Request;
import dev.xernas.lithium.response.JSONResponse;
import dev.xernas.lithium.response.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager {

    private static final Map<Priority, Map<String, Plugin>> plugins = new HashMap<>();
    private static final RouteManager routeManager = new RouteManager();
    private static final ListenerManager listenerManager = new ListenerManager();

    public static void onEnable() {
        processPriority(Priority.HIGHEST, plugins -> plugins.values().forEach(Plugin::onEnable));
        processPriority(Priority.HIGH, plugins -> plugins.values().forEach(Plugin::onEnable));
        processPriority(Priority.NORMAL, plugins -> plugins.values().forEach(Plugin::onEnable));
        processPriority(Priority.LOW, plugins -> plugins.values().forEach(Plugin::onEnable));
        processPriority(Priority.LOWEST, plugins -> plugins.values().forEach(Plugin::onEnable));
    }

    public static void onDisable() {
        processPriority(Priority.HIGHEST, plugins -> plugins.values().forEach(Plugin::onDisable));
        processPriority(Priority.HIGH, plugins -> plugins.values().forEach(Plugin::onDisable));
        processPriority(Priority.NORMAL, plugins -> plugins.values().forEach(Plugin::onDisable));
        processPriority(Priority.LOW, plugins -> plugins.values().forEach(Plugin::onDisable));
        processPriority(Priority.LOWEST, plugins -> plugins.values().forEach(Plugin::onDisable));
    }

    public static Response onMessage(Request request) {
        Response currentResponse = new JSONResponse("{\"error\": \"Not found\"}", Status.NOT_FOUND);
        for (Map.Entry<String, RouteHandler> entry : routeManager.getRoutes().entrySet()) {
            if (request.path().equalsIgnoreCase(entry.getKey())) {
                currentResponse = entry.getValue().handle(request);
            }
        }
        listenerManager.getListeners().forEach(listener -> listener.onRequest(request));
        return currentResponse;
    }

    public static void onResponse(Response response) {
        listenerManager.getListeners().forEach(listener -> listener.onResponse(response));
    }

    public static RouteManager getRouteManager() {
        return routeManager;
    }

    public static ListenerManager getListenerManager() {
        return listenerManager;
    }

    private static void registerPlugin(String name, Plugin plugin) {
        Map<String, Plugin> plugins = PluginManager.plugins.get(plugin.getPriority());
        if (plugins == null) plugins = new HashMap<>();
        plugins.put(name, plugin);
        PluginManager.plugins.put(plugin.getPriority(), plugins);
    }

    public static void findPlugins(Path path) {
        File pluginsFolder = path.toFile();
        if (!pluginsFolder.exists()) {
            if (!pluginsFolder.mkdirs()) {
                throw new RuntimeException("Could not create plugins folder");
            }
        }
        if (!pluginsFolder.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory");
        }
        File[] jarFiles = pluginsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (jarFiles == null) return;
        for (File jarFile : jarFiles) {
            try {
                loadPlugin(jarFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadPlugin(File jarFile) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        URL[] urls = { jarFile.toURI().toURL() };

        try (URLClassLoader classLoader = new URLClassLoader(urls, PluginManager.class.getClassLoader())) {

            try (JarFile jar = new JarFile(jarFile)) {
                JarEntry entry = jar.getJarEntry("plugin.yml");
                if (entry == null) {
                    throw new FileNotFoundException("plugin.yml not found in " + jarFile.getName());
                }

                InputStream input = jar.getInputStream(entry);
                Properties properties = new Properties();
                properties.load(input);

                String mainClass = properties.getProperty("main");
                if (mainClass == null) {
                    throw new IllegalArgumentException("Main class not specified in plugin.yml");
                }

                Class<?> clazz = classLoader.loadClass(mainClass);
                if (!Plugin.class.isAssignableFrom(clazz)) {
                    throw new IllegalArgumentException(mainClass + " does not implement Plugin interface");
                }

                // Load all classes from the JAR
                loadAllClassesFromJar(classLoader, jar);

                String name = properties.getProperty("name");
                Plugin plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();
                registerPlugin(name, plugin);
            }
        }
    }

    private static void loadAllClassesFromJar(URLClassLoader classLoader, JarFile jar) throws ClassNotFoundException {
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class")) {
                String className = entry.getName().replace('/', '.').replace(".class", "");
                classLoader.loadClass(className);
            }
        }
    }

    private static void processPriority(Priority priority, Consumer<Map<String, Plugin>> consumer) {
        Map<String, Plugin> plugins = PluginManager.plugins.get(priority);
        if (plugins != null) consumer.accept(plugins);
    }

}
