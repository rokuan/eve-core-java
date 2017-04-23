package com.ideal.evecore.io;

import com.ideal.evecore.common.SocketWrapper;
import com.ideal.evecore.users.Session;
import com.ideal.evecore.util.Result;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by chris on 21/04/2017.
 */
public abstract class UserServer<T extends Session> extends Thread implements Closeable {
    protected ServerSocket server;
    protected final int port;
    protected final AtomicBoolean running = new AtomicBoolean(true);

    public UserServer(int p) throws IOException {
        port = p;
        server = new ServerSocket(port);
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                Socket client = server.accept();
                try {
                    SocketWrapper wrapper = new SocketWrapper(client);
                    String login = wrapper.readValue();
                    String password = wrapper.readValue();

                    Result<T> authenticationResult = authenticate(login, password);

                    if (authenticationResult.isSuccess()) {
                        wrapper.writeValue(true);
                        UserSocket<T> target = connectUser(client, authenticationResult.get());
                        target.start();
                    } else {
                        wrapper.writeValue(false);
                        client.close();
                    }
                } catch (Exception e) {
                    try { client.close(); } catch (Exception e1) {}
                }
            } catch (IOException e) {
                running.set(false);
            }
        }
    }

    abstract Result<T> authenticate(String login, String password);
    abstract UserSocket<T> connectUser(Socket socket, T user);

    @Override
    public void close() throws IOException {
        try {
            server.close();
        } catch (Exception e) {

        }
    }
}
