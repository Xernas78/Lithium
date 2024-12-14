package dev.xernas.lithium.io.http;

public class Protocol {

    public static final Protocol HTTP = new Protocol("HTTP", "1.1");
    public static final Protocol HTTPS = new Protocol("HTTPS", "-");

    private final String name;
    private final String version;

    private Protocol(String name, String version) {
        this.name = name;
        this.version = version;
    }

    @Override
    public String toString() {
        return name + "/" + version;
    }

    public static Protocol fromString(String string) {
        return switch (string) {
            case "HTTP" -> HTTP;
            case "HTTPS" -> HTTPS;
            default -> null;
        };
    }

}
