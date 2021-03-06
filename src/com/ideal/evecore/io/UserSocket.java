package com.ideal.evecore.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ideal.evecore.interpreter.Environment;
import com.ideal.evecore.interpreter.Evaluator;
import com.ideal.evecore.interpreter.QuerySource;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.remote.RemoteContext;
import com.ideal.evecore.interpreter.remote.RemoteReceiver;
import com.ideal.evecore.io.command.structured.*;
import com.ideal.evecore.io.command.user.*;
import com.ideal.evecore.io.serialization.EveObjectSerialization;
import com.ideal.evecore.universe.World;
import com.ideal.evecore.users.Session;
import com.ideal.evecore.util.Pair;
import com.ideal.evecore.util.Result;
import com.rokuan.calliopecore.parser.AbstractParser;
import com.rokuan.calliopecore.sentence.structure.InterpretationObject;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Christophe on 22/04/2017.
 */
public abstract class UserSocket<T extends Session> extends Thread {
    protected final Socket socket;
    protected final T session;
    protected final StreamHandler handler;
    protected final Thread handlerThread;
    protected final Evaluator evaluator;
    protected final AbstractParser parser;
    protected final Environment environment;
    protected final World world;

    private final AtomicBoolean running = new AtomicBoolean(true);
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    private Map<String, RemoteReceiver> receivers = new HashMap<String, RemoteReceiver>();
    private Map<String, RemoteContext> contexts = new HashMap<String, RemoteContext>();
    private Map<String, QuerySource> sources = new HashMap<String, QuerySource>();

    protected final ObjectMapper mapper;

    public UserSocket(Socket s, T u, AbstractParser p, Evaluator e, Environment env, World w) throws IOException {
        socket = s;
        session = u;
        parser = p;
        evaluator = e;
        environment = env;
        world = w;
        handler = new StreamHandler(socket);
        handlerThread = new Thread(handler);
        handlerThread.start();
        mapper = new ObjectMapper();
        // TODO: fill the mapper
        SimpleModule eveObjectModule = new SimpleModule();
        eveObjectModule.addSerializer(EveObject.class, new EveObjectSerialization.EveObjectSerializer(null));
        eveObjectModule.addDeserializer(EveObject.class, new EveObjectSerialization.EveObjectDeserializer(handler));
        mapper.registerModule(eveObjectModule);
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                Pair<String, UserCommand> request = handler.nextCommand();
                final UserCommand command = request.second;
                final StreamSource sender = new StreamSource(socket, request.first);

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            if (command instanceof ObjectRequestCommand) {
                                redirectObjectCommand((ObjectRequestCommand) command, sender);
                            } else if (command instanceof EvaluateCommand) {
                                evaluate(((EvaluateCommand) command).getText(), sender);
                            } else if (command instanceof RegisterContextCommand) {
                                registerContext(sender);
                            } else if (command instanceof RegisterReceiverCommand) {
                                registerReceiver(sender);
                            } else if (command instanceof UnregisterContextCommand) {
                                unregisterContext(((UnregisterContextCommand) command).getContextId());
                            } else if (command instanceof UnregisterReceiverCommand) {
                                unregisterReceiver(((UnregisterReceiverCommand) command).getReceiverId());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            } catch (Exception e) {
                running.set(false);
            }
        }
    }

    /**
     * Evaluate a text and write the result back to the sender
     * @param text The text to parse and evaluate
     * @param sender The sender of the request
     * @throws IOException
     */
    protected void evaluate(String text, StreamSource sender) throws IOException {
        try {
            InterpretationObject o = parser.parseText(text);
            Result<EveObject> result = evaluator.eval(o);
            sender.writeResultResponse(mapper, result);
        } catch (Exception e) {
            sender.writeResultResponse(mapper, Result.ko(e));
        }
    }

    /**
     * Redirects a command right back to the User so it can handle it and then redirect the result to the sender
     * @param command The received command
     * @param sender The initial sender
     * @throws IOException
     */
    protected void redirectObjectCommand(ObjectRequestCommand command, StreamSource sender) throws IOException, InterruptedException {
        EveStructuredObjectCommand oc = command.getObjectCommand();
        if (oc instanceof GetTypeCommand) {
            String type = handler.stringOperation(command, mapper);
            sender.writeStringResponse(type);
        } else if (oc instanceof GetFieldCommand) {
            Result<EveObject> value = handler.resultOperation(command, mapper, EveObject.class);
            sender.writeResultResponse(mapper, value);
        } else if (oc instanceof HasFieldCommand) {
            boolean has = handler.booleanOperation(command, mapper);
            sender.writeBooleanResponse(has);
        } else if (oc instanceof GetStateCommand) {
            String value = handler.stringOperation(command, mapper);
            sender.writeStringResponse(value);
        } else if (oc instanceof HasStateCommand) {
            boolean has = handler.booleanOperation(command, mapper);
            sender.writeBooleanResponse(has);
        } else if (oc instanceof CallActionCommand) {
            boolean returnedValue = handler.booleanOperation(command, mapper);
            sender.writeBooleanResponse(returnedValue);
        } else {
            handler.commandOperation(command, mapper);
        }
    }

    /**
     * Registers a new receiver to this world and writes back its newly generated ID
     * @param sender
     * @throws IOException
     */
    protected void registerReceiver(StreamSource sender) throws IOException {
        String receiverId = freshId();
        RemoteReceiver remoteReceiver = new RemoteReceiver(receiverId, handler);
        receivers.put(receiverId, remoteReceiver);
        sources.put(receiverId, remoteReceiver);
        sender.writeStringResponse(receiverId);
        world.registerReceiver(remoteReceiver);
    }

    /**
     * Registers a new context to this environment and writes back its newly generated ID
     * @param sender
     * @throws IOException
     */
    protected void registerContext(StreamSource sender) throws IOException {
        String contextId = freshId();
        RemoteContext remoteContext = new RemoteContext(contextId, handler);
        contexts.put(contextId, remoteContext);
        sources.put(contextId, remoteContext);
        sender.writeStringResponse(contextId);
        environment.addContext(remoteContext);
    }

    /**
     * Removes a receiver from this world
     * @param receiverId This receiver's ID
     */
    protected void unregisterReceiver(String receiverId) {
        RemoteReceiver receiver = receivers.remove(receiverId);
        sources.remove(receiverId);
        if (receiver != null) {
            world.unregisterReceiver(receiver);
        }
    }

    /**
     * Removes a context from this environment
     * @param contextId This context's ID
     */
    protected void unregisterContext(String contextId) {
        RemoteContext context = contexts.remove(contextId);
        sources.remove(contextId);
        if (context != null) {
            environment.removeContext(context);
        }
    }

    /**
     * Generates a new ID
     * @return a freshly created ID
     */
    private final String freshId() {
        return String.valueOf(idGenerator.getAndIncrement());
    }
}
