package com.ideal.evecore.io.command.structured;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ideal.evecore.io.command.AbstractCommand;
import com.rokuan.calliopecore.sentence.IAction;

/**
 * Created by Christophe on 28/05/2017.
 */
public class CallActionCommand extends AbstractCommand implements EveStructuredObjectCommand {
    private final IAction action;

    @JsonCreator
    public CallActionCommand(@JsonProperty("action") IAction a) {
        super(CALL_ACTION);
        action = a;
    }

    public IAction getAction() {
        return action;
    }
}
