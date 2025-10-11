package dev.xernas.lithium.request;

import dev.xernas.lithium.io.http.Method;
import dev.xernas.lithium.io.http.Protocol;
import dev.xernas.lithium.io.headers.Header;

import java.io.BufferedReader;
import java.io.IOException;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Request(Protocol protocol, Method method, String path,
                      List<Header> headers, String body) {

    public static Request parse(BufferedReader reader, String info) throws ServerException {
        try {
            String[] parts = info.split(" ");
            if (parts.length != 3) throw new IOException("Request not supported");

            Method method = Method.fromString(parts[0]);
            Protocol protocol = Protocol.fromString(parts[2].split("/")[0]);
            List<Header> headers = parseHeaders(reader);
            String path = parts[1];

            String body = parseBody(reader, headers);

            return new Request(protocol, method, path, headers, body);
        } catch (IOException e) {
            throw new ServerException("Couldn't parse request", e);
        }
    }

    private static List<Header> parseHeaders(BufferedReader reader) throws IOException {
        List<Header> headers = new ArrayList<>();
        String header = reader.readLine();
        while (header != null && !header.isEmpty()) {
            header = reader.readLine();
            Header h = Header.fromString(true, header);
            if (h != null) headers.add(h);
        }
        return headers;
    }

    private  static String parseBody(BufferedReader reader, List<Header> headers) throws IOException {
        Header contentLengthHeader = headers.stream()
                .filter(h -> h.isSame(Header.CONTENT_LENGTH))
                .findFirst()
                .orElse(null);
        if (contentLengthHeader == null) {
            return null;
        }
        int contentLength = Integer.parseInt(contentLengthHeader.getValue()[0]);

        char[] bodyChars = new char[contentLength];
        int read = reader.read(bodyChars, 0, contentLength);
        return (read > 0) ? new String(bodyChars, 0, read) : null;
    }

    public Header getHeader(String name) {
        return headers.stream()
                .filter(header -> header.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Method: ").append(method).append("\n");
        builder.append("Protocol: ").append(protocol).append("\n");
        builder.append("Path: ").append(path).append("\n");
        builder.append("Headers:\n");
        for (Header header : headers) {
            builder.append("    ").append(header).append("\n");
        }
        return builder.toString();
    }

}
