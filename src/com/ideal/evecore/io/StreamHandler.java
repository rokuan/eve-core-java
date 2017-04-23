package com.ideal.evecore.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.io.command.user.UserCommand;
import com.ideal.evecore.io.serialization.EveObjectSerialization;
import com.ideal.evecore.util.Pair;
import com.ideal.evecore.util.PendingAtomicReference;
import com.ideal.evecore.util.Result;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Christophe on 07/04/2017.
 */
public class StreamHandler extends StreamUtils implements Runnable {
    public static final int COMMAND = 0;
    public static final int BOOLEAN_RESULT = 1;
    public static final int STRING_RESULT = 2;
    public static final int OBJECT_RESULT = 3;

    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Map<String, PendingAtomicReference<String>> objectResults = new HashMap<String, PendingAtomicReference<String>>();
    private final Map<String, PendingAtomicReference<String>> stringResults = new HashMap<String, PendingAtomicReference<String>>();
    private final Map<String, PendingAtomicReference<Boolean>> booleanResults = new HashMap<String, PendingAtomicReference<Boolean>>();
    private final AtomicLong stamp = new AtomicLong(0);
    private final LinkedBlockingQueue<Pair<String, UserCommand>> commands = new LinkedBlockingQueue<Pair<String, UserCommand>>(1);

    private final ObjectMapper mapper;

    public StreamHandler(Socket s) throws IOException {
        super(s);
        // TODO: fill the mapper with the basic serializers/deserializers
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(EveObject.class, new EveObjectSerialization.EveObjectDeserializer(this));
        mapper.registerModule(module);
    }

    @Override
    public void run() {
        while (running.get()) {
            synchronized (is) {
                try {
                    int token = is.read();

                    switch (token) {
                        case COMMAND:
                            handleUserCommand();
                            break;
                        case BOOLEAN_RESULT:
                            handleBooleanAnswer();
                            break;
                        case STRING_RESULT:
                            handleStringAnswer();
                            break;
                        case OBJECT_RESULT:
                            handleObjectAnswer();
                            break;
                        case -1:
                            throw new IOException("Remote has disconnected");
                    }
                } catch (IOException e) {
                    running.set(false);
                }
            }
        }
    }

    protected void handleUserCommand() throws IOException {
        String requestId = readValue();
        UserCommand command = readUserCommand(mapper);
        commands.offer(new Pair<String, UserCommand>(requestId, command));
    }

    protected void handleBooleanAnswer() throws IOException {
        String requestId = readValue();
        boolean value = readTest();
        PendingAtomicReference<Boolean> reference = booleanResults.remove(requestId);
        if (reference != null) {
            reference.set(value);
        }
    }

    protected void handleStringAnswer() throws IOException {
        String requestId = readValue();
        String value = readValue();
        PendingAtomicReference<String> reference = stringResults.remove(requestId);
        if (reference != null) {
            reference.set(value);
        }
    }

    protected void handleObjectAnswer() throws IOException {
        String requestId = readValue();
        String json = readValue();
        PendingAtomicReference<String> reference = objectResults.remove(requestId);
        if (reference != null) {
            reference.set(json);
        }
    }

    public boolean booleanOperation(UserCommand command, ObjectMapper resultMapper) throws IOException {
        String operationId = String.valueOf(stamp.getAndIncrement());
        PendingAtomicReference<Boolean> reference = new PendingAtomicReference<Boolean>();
        synchronized (os) {
            booleanResults.put(operationId, reference);
            os.write(COMMAND);
            writeValue(operationId);
            writeUserCommand(resultMapper, command);
        }
        return reference.get();
    }

    public String stringOperation(UserCommand command, ObjectMapper resultMapper) throws IOException {
        String operationId = String.valueOf(stamp.getAndIncrement());
        PendingAtomicReference<String> reference = new PendingAtomicReference<String>();
        synchronized (os) {
            stringResults.put(operationId, reference);
            os.write(COMMAND);
            writeValue(operationId);
            writeUserCommand(resultMapper, command);
        }
        return reference.get();
    }

    public <T> T objectOperation(UserCommand command, ObjectMapper resultMapper, Class<T> clazz) throws IOException {
        String json = getJson(command, resultMapper);
        return resultMapper.readValue(json, clazz);
    }

    public <T> T objectOperation(UserCommand command, ObjectMapper resultMapper, TypeReference<T> typeReference) throws IOException {
        String json = getJson(command, resultMapper);
        return resultMapper.readValue(json, typeReference);
    }

    public <T> T objectOperation(UserCommand command, ObjectMapper resultMapper, JavaType t) throws IOException {
        String json = getJson(command, resultMapper);
        return resultMapper.readValue(json, t);
    }

    public <T> Result<T> resultOperation(UserCommand command, ObjectMapper resultMapper, Class<T> clazz) throws IOException {
        String json = getJson(command, resultMapper);
        JsonNode node = resultMapper.readTree(json);

        if (node.get("success").asBoolean(false)) {
            return Result.ok(resultMapper.treeToValue(node.get("value"), clazz));
        } else {
            return Result.ko(node.get("error").asText());
        }
    }

    protected String getJson(UserCommand command, ObjectMapper resultMapper) throws IOException {
        String operationId = String.valueOf(stamp.getAndIncrement());
        PendingAtomicReference<String> reference = new PendingAtomicReference<String>();
        synchronized (os) {
            objectResults.put(operationId, reference);
            os.write(COMMAND);
            writeValue(operationId);
            writeUserCommand(resultMapper, command);
        }
        return reference.get();
    }

    public void commandOperation(UserCommand command, ObjectMapper resultMapper) throws IOException {
        String operationId = String.valueOf(stamp.getAndIncrement());
        synchronized (os) {
            os.write(COMMAND);
            writeValue(operationId);
            writeUserCommand(resultMapper, command);
        }
    }

    public Pair<String, UserCommand> nextCommand() throws InterruptedException {
        return commands.take();
    }
}
