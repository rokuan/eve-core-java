package com.ideal.evecore.io.command.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class UnregisterReceiverCommand extends AbstractCommand implements UserCommand {
    private final String receiverId;

    @JsonCreator
    public UnregisterReceiverCommand(@JsonProperty("receiverId") String id) {
        super(UNREGISTER_RECEIVER);
        receiverId = id;
    }

    public String getReceiverId() {
        return receiverId;
    }
}
