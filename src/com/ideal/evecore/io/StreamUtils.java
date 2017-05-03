package com.ideal.evecore.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ideal.evecore.io.command.user.UserCommand;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Christophe on 07/04/2017.
 */
public abstract class StreamUtils extends BasicSocketUtils {
    protected StreamUtils(Socket s) throws IOException {
        super(s);
    }

    protected UserCommand readUserCommand(ObjectMapper mapper) throws IOException {
        String json = readValue();
        return mapper.readValue(json, UserCommand.class);
    }

    protected void writeUserCommand(ObjectMapper mapper, UserCommand command) throws IOException {
        String json = mapper.writeValueAsString(command);
        writeValue(json);
    }

    protected <T> T readItem(ObjectMapper mapper, Class<T> clazz) throws IOException {
        String json = readValue();
        return mapper.readValue(json, clazz);
    }

    protected <T> void writeItem(ObjectMapper mapper, T t) throws IOException {
        String json = mapper.writeValueAsString(t);
        writeValue(json);
    }

    protected <T> void writeItem(ObjectWriter writer, T t) throws IOException {
        String json = writer.writeValueAsString(t);
        writeValue(json);
    }
}
