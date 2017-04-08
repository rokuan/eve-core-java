package com.ideal.evecore.interpreter.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideal.evecore.common.Mapping;
import com.ideal.evecore.interpreter.QuerySource;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.io.StreamHandler;
import com.ideal.evecore.io.StreamSource;
import com.ideal.evecore.io.command.receiver.*;
import com.ideal.evecore.io.command.receiver.ReceiverCommand.*;
import com.ideal.evecore.universe.ValueMatcher;
import com.ideal.evecore.universe.receiver.EveObjectMessage;
import com.ideal.evecore.universe.receiver.Receiver;
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
    }

    public final void handleCommand(ReceiverCommand command, StreamSource source) throws IOException {
        if (command instanceof GetMappingsCommand) {
            Mapping<ValueMatcher> mappings = getMappings();
            source.writeResponse(mapper, mappings);
        } else if (command instanceof HandleMessageCommand) {
            HandleMessageCommand c = (HandleMessageCommand) command;
            Result<EveObject> result = handleMessage(c.getMessage());
            source.writeResponse(mapper, result);
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
