package com.ideal.evecore.io;

import java.io.Closeable;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by chris on 21/04/2017.
 */
public abstract class UserServer<T> extends Thread implements Closeable {
    protected ServerSocket server;
    protected final int port;
    protected final AtomicBoolean running = new AtomicBoolean(true);

    public UserServer(int p) {
        port = p;
    }

    // TODO:
}
