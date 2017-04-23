package com.ideal.evecore.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ideal.evecore.interpreter.Environment;
import com.ideal.evecore.interpreter.Evaluator;
import com.ideal.evecore.interpreter.QuerySource;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.remote.RemoteContext;
import com.ideal.evecore.interpreter.remote.RemoteReceiver;
import com.ideal.evecore.io.command.query.ObjectCommand;
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
                UserCommand command = request.second;
                StreamSource sender = new StreamSource(socket, request.first);

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
                }
            } catch (Exception e) {
                running.set(false);
            }
        }
    }

    protected void evaluate(String text, StreamSource sender) throws IOException {
        try {
            InterpretationObject o = parser.parseText(text);
            Result<EveObject> result = evaluator.eval(o);
            sender.writeResultResponse(mapper, result);
        } catch (Exception e) {
            sender.writeResultResponse(mapper, Result.ko(e));
        }
    }

    protected void redirectObjectCommand(ObjectRequestCommand command, StreamSource sender) throws IOException {
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
        } else {
            handler.commandOperation(command, mapper);
        }
    }

    protected void registerReceiver(StreamSource sender) throws IOException {
        String receiverId = freshId();
        RemoteReceiver remoteReceiver = new RemoteReceiver(receiverId, handler);
        receivers.put(receiverId, remoteReceiver);
        sources.put(receiverId, remoteReceiver);
        world.registerReceiver(remoteReceiver);
        sender.writeStringResponse(receiverId);
    }

    protected void registerContext(StreamSource sender) throws IOException {
        String contextId = freshId();
        RemoteContext remoteContext = new RemoteContext(contextId, handler);
        contexts.put(contextId, remoteContext);
        sources.put(contextId, remoteContext);
        environment.addContext(remoteContext);
        sender.writeStringResponse(contextId);
    }

    protected void unregisterReceiver(String receiverId) {
        RemoteReceiver receiver = receivers.remove(receiverId);
        sources.remove(receiverId);
        if (receiver != null) {
            world.unregisterReceiver(receiver);
        }
    }

    protected void unregisterContext(String contextId) {
        RemoteContext context = contexts.remove(contextId);
        sources.remove(contextId);
        if (context != null) {
            environment.remoteContext(context);
        }
    }

    private final String freshId() {
        return String.valueOf(idGenerator.getAndIncrement());
    }
}
