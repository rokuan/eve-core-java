package com.ideal.evecore.interpreter.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.io.StreamHandler;
import com.ideal.evecore.io.command.structured.*;
import com.ideal.evecore.io.command.user.ObjectRequestCommand;
import com.ideal.evecore.io.command.user.UserCommand;
import com.ideal.evecore.io.serialization.EveObjectSerialization;
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

    public RemoteEveStructuredObject(String dId, String oId, StreamHandler h) {
        domainId = dId;
        objectId = oId;
        handler = h;
        // TODO: fill the mapper
        mapper = new ObjectMapper();
        SimpleModule basicModule = new SimpleModule();
        //basicModule.addSerializer(EveObject.class, new EveObjectSerialization.EveObjectSerializer(domainId));
        basicModule.addDeserializer(EveObject.class, new EveObjectSerialization.EveObjectDeserializer(h));
        mapper.registerModule(basicModule);
    }

    public String getDomainId() {
        return domainId;
    }

    public String getObjectId() {
        return objectId;
    }

    protected UserCommand getCommand(EveStructuredObjectCommand command) {
        return new ObjectRequestCommand(domainId, objectId, command);
    }

    @Override
    public String getType() {
        try {
            UserCommand command = getCommand(GetTypeCommand.GET_TYPE_COMMAND);
            return handler.stringOperation(command, mapper);
        } catch (IOException e) {
            return "";
        } catch (InterruptedException e) {
            return "";
        }
    }

    @Override
    public boolean has(String field) {
        try {
            UserCommand command = getCommand(new HasFieldCommand(field));
            return handler.booleanOperation(command, mapper);
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public boolean hasState(String state) {
        try {
            UserCommand command = getCommand(new HasStateCommand(state));
            return handler.booleanOperation(command, mapper);
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public Option<EveObject> get(String field) {
        try {
            UserCommand command = getCommand(new GetFieldCommand(field));
            Result<EveObject> result = handler.resultOperation(command, mapper, EveObject.class);
            return Conversions.toOption(result);
        } catch (IOException e) {
            return Option.empty();
        } catch (InterruptedException e) {
            return Option.empty();
        }
    }

    @Override
    public Option<String> getState(String state) {
        try {
            UserCommand command = getCommand(new GetStateCommand(state));
            Result<String> result = handler.resultOperation(command, mapper, String.class);
            return Conversions.toOption(result);
        } catch (IOException e) {
            return Option.empty();
        } catch (InterruptedException e) {
            return Option.empty();
        }
    }

    @Override
    public boolean set(String field, EveObject value) {
        try {
            UserCommand command = getCommand(new SetFieldCommand(field, value));
            return handler.booleanOperation(command, mapper);
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public boolean setState(String field, String value) {
        try {
            UserCommand command = getCommand(new SetStateCommand(field, value));
            return handler.booleanOperation(command, mapper);
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        }
    }
}
