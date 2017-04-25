package com.ideal.evecore.interpreter.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ideal.evecore.interpreter.Context;
import com.ideal.evecore.interpreter.QuerySource;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.data.EveObjectList;
import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.io.StreamHandler;
import com.ideal.evecore.io.StreamSource;
import com.ideal.evecore.io.command.context.ContextCommand;
import com.ideal.evecore.io.command.context.FindItemsOfTypeCommand;
import com.ideal.evecore.io.command.context.FindOneItemOfTypeCommand;
import com.ideal.evecore.io.command.query.ObjectCommand;
import com.ideal.evecore.io.serialization.EveObjectListSerialization;
import com.ideal.evecore.io.serialization.EveObjectSerialization;
import com.ideal.evecore.io.serialization.EveStructuredObjectSerialization;
import com.ideal.evecore.util.Conversions;
import com.ideal.evecore.util.Option;
import com.ideal.evecore.util.Result;

import java.io.IOException;

/**
 * Created by Christophe on 07/04/2017.
 */
public class StreamContext extends ObjectStreamSource implements Context {
    protected final String contextId;
    protected final Context context;
    protected final ObjectMapper mapper;

    public StreamContext(String id, StreamHandler h, Context c) {
        super(h);
        contextId = id;
        context = c;
        mapper = new ObjectMapper(); // TODO: build the mapper
        SimpleModule basicModule = new SimpleModule();
        basicModule.addSerializer(EveObject.class, new EveObjectSerialization.EveObjectSerializer(contextId));
        basicModule.addSerializer(EveObjectList.class, new EveObjectListSerialization.EveObjectListSerializer(contextId));
        basicModule.addSerializer(EveStructuredObject.class, new EveStructuredObjectSerialization.EveStructuredObjectSerializer(contextId));
        mapper.registerModule(basicModule);
    }

    /**
     * Calls the underlying context's method
     * @param command
     * @param source
     * @throws IOException
     */
    public final void handleCommand(ContextCommand command, StreamSource source) throws IOException {
        if (command instanceof FindItemsOfTypeCommand) {
            FindItemsOfTypeCommand query = (FindItemsOfTypeCommand) command;
            Option<EveObjectList> items = findItemsOfType(query.getItemType());
            Result<EveObjectList> result = Conversions.toResult(items);
            source.writeResultResponse(mapper, result);
        } else if (command instanceof FindOneItemOfTypeCommand) {
            FindOneItemOfTypeCommand query = (FindOneItemOfTypeCommand) command;
            Option<EveStructuredObject> item = findOneItemOfType(query.getItemType());
            Result<EveStructuredObject> result = Conversions.toResult(item);
            source.writeResultResponse(mapper, result);
        } else if (command instanceof ObjectCommand) {
            handleObjectCommand((ObjectCommand)command, source);
        }
    }

    @Override
    public Option<EveStructuredObject> findById(String id) {
        if (context instanceof QuerySource) {
            return ((QuerySource) context).findById(id);
        }
        return Option.empty();
    }

    @Override
    public Option<EveStructuredObject> findOneItemOfType(String type) {
        return context.findOneItemOfType(type);
    }

    @Override
    public Option<EveObjectList> findItemsOfType(String type) {
        return context.findItemsOfType(type);
    }

    @Override
    protected ObjectMapper getMapper() {
        return mapper;
    }
}
