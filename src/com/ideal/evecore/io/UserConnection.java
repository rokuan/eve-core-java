package com.ideal.evecore.io;


import com.ideal.evecore.common.Credentials;
import com.ideal.evecore.common.SocketWrapper;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserConnection extends Thread {
    private final String host;
    private final int port;
    private final Socket socket;
    private final StreamHandler handler;
    private final Thread handlerThread;

    private final AtomicBoolean running = new AtomicBoolean(true);

    public UserConnection(String h, int p, Credentials c) throws Exception {
        host = h;
        port = p;
        socket = new Socket(host, port);
        handler = new StreamHandler(socket);
        handlerThread = new Thread(handler);
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

    @Override
    public void run() {
        while (running.get()) {

        }
    }
}
