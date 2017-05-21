package com.ideal.evecore.interpreter.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ideal.evecore.interpreter.Context;
import com.ideal.evecore.interpreter.QuerySource;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.data.EveObjectList;
import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.io.StreamHandler;
import com.ideal.evecore.io.command.context.ContextCommand;
import com.ideal.evecore.io.command.context.FindItemsOfTypeCommand;
import com.ideal.evecore.io.command.context.FindOneItemOfTypeCommand;
import com.ideal.evecore.io.command.query.FindItemByIdCommand;
import com.ideal.evecore.io.command.user.ContextRequestCommand;
import com.ideal.evecore.io.command.user.UserCommand;
import com.ideal.evecore.io.serialization.EveObjectListSerialization;
import com.ideal.evecore.io.serialization.EveObjectSerialization;
import com.ideal.evecore.io.serialization.EveStructuredObjectSerialization;
import com.ideal.evecore.util.Conversions;
import com.ideal.evecore.util.Option;
import com.ideal.evecore.util.Result;

import java.io.IOException;

/**
 * Created by Christophe on 22/04/2017.
 */
public class RemoteContext implements Context, QuerySource {
    protected final String contextId;
    protected final StreamHandler handler;
    protected final ObjectMapper mapper;

    public RemoteContext(String id, StreamHandler h) {
        contextId = id;
        handler = h;
        mapper = new ObjectMapper();
        // TODO: fill the mapper
        SimpleModule module = new SimpleModule();
        module.addDeserializer(EveStructuredObject.class, new EveStructuredObjectSerialization.EveStructuredObjectDeserializer(handler));
        module.addDeserializer(EveObject.class, new EveObjectSerialization.EveObjectDeserializer(handler));
        module.addDeserializer(EveObjectList.class, new EveObjectListSerialization.EveObjectListDeserializer(handler));
        mapper.registerModule(module);
    }

    @Override
    public Option<EveStructuredObject> findOneItemOfType(String type) {
        try {
            Result<EveStructuredObject> result = handler.resultOperation(getUserCommand(new FindOneItemOfTypeCommand(type)), mapper, EveStructuredObject.class);
            return Conversions.toOption(result);
        } catch (IOException e) {
            e.printStackTrace();
            return Option.empty();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Option.empty();
        }
    }

    @Override
    public Option<EveObjectList> findItemsOfType(String type) {
        try {
            Result<EveObjectList> result = handler.resultOperation(getUserCommand(new FindItemsOfTypeCommand(type)), mapper, EveObjectList.class);
            return Conversions.toOption(result);
        } catch (IOException e) {
            e.printStackTrace();
            return Option.empty();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Option.empty();
        }
    }

    @Override
    public Option<EveStructuredObject> findById(String id) {
        try {
            Result<EveStructuredObject> result = handler.resultOperation(getUserCommand(new FindItemByIdCommand(id)), mapper, EveStructuredObject.class);
            return Conversions.toOption(result);
        } catch (IOException e) {
            e.printStackTrace();
            return Option.empty();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Option.empty();
        }
    }

    protected UserCommand getUserCommand(ContextCommand command) {
        return new ContextRequestCommand(contextId, command);
    }
}
