package com.ideal.evecore.io.command.structured;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import static com.ideal.evecore.io.command.structured.EveStructuredObjectCommand.*;

/**
 * Created by Christophe on 06/04/2017.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "command")
@JsonSubTypes({
        @Type(name = GET_TYPE, value = GetTypeCommand.class),
        @Type(name = GET_FIELD, value = GetFieldCommand.class),
        @Type(name = SET_FIELD, value = SetFieldCommand.class),
        @Type(name = HAS_FIELD, value = HasFieldCommand.class),
        @Type(name = GET_STATE, value = GetStateCommand.class),
        @Type(name = SET_STATE, value = SetStateCommand.class),
        @Type(name = HAS_STATE, value = HasStateCommand.class),
        @Type(name = CALL_ACTION, value = CallActionCommand.class)
})
public interface EveStructuredObjectCommand {
    public static final String GET_TYPE = "GTYP";
    public static final String GET_FIELD = "GFLD";
    public static final String SET_FIELD = "SFLD";
    public static final String HAS_FIELD = "HFLD";
    public static final String GET_STATE = "GSTE";
    public static final String SET_STATE = "SSTE";
    public static final String HAS_STATE = "HSTE";
    public static final String CALL_ACTION = "CACT";
}
