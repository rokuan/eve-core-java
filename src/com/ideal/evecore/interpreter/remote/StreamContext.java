package com.ideal.evecore.interpreter.remote;

import com.ideal.evecore.interpreter.Context;
import com.ideal.evecore.interpreter.QuerySource;
import com.ideal.evecore.interpreter.data.EveObjectList;
import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.io.StreamHandler;
import com.ideal.evecore.io.StreamSource;
import com.ideal.evecore.io.command.ContextCommand;
import com.ideal.evecore.io.command.QueryCommand;
import com.ideal.evecore.util.Option;

/**
 * Created by chris on 07/04/2017.
 */
public class StreamContext extends ObjectStreamSource implements Context {
    protected final String contextId;
    protected final Context context;

    public StreamContext(String id, StreamHandler h, Context c) {
        super(h);
        contextId = id;
        context = c;
    }

    public final void handleCommand(ContextCommand command, StreamSource source) {
        try {
            if (command instanceof ContextCommand.FindItemsOfTypeCommand) {
                // TODO:
            } else if (command instanceof ContextCommand.FindOneItemOfTypeCommand) {
                // TODO:
            } else if (command instanceof QueryCommand.ObjectCommand) {
                handleObjectCommand((QueryCommand.ObjectCommand)command, source);
            }
        } catch (Exception e) {

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
}
