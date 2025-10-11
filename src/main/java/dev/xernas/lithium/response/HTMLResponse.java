package dev.xernas.lithium.response;

import dev.xernas.lithium.io.headers.ContentType;
import dev.xernas.lithium.io.headers.Header;
import dev.xernas.lithium.io.http.Status;

import java.util.List;

public class HTMLResponse implements Response {

    private final String html;
    private final Status status;

    public HTMLResponse(String html, Status status) {
        this.html = html;
        this.status = status;
    }

    @Override
    public ContentType getContentType() {
        return ContentType.TEXT_HTML;
    }

    @Override
    public List<Header> getHeaders() {
        return List.of();
    }

    @Override
    public byte[] getBody() {
        return html.getBytes();
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
