package com.ideal.evecore.io;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.Socket;
import static com.ideal.evecore.io.StreamHandler.*;

/**
 * Created by chris on 07/04/2017.
 */
public class StreamSource extends StreamUtils {
    private String sourceId;

    public StreamSource(Socket s, String id) throws IOException {
        super(s);
        sourceId = id;
    }

    public void writeBooleanResponse(boolean response) throws IOException {
        os.write(BOOLEAN_RESULT);
        writeValue(sourceId);
        writeValue(response);
    }

    public void writeStringResponse(String response) throws IOException {
        os.write(STRING_RESULT);
        writeValue(sourceId);
        writeValue(response);
    }

    public <T> void writeResponse(ObjectMapper responseMapper, T response) throws IOException {
        os.write(OBJECT_RESULT);
        writeValue(sourceId);
        writeItem(responseMapper, response);
    }
}
