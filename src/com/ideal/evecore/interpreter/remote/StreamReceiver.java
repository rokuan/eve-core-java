package com.ideal.evecore.interpreter.remote;

import com.ideal.evecore.interpreter.QuerySource;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.io.StreamHandler;
import com.ideal.evecore.universe.receiver.EveObjectMessage;
import com.ideal.evecore.universe.receiver.Receiver;
import com.ideal.evecore.util.Option;
import com.ideal.evecore.util.Result;

/**
 * Created by chris on 07/04/2017.
 */
public class StreamReceiver extends ObjectStreamSource implements Receiver {
    protected final String receiverId;
    protected final Receiver receiver;

    public StreamReceiver(String id, StreamHandler h, Receiver r) {
        super(h);
        receiverId = id;
        receiver = r;
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
    public Result<EveObject> handleMessage(EveObjectMessage message) {
        return receiver.handleMessage(message);
    }
}
