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
import com.ideal.evecore.io.command.query.FindItemByIdCommand;
import com.ideal.evecore.io.command.receiver.*;
import com.ideal.evecore.io.command.user.ReceiverRequestCommand;
import com.ideal.evecore.io.command.user.UserCommand;
import com.ideal.evecore.io.serialization.EveObjectSerialization;
import com.ideal.evecore.io.serialization.EveStructuredObjectSerialization;
import com.ideal.evecore.io.serialization.ValueMatcherSerialization;
import com.ideal.evecore.universe.matcher.UndefinedValueMatcher;
import com.ideal.evecore.universe.matcher.ValueMatcher;
import com.ideal.evecore.universe.receiver.EveObjectMessage;
import com.ideal.evecore.universe.receiver.Receiver;
import com.ideal.evecore.util.Conversions;
import com.ideal.evecore.util.Option;
import com.ideal.evecore.util.Pair;
import com.ideal.evecore.util.Result;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by Christophe on 22/04/2017.
 */
public class RemoteReceiver implements Receiver, QuerySource {
    public static final String UNKNOWN_RECEIVER_NAME = "_UNKNOWN_RECEIVER";

    protected final String receiverId;
    protected final StreamHandler handler;
    protected final ObjectMapper mapper;

    public RemoteReceiver(String id, StreamHandler h) {
        receiverId = id;
        handler = h;
        mapper = new ObjectMapper();
        // TODO: fill the mapper
        SimpleModule eveObjectModule = new SimpleModule();
        eveObjectModule.addDeserializer(EveStructuredObject.class, new EveStructuredObjectSerialization.EveStructuredObjectDeserializer(handler));
        eveObjectModule.addDeserializer(EveObject.class, new EveObjectSerialization.EveObjectDeserializer(handler));
        eveObjectModule.addSerializer(EveStructuredObject.class, new EveStructuredObjectSerialization.EveStructuredObjectSerializer(receiverId));
        SimpleModule matcherModule = new SimpleModule();
        matcherModule.addDeserializer(ValueMatcher.class, new ValueMatcherSerialization.ValueMatcherDeserializer());
        mapper.registerModule(eveObjectModule);
        mapper.registerModule(matcherModule);
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

    @Override
    public void initReceiver() {
        try {
            handler.commandOperation(getUserCommand(InitReceiverCommand.INIT_RECEIVER_COMMAND), mapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroyReceiver() {
        try {
            handler.commandOperation(getUserCommand(DestroyReceiverCommand.DESTROY_RECEIVER_COMMAND), mapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getReceiverName() {
        try {
            return handler.stringOperation(getUserCommand(GetReceiverNameCommand.GET_RECEIVER_NAME_COMMAND), mapper);
        } catch (IOException e) {
            return UNKNOWN_RECEIVER_NAME;
        } catch (InterruptedException e) {
            return UNKNOWN_RECEIVER_NAME;
        }
    }

    @Override
    public Mapping<ValueMatcher> getMappings() {
        try {
            return handler.objectOperation(getUserCommand(GetMappingsCommand.GET_MAPPINGS_COMMAND), mapper, new TypeReference<Mapping<ValueMatcher>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new Mapping<ValueMatcher>(new Pair<String, ValueMatcher>("*", UndefinedValueMatcher.UNDEFINED_VALUE_MATCHER));
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new Mapping<ValueMatcher>(new Pair<String, ValueMatcher>("*", UndefinedValueMatcher.UNDEFINED_VALUE_MATCHER));
        }
    }

    @Override
    public Result<EveObject> handleMessage(EveObjectMessage message) {
        try {
            return handler.resultOperation(getUserCommand(new HandleMessageCommand(message)), mapper, EveObject.class);
        } catch (IOException e) {
            return Result.ko(e);
        } catch (InterruptedException e) {
            return Result.ko(e);
        }
    }

    protected final UserCommand getUserCommand(ReceiverCommand command) {
        return new ReceiverRequestCommand(receiverId, command);
    }
}
