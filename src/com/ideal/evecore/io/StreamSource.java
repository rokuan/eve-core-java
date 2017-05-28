package com.ideal.evecore.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ideal.evecore.util.Result;

import java.io.IOException;
import java.net.Socket;

import static com.ideal.evecore.io.StreamHandler.*;

/**
 * Created by Christophe on 07/04/2017.
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

    public <T> void writeResponse(ObjectMapper responseMapper, T response, TypeReference<T> t) throws IOException {
        os.write(OBJECT_RESULT);
        writeValue(sourceId);
        writeItem(responseMapper.writerFor(t), response);
    }

    public <T> void writeResponse(ObjectMapper responseMapper, T response, JavaType t) throws IOException {
        os.write(OBJECT_RESULT);
        writeValue(sourceId);
        writeItem(responseMapper.writerFor(t), response);
    }

    public <T> void writeResultResponse(ObjectMapper responseMapper, Result<T> response) throws IOException {
        os.write(OBJECT_RESULT);
        writeValue(sourceId);
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ObjectNode value = factory.objectNode();
        if (response.isSuccess()) {
            value.put("success", true);
            value.put("value", responseMapper.valueToTree(response.get()));
        } else {
            value.put("success", false);
            value.put("error", response.getError().getMessage());
        }
        writeValue(value.toString());
    }
}
