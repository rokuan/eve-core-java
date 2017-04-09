package com.ideal.evecore.io.command.user;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import static com.ideal.evecore.io.command.user.UserCommand.*;

/**
 * Created by Christophe on 06/04/2017.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "command")
@JsonSubTypes({
        @Type(name = EVALUATE, value = EvaluateCommand.class),
        @Type(name = REGISTER_RECEIVER, value = RegisterReceiverCommand.class),
        @Type(name = UNREGISTER_RECEIVER, value = UnregisterReceiverCommand.class),
        @Type(name = REGISTER_CONTEXT, value = RegisterContextCommand.class),
        @Type(name = UNREGISTER_CONTEXT, value = UnregisterContextCommand.class),
        @Type(name = CALL_RECEIVER_METHOD, value = ReceiverRequestCommand.class),
        @Type(name = CALL_CONTEXT_METHOD, value = ContextRequestCommand.class),
        @Type(name = CALL_OBJECT_METHOD, value = ObjectRequestCommand.class)
})
public interface UserCommand {
    public static final String EVALUATE = "EVAL";
    public static final String REGISTER_RECEIVER = "RRCV";
    public static final String UNREGISTER_RECEIVER = "URCV";
    public static final String REGISTER_CONTEXT = "RCTX";
    public static final String UNREGISTER_CONTEXT = "UCTX";
    public static final String CALL_RECEIVER_METHOD = "CRMT";
    public static final String CALL_CONTEXT_METHOD = "CCMT";
    public static final String CALL_OBJECT_METHOD = "COMT";
}
