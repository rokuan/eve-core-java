package com.ideal.evecore.io.command.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ideal.evecore.io.command.AbstractCommand;
import com.ideal.evecore.io.command.query.ObjectCommand;
import com.ideal.evecore.io.command.structured.EveStructuredObjectCommand;

/**
 * Created by Christophe on 08/04/2017.
 */
public final class ObjectRequestCommand extends AbstractCommand implements UserCommand {
    private final String domainId;
    private final String objectId;
    private final EveStructuredObjectCommand objectCommand;

    @JsonCreator
    public ObjectRequestCommand(@JsonProperty("domainId") String dId, @JsonProperty("objectId") String oId, @JsonProperty("objectCommand") EveStructuredObjectCommand command) {
        super(CALL_OBJECT_METHOD);
        domainId = dId;
        objectId = oId;
        objectCommand = command;
    }

    public String getDomainId() {
        return domainId;
    }

    public String getObjectId() {
        return objectId;
    }

    public EveStructuredObjectCommand getObjectCommand() {
        return objectCommand;
    }
}
