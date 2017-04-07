package com.ideal.evecore.interpreter.remote;

import com.ideal.evecore.interpreter.QuerySource;
import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.io.StreamHandler;
import com.ideal.evecore.io.StreamSource;
import com.ideal.evecore.io.command.QueryCommand;
import com.ideal.evecore.util.Handler;
import com.ideal.evecore.util.Transformer;

/**
 * Created by chris on 07/04/2017.
 */
public abstract class ObjectStreamSource implements QuerySource {
    protected final StreamHandler handler;

    protected ObjectStreamSource(StreamHandler h) {
        handler = h;
    }

    public final void handleObjectCommand(QueryCommand.ObjectCommand command, StreamSource source) {
        findById(command.getObjectId()).foreach(new Handler<EveStructuredObject>() {
            @Override
            public void apply(EveStructuredObject eveStructuredObject) {
                // TODO:
            }
        });
    }
}
