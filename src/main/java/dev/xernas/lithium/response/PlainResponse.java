package dev.xernas.lithium.response;

import dev.xernas.lithium.io.headers.ContentType;
import dev.xernas.lithium.io.headers.Header;
import dev.xernas.lithium.io.http.Status;

import java.util.List;

public class PlainResponse implements Response {

    private final String text;

    public PlainResponse(String text) {
        this.text = text;
    }

    @Override
    public ContentType getContentType() {
        return ContentType.TEXT_PLAIN;
    }

    @Override
    public List<Header> getHeaders() {
        return List.of();
    }

    @Override
    public String getBody() {
        return text;
    }

    @Override
    public Status getStatus() {
        return Status.OK;
    }
}
