package com.ideal.evecore.io;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ideal.evecore.common.Credentials;
import com.ideal.evecore.common.SocketWrapper;
import com.ideal.evecore.interpreter.Context;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.interpreter.remote.ObjectStreamSource;
import com.ideal.evecore.interpreter.remote.StreamContext;
import com.ideal.evecore.interpreter.remote.StreamReceiver;
import com.ideal.evecore.io.command.context.ContextCommand;
import com.ideal.evecore.io.command.query.ObjectCommand;
import com.ideal.evecore.io.command.user.*;
import com.ideal.evecore.io.serialization.EveObjectSerialization;
import com.ideal.evecore.io.serialization.EveStructuredObjectSerialization;
import com.ideal.evecore.universe.receiver.Receiver;
import com.ideal.evecore.util.Pair;
import com.ideal.evecore.util.Result;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserConnection extends Thread {
    private final Socket socket;
    private final StreamHandler handler;
    private final Thread handlerThread;

    private final ObjectMapper mapper;

    private final Map<String, StreamReceiver> receivers = new HashMap<String, StreamReceiver>();
    private final Map<String, StreamContext> contexts = new HashMap<String, StreamContext>();
    private final Map<String, ObjectStreamSource> sources = new HashMap<String, ObjectStreamSource>();
    private final Map<Receiver, String> receiverIds = new HashMap<Receiver, String>();
    private final Map<Context, String> contextIds = new HashMap<Context, String>();

    private final AtomicBoolean running = new AtomicBoolean(true);

    public UserConnection(String h, int p, Credentials c) throws Exception {
        socket = new Socket(h, p);
        handler = new StreamHandler(socket);
        handlerThread = new Thread(handler);
        // TODO: fill the mapper
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(EveObject.class, new EveObjectSerialization.EveObjectDeserializer(handler));
        module.addDeserializer(EveStructuredObject.class, new EveStructuredObjectSerialization.EveStructuredObjectDeserializer(handler));
        mapper.registerModule(module);
        authenticate(c);
    }

    private void authenticate(Credentials credentials) throws Exception {
        SocketWrapper wrapper = new SocketWrapper(socket);
        wrapper.writeValue(credentials.getLogin());
        wrapper.writeValue(credentials.getPassword());

        if (!wrapper.readTest()) {
            throw new Exception("Failed to authenticate");
        } else {
            handlerThread.start();
        }
    }

    public void registerReceiver(Receiver r) {
        try {
            String receiverId = handler.stringOperation(RegisterReceiverCommand.REGISTER_RECEIVER_COMMAND, mapper);
            StreamReceiver streamReceiver = new StreamReceiver(receiverId, handler, r);
            receiverIds.put(r, receiverId);
            receivers.put(receiverId, streamReceiver);
            sources.put(receiverId, streamReceiver);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerContext(Context c) {
        try {
            String contextId = handler.stringOperation(RegisterContextCommand.REGISTER_CONTEXT_COMMAND, mapper);
            StreamContext streamContext = new StreamContext(contextId, handler, c);
            contextIds.put(c, contextId);
            contexts.put(contextId, streamContext);
            sources.put(contextId, streamContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unregisterReceiver(Receiver r) {
        try {
            String receiverId = receiverIds.get(r);
            if (receiverId != null) {
                handler.commandOperation(new UnregisterReceiverCommand(receiverId), mapper);
                receivers.remove(receiverId);
                sources.remove(receiverId);
                receiverIds.remove(r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unregisterContext(Context c) {
        try {
           String contextId = contextIds.get(c);
            if (contextId != null) {
                handler.commandOperation(new UnregisterContextCommand(contextId), mapper);
                contexts.remove(contextId);
                sources.remove(contextId);
                contextIds.remove(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Result<EveObject> evaluate(String text) {
        try {
            return handler.resultOperation(new EvaluateCommand(text), mapper, EveObject.class);
        } catch (IOException e) {
            return Result.ko(e);
        }
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                Pair<String, UserCommand> parameters = handler.nextCommand();
                StreamSource source = new StreamSource(socket, parameters.first);
                UserCommand command = parameters.second;

                if (command instanceof ReceiverRequestCommand) {
                    executeReceiverCommand((ReceiverRequestCommand) command, source);
                } else if (command instanceof ContextRequestCommand) {
                    executeContextCommand((ContextRequestCommand) command, source);
                } else if (command instanceof ObjectRequestCommand) {
                    executeObjectCommand((ObjectRequestCommand) command, source);
                }
            } catch (InterruptedException e) {
                disconnect();
            } catch (IOException e) {
                disconnect();
            }
        }
    }

    protected void executeReceiverCommand(ReceiverRequestCommand command, StreamSource source) throws IOException {
        StreamReceiver receiver = receivers.get(command.getReceiverId());
        if(receiver != null){
            receiver.handleCommand(command.getReceiverCommand(), source);
        }
    }

    protected void executeContextCommand(ContextRequestCommand command, StreamSource source) throws IOException {
        StreamContext context = contexts.get(command.getContextId());
        if(context != null){
            context.handleCommand(command.getContextCommand(), source);
        }
    }

    protected void executeObjectCommand(ObjectRequestCommand command, StreamSource source) throws IOException {
        ObjectStreamSource domain = sources.get(command.getDomainId());
        if(domain != null){
            ObjectCommand delegateCommand = new ObjectCommand(command.getObjectId(), command.getObjectCommand());
            domain.handleObjectCommand(delegateCommand, source);
        }
    }

    public void disconnect() {
        running.set(false);
        try { socket.close(); } catch (Throwable t) {}
        try { handlerThread.interrupt(); } catch (Throwable t) {}
    }
}
