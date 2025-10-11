package dev.xernas.lithium.io;

import dev.xernas.lithium.io.headers.ContentType;
import dev.xernas.lithium.io.headers.Header;
import dev.xernas.lithium.io.http.Protocol;
import dev.xernas.lithium.request.Request;
import dev.xernas.lithium.response.Response;
import dev.xernas.particle.Particle;
import dev.xernas.particle.message.MessageIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class WebIO implements MessageIO<Request, Response> {

    @Override
    public Request read(Particle particle) throws Particle.ReadException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(particle.in()));
        try {
            String info = reader.readLine();

            if (info == null) {
                throw new Particle.ReadException("Couldn't get request info");
            }
            return Request.parse(reader, info);
        } catch (IOException e) {
            throw new Particle.ReadException("Couldn't read request", e);
        }
    }

    @Override
    public void write(Response response, Particle particle) throws Particle.WriteException {
        // --- Build status line and headers ---
        StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append(Protocol.HTTP)
                .append(" ")
                .append(response.getStatus().getCode())
                .append(" ")
                .append(response.getStatus().getMessage())
                .append("\r\n");

        // Collect headers
        List<Header> headers = new ArrayList<>();
        ContentType contentType = response.getContentType();
        headers.add(Header.CONTENT_LENGTH.setValues(String.valueOf(response.getBody().length)));
        headers.add(contentType.getHeader());
        headers.addAll(response.getHeaders());

        // Append headers
        for (Header header : headers) {
            headerBuilder.append(header.toString()).append("\r\n");
        }
        headerBuilder.append("\r\n"); // blank line separating headers from body

        // --- Write headers ---
        particle.writeBytes(headerBuilder.toString().getBytes());

        // --- Write raw body ---
        particle.writeBytes(response.getBody());
        particle.flush();
    }

}
