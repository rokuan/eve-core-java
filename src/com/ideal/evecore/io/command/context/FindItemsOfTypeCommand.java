package com.ideal.evecore.io.command.context;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class FindItemsOfTypeCommand extends AbstractCommand implements ContextCommand {
    private String itemType;

    @JsonCreator
    public FindItemsOfTypeCommand(@JsonProperty("itemType") String t) {
        super(FIND_ITEMS_OF_TYPE);
        itemType = t;
    }

    public String getItemType() {
        return itemType;
    }
}
