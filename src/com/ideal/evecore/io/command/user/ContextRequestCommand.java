package com.ideal.evecore.io.command.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ideal.evecore.io.command.AbstractCommand;
import com.ideal.evecore.io.command.context.ContextCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class ContextRequestCommand extends AbstractCommand implements UserCommand {
    private final String contextId;
    private final ContextCommand contextCommand;

    @JsonCreator
    public ContextRequestCommand(@JsonProperty("contextId") String id, @JsonProperty("contextCommand") ContextCommand command) {
        super(CALL_CONTEXT_METHOD);
        contextId = id;
        contextCommand = command;
    }

    public String getContextId() {
        return contextId;
    }

    public ContextCommand getContextCommand() {
        return contextCommand;
    }
}