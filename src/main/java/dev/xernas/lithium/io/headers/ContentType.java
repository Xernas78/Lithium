package dev.xernas.lithium.io.headers;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ContentType {

    public static final ContentType TEXT_HTML = new ContentType(Header.CONTENT_TYPE_HTML);
    public static final ContentType TEXT_CSS = new ContentType(Header.CONTENT_TYPE_CSS);
    public static final ContentType APPLICATION_JAVASCRIPT = new ContentType(Header.CONTENT_TYPE_JS);
    public static final ContentType APPLICATION_PHP = new ContentType(Header.CONTENT_TYPE_PHP);
    public static final ContentType TEXT_PLAIN = new ContentType(Header.CONTENT_TYPE_TEXT);
    public static final ContentType APPLICATION_JSON = new ContentType(Header.CONTENT_TYPE_JSON);
    public static final ContentType IMAGE_JPEG = new ContentType(Header.CONTENT_TYPE_JPEG, StandardCharsets.ISO_8859_1);
    public static final ContentType IMAGE_PNG = new ContentType(Header.CONTENT_TYPE_PNG, StandardCharsets.ISO_8859_1);
    public static final ContentType IMAGE_GIF = new ContentType(Header.CONTENT_TYPE_GIF, StandardCharsets.ISO_8859_1);
    public static final ContentType IMAGE_SVG = new ContentType(Header.CONTENT_TYPE_SVG, StandardCharsets.ISO_8859_1);
    public static final ContentType IMAGE_ICO = new ContentType(Header.CONTENT_TYPE_ICO, StandardCharsets.ISO_8859_1);

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
