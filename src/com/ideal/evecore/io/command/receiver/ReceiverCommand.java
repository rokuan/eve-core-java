package com.ideal.evecore.io.command.receiver;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.ideal.evecore.io.command.query.FindItemByIdCommand;
import com.ideal.evecore.io.command.query.ObjectCommand;

import static com.ideal.evecore.io.command.query.QueryCommand.*;

/**
 * Created by Christophe on 06/04/2017.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "command")
@JsonSubTypes({
        @Type(name = INIT_RECEIVER, value = InitReceiverCommand.class),
        @Type(name = DESTROY_RECEIVER, value = DestroyReceiverCommand.class),
        @Type(name = HANDLE_MESSAGE, value = HandleMessageCommand.class),
        @Type(name = GET_MAPPINGS, value = GetMappingsCommand.class),
        @Type(name = GET_RECEIVER_NAME, value = GetReceiverNameCommand.class),
        @Type(name = FIND_ITEM_BY_ID, value = FindItemByIdCommand.class),
        @Type(name = OBJECT_REQUEST, value = ObjectCommand.class)
})
public interface ReceiverCommand {
    public static final String INIT_RECEIVER = "IRCV";
    public static final String DESTROY_RECEIVER = "DRCV";
    public static final String HANDLE_MESSAGE = "HMSG";
    public static final String GET_MAPPINGS = "GMAP";
    public static final String GET_RECEIVER_NAME = "GRNM";
}
