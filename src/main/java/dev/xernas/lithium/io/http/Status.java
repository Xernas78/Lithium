package dev.xernas.lithium.io.http;

public enum Status {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    BAD_REQUEST(400, "Bad Request"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    FORBIDDEN(403, "Forbidden"),
    UNAUTHORIZED(401, "Unauthorized"),
    CREATED(201, "Created"),
    NO_CONTENT(204, "No Content");

    private final int code;
    private final String message;

    Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
