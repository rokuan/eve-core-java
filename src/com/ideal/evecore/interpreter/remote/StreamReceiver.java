package com.ideal.evecore.interpreter.remote;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.ideal.evecore.common.Mapping;
import com.ideal.evecore.interpreter.QuerySource;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.io.StreamHandler;
import com.ideal.evecore.io.StreamSource;
import com.ideal.evecore.io.command.query.FindItemByIdCommand;
import com.ideal.evecore.io.command.receiver.*;
import com.ideal.evecore.io.serialization.EveObjectSerialization;
import com.ideal.evecore.io.serialization.EveStructuredObjectSerialization;
import com.ideal.evecore.io.serialization.ValueMatcherSerialization;
import com.ideal.evecore.universe.matcher.ValueMatcher;
import com.ideal.evecore.universe.receiver.EveObjectMessage;
import com.ideal.evecore.universe.receiver.Receiver;
import com.ideal.evecore.util.Conversions;
import com.ideal.evecore.util.Option;
import com.ideal.evecore.util.Result;

import java.io.IOException;

/**
 * Created by Christophe on 07/04/2017.
 */
public class StreamReceiver extends ObjectStreamSource implements Receiver {
    protected final String receiverId;
    protected final Receiver receiver;
    protected final ObjectMapper mapper;

    public StreamReceiver(String id, StreamHandler h, Receiver r) {
        super(h);
        receiverId = id;
        receiver = r;
        // TODO: fill the mapper
        mapper = new ObjectMapper();
        SimpleModule basicModule = new SimpleModule();
        basicModule.addSerializer(EveObject.class, new EveObjectSerialization.EveObjectSerializer(receiverId));
        basicModule.addSerializer(ValueMatcher.class, new ValueMatcherSerialization.ValueMatcherSerializer());
        mapper.registerModule(basicModule);
    }

    public synchronized final void handleCommand(ReceiverCommand command, StreamSource source) throws IOException {
        if (command instanceof GetMappingsCommand) {
            Mapping<ValueMatcher> mappings = getMappings();
            source.writeResponse(mapper, mappings, new TypeReference<Mapping<ValueMatcher>>() {});
        } else if (command instanceof HandleMessageCommand) {
            HandleMessageCommand c = (HandleMessageCommand) command;
            Result<EveObject> result = handleMessage(c.getMessage());
            source.writeResultResponse(mapper, result);
        } else if (command instanceof FindItemByIdCommand) {
            FindItemByIdCommand c = (FindItemByIdCommand) command;
            Option<EveStructuredObject> result = findById(c.getId());
            source.writeResultResponse(mapper, Conversions.toResult(result));
        } else if (command instanceof GetReceiverNameCommand) {
            String name = getReceiverName();
            source.writeStringResponse(name);
        } else if (command instanceof InitReceiverCommand) {
            initReceiver();
        } else if (command instanceof DestroyReceiverCommand) {
            destroyReceiver();
        }
    }

    @Override
    public Option<EveStructuredObject> findById(String id) {
        if (receiver instanceof QuerySource) {
            return ((QuerySource) receiver).findById(id);
        }
        return Option.empty();
    }

    @Override
    public void initReceiver() {
        receiver.initReceiver();
    }

    @Override
    public void destroyReceiver() {
        receiver.destroyReceiver();
    }

    @Override
    public String getReceiverName() {
        return receiver.getReceiverName();
    }

    @Override
    public Mapping<ValueMatcher> getMappings() {
        return receiver.getMappings();
    }

    @Override
    public Result<EveObject> handleMessage(EveObjectMessage message) {
        return receiver.handleMessage(message);
    }

    @Override
    protected ObjectMapper getMapper() {
        return mapper;
    }
}
