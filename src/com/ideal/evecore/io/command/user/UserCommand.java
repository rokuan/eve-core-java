package com.ideal.evecore.io.command.user;

import com.ideal.evecore.io.command.AbstractCommand;
import com.ideal.evecore.io.command.context.ContextCommand;
import com.ideal.evecore.io.command.receiver.ReceiverCommand;

/**
 * Created by Christophe on 06/04/2017.
 */
public interface UserCommand {
    public static final String EVALUATE = "EVAL";
    public static final String REGISTER_RECEIVER = "RRCV";
    public static final String UNREGISTER_RECEIVER = "URCV";
    public static final String REGISTER_CONTEXT = "RCTX";
    public static final String UNREGISRER_CONTEXT = "UCTX";
    public static final String CALL_RECEIVER_METHOD = "CRMT";
    public static final String CALL_CONTEXT_METHOD = "CCMT";
    public static final String CALL_OBJECT_METHOD = "COMT";
}
