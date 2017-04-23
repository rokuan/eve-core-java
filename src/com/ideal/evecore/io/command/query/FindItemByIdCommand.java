package com.ideal.evecore.io.command.query;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class FindItemByIdCommand extends AbstractCommand implements QueryCommand {
    private final String id;

    @JsonCreator
    public FindItemByIdCommand(@JsonProperty("id") String i) {
        super(FIND_ITEM_BY_ID);
        id = i;
    }

    public String getId() {
        return id;
    }
}
