package dev.xernas.lithium.response;

import dev.xernas.lithium.io.http.Status;
import dev.xernas.lithium.io.headers.ContentType;
import dev.xernas.lithium.io.headers.Header;

import java.util.List;

public interface Response {

    ContentType getContentType();

    List<Header> getHeaders();

    String getBody();

    Status getStatus();

    default Header getHeader(String name) {
        return getHeaders().stream()
                .filter(header -> header.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

}
