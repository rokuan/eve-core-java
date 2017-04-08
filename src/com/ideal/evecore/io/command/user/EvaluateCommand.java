package com.ideal.evecore.io.command.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class EvaluateCommand extends AbstractCommand implements UserCommand {
    private final String text;

    @JsonCreator
    public EvaluateCommand(@JsonProperty("text") String t) {
        super(EVALUATE);
        text = t;
    }

    public String getText() {
        return text;
    }
}
