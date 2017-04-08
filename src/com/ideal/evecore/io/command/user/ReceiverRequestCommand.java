package com.ideal.evecore.io.command.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ideal.evecore.io.command.AbstractCommand;
import com.ideal.evecore.io.command.receiver.ReceiverCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class ReceiverRequestCommand extends AbstractCommand implements UserCommand {
    private final String receiverId;
    private final ReceiverCommand receiverCommand;

    public ReceiverRequestCommand(@JsonProperty("receiverId") String id, @JsonProperty("receiverCommand") ReceiverCommand command) {
        super(CALL_RECEIVER_METHOD);
        receiverId = id;
        receiverCommand = command;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public ReceiverCommand getReceiverCommand() {
        return receiverCommand;
    }
}
