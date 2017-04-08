package com.ideal.evecore.common;

import com.ideal.evecore.io.BasicSocketUtils;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Christophe on 07/04/2017.
 */
public class SocketWrapper extends BasicSocketUtils {
    public SocketWrapper(Socket s) throws IOException {
        super(s);
    }

    @Override
    public void writeValue(boolean b) throws IOException {
        super.writeValue(b);
    }

    @Override
    public void writeValue(String s) throws IOException {
        super.writeValue(s);
    }

    @Override
    public String readValue() throws IOException {
        return super.readValue();
    }

    @Override
    public boolean readTest() throws IOException {
        return super.readTest();
    }
}
