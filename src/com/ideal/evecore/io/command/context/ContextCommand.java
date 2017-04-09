package com.ideal.evecore.io.command.context;


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
        @Type(name = FIND_ITEMS_OF_TYPE, value = FindItemsOfTypeCommand.class),
        @Type(name = FIND_ONE_ITEM_OF_TYPE, value = FindOneItemOfTypeCommand.class),
        @Type(name = FIND_ITEM_BY_ID, value = FindItemByIdCommand.class),
        @Type(name = OBJECT_REQUEST, value = ObjectCommand.class)
})
public interface ContextCommand {
    public static final String FIND_ITEMS_OF_TYPE = "FTYP";
    public static final String FIND_ONE_ITEM_OF_TYPE = "FOTY";
}
