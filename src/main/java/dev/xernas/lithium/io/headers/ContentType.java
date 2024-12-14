package dev.xernas.lithium.io.headers;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ContentType {

    public static final ContentType TEXT_HTML = new ContentType(Header.CONTENT_TYPE_HTML);
    public static final ContentType TEXT_PLAIN = new ContentType(Header.CONTENT_TYPE_TEXT);
    public static final ContentType APPLICATION_JSON = new ContentType(Header.CONTENT_TYPE_JSON);
    public static final ContentType IMAGE_JPEG = new ContentType(Header.CONTENT_TYPE_JPEG);
    public static final ContentType IMAGE_PNG = new ContentType(Header.CONTENT_TYPE_PNG);
    public static final ContentType IMAGE_GIF = new ContentType(Header.CONTENT_TYPE_GIF);
    public static final ContentType IMAGE_SVG = new ContentType(Header.CONTENT_TYPE_SVG);

    private final Header value;
    private final Charset charset;

    public ContentType(Header value) {
        this.value = value;
        this.charset = StandardCharsets.UTF_8;
    }

    public ContentType(Header value, Charset charset) {
        this.value = value;
        this.charset = charset;
    }

    public Header getHeader() {
        return value;
    }

    public Charset getCharset() {
        return charset;
    }
}
