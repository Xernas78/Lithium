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
        List<String> lines = new ArrayList<>();
        List<Header> headers = new ArrayList<>();

        ContentType contentType = response.getContentType();
        headers.add(Header.CONTENT_LENGTH.setValues(String.valueOf(response.getBody().getBytes(contentType.getCharset()).length)));
        headers.add(contentType.getHeader());
        headers.addAll(response.getHeaders());

        lines.add(Protocol.HTTP + " " + response.getStatus().getCode() + " " + response.getStatus().getMessage());
        headers.forEach(header -> lines.add(header.toString()));
        lines.add("");
        lines.add(response.getBody());

        writeLines(particle, lines);
    }

    private void writeLines(Particle particle, List<String> lines) throws Particle.WriteException {
        for (String line : lines) {
            particle.writeBytes((line + "\r\n").getBytes());
        }
        particle.flush();
    }

}
