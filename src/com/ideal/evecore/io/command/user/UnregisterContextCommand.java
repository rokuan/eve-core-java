package com.ideal.evecore.io.command.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class UnregisterContextCommand extends AbstractCommand implements UserCommand {
    private final String contextId;

    @JsonCreator
    public UnregisterContextCommand(@JsonProperty("contextId") String id) {
        super(UNREGISRER_CONTEXT);
        contextId = id;
    }

    public String getContextId() {
        return contextId;
    }
}
