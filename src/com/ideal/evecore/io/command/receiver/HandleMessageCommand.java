package com.ideal.evecore.io.command.receiver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ideal.evecore.io.command.AbstractCommand;
import com.ideal.evecore.universe.receiver.EveObjectMessage;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class HandleMessageCommand extends AbstractCommand implements ReceiverCommand {
    private final EveObjectMessage message;

    @JsonCreator
    public HandleMessageCommand(@JsonProperty("message") EveObjectMessage m) {
        super(HANDLE_MESSAGE);
        message = m;
    }

    public EveObjectMessage getMessage() {
        return message;
    }
}
