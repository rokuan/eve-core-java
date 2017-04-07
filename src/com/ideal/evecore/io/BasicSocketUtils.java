package com.ideal.evecore.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by chris on 07/04/2017.
 */
public abstract class BasicSocketUtils {
    protected final Socket socket;
    protected final InputStream is;
    protected final OutputStream os;

    protected BasicSocketUtils(Socket s) throws IOException {
        socket = s;
        is = s.getInputStream();
        os = s.getOutputStream();
    }

    private final void writeSize(int size) throws IOException {
        byte[] sizeData = new byte[4];
        for(int i=0; i<sizeData.length; i++){
            sizeData[i] = (byte)((size >> (i * 8)) & 0xFF);
        }
        os.write(sizeData);
        os.flush();
    }

    protected void writeValue(boolean b) throws IOException {
        os.write(b ? 1 : 0);
        os.flush();
    }

    protected void writeValue(String s) throws IOException {
        writeSize(s.length());
        os.write(s.getBytes());
        os.flush();
    }

    private final int readSize() throws IOException {
        byte[] data = new byte[4];
        if (is.read(data) == 4) {
            int sum = 0;
            for(int i=0; i<data.length; i++){
                sum += ((data[i] & 0xFF) << (i * 8));
            }
            return sum;
        }
        return 0;
    }

    protected String readValue() throws IOException {
        int length = readSize();
        byte[] block = new byte[1024];
        StringBuilder buffer = new StringBuilder();

        while (length > 0) {
            int read = is.read(block, 0, Math.min(length, block.length));

            if (read > 0) {
                buffer.append(new String(block, 0, read));
                length -= read;
            } else {
                length = 0;
            }
        }

        return buffer.toString();
    }

    protected boolean readTest() throws IOException {
        return is.read() != 0;
    }
}
