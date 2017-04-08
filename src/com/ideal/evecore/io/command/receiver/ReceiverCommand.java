package com.ideal.evecore.io.command.receiver;

import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 06/04/2017.
 */
public interface ReceiverCommand {
    public static final String INIT_RECEIVER = "IRCV";
    public static final String DESTROY_RECEIVER = "DRCV";
    public static final String HANDLE_MESSAGE = "HMSG";
    public static final String GET_MAPPINGS = "GMAP";
    public static final String GET_RECEIVER_NAME = "GRNM";
}
