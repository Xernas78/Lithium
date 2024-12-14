package dev.xernas.lithium.io.http;

public class Method {

    public static final Method GET = new Method("GET");
    public static final Method POST = new Method("POST");
    public static final Method PUT = new Method("PUT");
    public static final Method DELETE = new Method("DELETE");

    private final String name;

    private Method(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Method fromString(String string) {
        return switch (string) {
            case "GET" -> GET;
            case "POST" -> POST;
            case "PUT" -> PUT;
            case "DELETE" -> DELETE;
            default -> null;
        };
    }

}
