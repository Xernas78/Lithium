package dev.xernas.lithium.io.headers;

import java.util.List;

public enum Header {

    CONTENT_TYPE_HTML("Content-Type", "text/html", "charset=utf-8"),
    CONTENT_TYPE_JSON("Content-Type", "application/json", "charset=utf-8"),
    CONTENT_TYPE_CSS("Content-Type", "text/css"),
    CONTENT_TYPE_JS("Content-Type", "application/javascript"),
    CONTENT_TYPE_TEXT("Content-Type", "text/plain", "charset=utf-8"),
    CONTENT_TYPE_PHP("Content-Type", "application/x-httpd-php"),
    CONTENT_TYPE_JPEG("Content-Type", "image/jpeg"),
    CONTENT_TYPE_PNG("Content-Type", "image/png"),
    CONTENT_TYPE_GIF("Content-Type", "image/gif"),
    CONTENT_TYPE_SVG("Content-Type", "image/svg+xml"),
    CONTENT_TYPE_ICO("Content-Type", "image/x-icon"),

    IN_ACCEPT("Accept"),
    IN_COOKIE("Cookie"),

    CONTENT_LENGTH("Content-Length", "0"),
    SET_COOKIE("Set-Cookie", "");

    private final String name;
    private String[] value;

    Header(String name) {
        this.name = name;
    }

    Header(String name, String... value) {
        this.name = name;
        this.value = value;
    }

    public Header setValues(String... value) {
        this.value = value;
        return this;
    }

    public String getName() {
        return name;
    }

    public String[] getValue() {
        return value;
    }

    public boolean isSame(Header header) {
        return name.equals(header.getName());
    }

    @Override
    public String toString() {
        return name + ": " + valuesToString();
    }

    public static Header fromString(boolean in, String header) {
        String[] parts = header.split(": ");
        if (parts.length != 2) return null;
        for (Header h : values()) {
            if (in) {
                if (h.getName().equals(parts[0])) {
                    return h.setValues(parts[1]);
                }
            } else {
                if (h.getName().equals(parts[0]) && h.contains(parts[1])) {
                    return h;
                }
            }
        }
        return null;
    }

    private String valuesToString() {
        StringBuilder sb = new StringBuilder();
        for (String v : this.value) {
            sb.append(v).append("; ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    private boolean contains(String value) {
        for (String v : this.value) {
            if (v.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
