package com.ideal.evecore.io.command.query;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ideal.evecore.io.command.AbstractCommand;
import com.ideal.evecore.io.command.structured.EveStructuredObjectCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class ObjectCommand extends AbstractCommand implements QueryCommand {
    private final String objectId;
    private final EveStructuredObjectCommand objectCommand;

    @JsonCreator
    public ObjectCommand(@JsonProperty("objectId") String id, @JsonProperty("objectCommand") EveStructuredObjectCommand cmd) {
        super(OBJECT_REQUEST);
        objectId = id;
        objectCommand = cmd;
    }

    public String getObjectId() {
        return objectId;
    }

    public EveStructuredObjectCommand getObjectCommand() {
        return objectCommand;
    }
}
