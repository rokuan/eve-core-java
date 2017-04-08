package com.ideal.evecore.io.command.structured;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.io.command.AbstractCommand;

/**
 * Created by Christophe on 06/04/2017.
 */
public interface EveStructuredObjectCommand {
    public static final String GET_TYPE = "GTYP";
    public static final String SET_FIELD = "SFLD";
    public static final String GET_FIELD = "GFLD";
    public static final String HAS_FIELD = "HFLD";
    public static final String GET_STATE = "GSTE";
    public static final String SET_STATE = "SSTE";
    public static final String HAS_STATE = "HSTE";
}
