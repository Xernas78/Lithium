package dev.xernas.lithium;

import dev.xernas.lithium.io.*;
import dev.xernas.lithium.plugin.PluginManager;
import dev.xernas.lithium.request.Request;
import dev.xernas.lithium.response.Response;
import dev.xernas.particle.Particle;
import dev.xernas.particle.client.Client;
import dev.xernas.particle.message.MessageIO;
import dev.xernas.particle.server.TCPServer;
import dev.xernas.particle.server.exceptions.ServerException;
import dev.xernas.particle.tasks.Task;
import org.jetbrains.annotations.NotNull;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Lithium extends TCPServer<Request, Response> {

    public static final Path PLUGINS_PATH = Paths.get("plugins");

    private static final Map<UUID, List<UUID>> sessions = new HashMap<>();
    private final int port;

    public Lithium(int port) {
        this.port = port;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public @NotNull List<Task> getRepeatedTasks() {
        return List.of();
    }

    @Override
    public @NotNull MessageIO<Request, Response> getMessageIO(UUID uuid) {
        return new WebIO();
    }

    @Override
    public void onServerStart() throws ServerException {
        PluginManager.findPlugins(PLUGINS_PATH);
        PluginManager.onEnable();
    }

    @Override
    public void onClientConnect(UUID uuid, Particle particle) throws ServerException {
    }

    @Override
    public void onMessage(UUID uuid, Request request, Particle particle) throws ServerException {
        Response response = PluginManager.onMessage(request);
        PluginManager.onResponse(response);
        send(uuid, response);
    }

    @Override
    public void onClientDisconnect(UUID uuid, Particle particle) throws ServerException {

    }

    @Override
    public void onClientConnectionEnd(UUID uuid, Client<Request, Response> client) throws ServerException {

    }

    @Override
    public void onServerStop() throws ServerException {
        PluginManager.onDisable();
    }

    public static UUID newSession(UUID uuid) {
        List<UUID> session = getSession(uuid);
        if (session == null) session = new ArrayList<>();
        session.add(uuid);
        UUID newSession = UUID.randomUUID();
        sessions.put(newSession, session);
        return newSession;
    }

    public static List<UUID> getSession(UUID uuid) {
        return sessions.get(uuid);
    }

}
