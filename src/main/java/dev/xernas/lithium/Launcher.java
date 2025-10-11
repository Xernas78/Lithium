package dev.xernas.lithium;


import dev.xernas.lithium.plugin.PluginManager;
import dev.xernas.particle.server.exceptions.ServerException;

public class Launcher {
    public static void main(String[] args) {
        int port = 4242;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number: " + args[0]);
                System.exit(1);
            }
        }
        try {
            Lithium server = new Lithium(port);
            PluginManager.setServerInstance(server);
            server.listen();
        } catch (ServerException e) {
            throw new RuntimeException(e);
        }
    }
}