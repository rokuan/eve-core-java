package com.ideal.evecore.io;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideal.evecore.common.Credentials;
import com.ideal.evecore.common.SocketWrapper;
import com.ideal.evecore.interpreter.Context;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.remote.ObjectStreamSource;
import com.ideal.evecore.interpreter.remote.StreamContext;
import com.ideal.evecore.interpreter.remote.StreamReceiver;
import com.ideal.evecore.io.command.context.ContextCommand;
import com.ideal.evecore.io.command.query.ObjectCommand;
import com.ideal.evecore.io.command.user.*;
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

    private final AtomicBoolean running = new AtomicBoolean(true);

    public UserConnection(String h, int p, Credentials c) throws Exception {
        socket = new Socket(h, p);
        handler = new StreamHandler(socket);
        handlerThread = new Thread(handler);
        // TODO: fill the mapper
        mapper = new ObjectMapper();
        authenticate(c);
        handlerThread.start();
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
            String receiverId = handler.stringOperation(new RegisterReceiverCommand(), mapper);
            StreamReceiver streamReceiver = new StreamReceiver(receiverId, handler, r);
            receivers.put(receiverId, streamReceiver);
            sources.put(receiverId, streamReceiver);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerContext(Context c) {
        try {
            String contextId = handler.stringOperation(new RegisterContextCommand(), mapper);
            StreamContext streamContext = new StreamContext(contextId, handler, c);
            contexts.put(contextId, streamContext);
            sources.put(contextId, streamContext);
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
                running.set(false);
            } catch (IOException e) {
                running.set(false);
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
        try { socket.close(); } catch (Exception e) {}
    }
}
