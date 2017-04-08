package com.ideal.evecore.interpreter.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.io.StreamHandler;
import com.ideal.evecore.io.command.structured.*;
import com.ideal.evecore.io.command.user.ObjectRequestCommand;
import com.ideal.evecore.io.command.user.UserCommand;
import com.ideal.evecore.util.Conversions;
import com.ideal.evecore.util.Option;
import com.ideal.evecore.util.Result;

import java.io.IOException;

/**
 * Created by Christophe on 08/04/2017.
 */
public class RemoteEveStructuredObject implements EveStructuredObject {
    private final String domainId;
    private final String objectId;
    private final StreamHandler handler;
    private final ObjectMapper mapper;

    public RemoteEveStructuredObject(String dId, String oId, StreamHandler h, ObjectMapper m) {
        domainId = dId;
        objectId = oId;
        handler = h;
        mapper = m;
    }

    protected UserCommand getCommand(EveStructuredObjectCommand command) {
        return new ObjectRequestCommand(domainId, objectId, command);
    }

    @Override
    public String getType() {
        UserCommand command = getCommand(new GetTypeCommand());
        try {
            return handler.stringOperation(command, mapper);
        } catch (IOException e) {
            return "";
        }
    }

    @Override
    public boolean has(String field) {
        UserCommand command = getCommand(new HasFieldCommand(field));
        try {
            return handler.booleanOperation(command, mapper);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean hasState(String state) {
        UserCommand command = getCommand(new HasStateCommand(state));
        try {
            return handler.booleanOperation(command, mapper);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Option<EveObject> get(String field) {
        UserCommand command = getCommand(new GetFieldCommand(field));
        try {
            Result<EveObject> result = handler.resultOperation(command, mapper, EveObject.class);
            return Conversions.toOption(result);
        } catch (IOException e) {
            return Option.empty();
        }
    }

    @Override
    public Option<String> getState(String state) {
        UserCommand command = getCommand(new GetStateCommand(state));
        try {
            Result<String> result = handler.resultOperation(command, mapper, String.class);
            return Conversions.toOption(result);
        } catch (IOException e) {
            return Option.empty();
        }
    }

    @Override
    public void set(String field, EveObject value) {
        UserCommand command = getCommand(new SetFieldCommand(field, value));
        try {
            handler.commandOperation(command, mapper);
        } catch (IOException e) {

        }
    }

    @Override
    public void setState(String field, String value) {
        UserCommand command = getCommand(new SetStateCommand(field, value));
        try {
            handler.commandOperation(command, mapper);
        } catch (IOException e) {

        }
    }
}
