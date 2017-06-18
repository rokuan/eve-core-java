package com.ideal.evecore.interpreter.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ideal.evecore.interpreter.QuerySource;
import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.interpreter.data.EveStructuredObject;
import com.ideal.evecore.io.StreamHandler;
import com.ideal.evecore.io.StreamSource;
import com.ideal.evecore.io.command.query.ObjectCommand;
import com.ideal.evecore.io.command.structured.*;
import com.ideal.evecore.util.Conversions;
import com.ideal.evecore.util.Handler;
import com.ideal.evecore.util.Option;
import com.rokuan.calliopecore.sentence.IAction;

import java.io.IOException;


/**
 * Created by Christophe on 07/04/2017.
 */
public abstract class ObjectStreamSource implements QuerySource {
    protected final StreamHandler handler;

    protected ObjectStreamSource(StreamHandler h) {
        handler = h;
    }

    protected abstract ObjectMapper getMapper();

    /**
     * Call the corresponding EveStructuredObject's method and writes the result back to the sender
     *
     * @param command
     * @param source
     * @throws IOException
     */
    public final void handleObjectCommand(final ObjectCommand command, final StreamSource source) throws IOException {
        Option<EveStructuredObject> result = findById(command.getObjectId());
        EveStructuredObjectCommand objectCommand = command.getObjectCommand();
        ObjectMapper mapper = getMapper();
        EveStructuredObject o = result.isDefined() ? result.get() : null;
        try {
            if (objectCommand instanceof GetTypeCommand) {
                String type = (o != null) ? o.getType() : "";
                source.writeStringResponse(type);
            } else if (objectCommand instanceof GetFieldCommand) {
                String field = ((GetFieldCommand) objectCommand).getField();
                Option<EveObject> value = (o != null) ? o.get(field) : Option.<EveObject>empty();
                source.writeResultResponse(mapper, Conversions.toResult(value));
            } else if (objectCommand instanceof SetFieldCommand) {
                SetFieldCommand cmd = (SetFieldCommand) objectCommand;
                if (o != null) {
                    o.set(cmd.getField(), cmd.getValue());
                }
            } else if (objectCommand instanceof HasFieldCommand) {
                String field = ((HasFieldCommand) objectCommand).getField();
                boolean has = (o != null) ? o.has(field) : false;
                source.writeBooleanResponse(has);
            } else if (objectCommand instanceof GetStateCommand) {
                String field = ((GetStateCommand) objectCommand).getField();
                Option<String> value = (o != null) ? o.getState(field) : Option.<String>empty();
                source.writeResultResponse(mapper, Conversions.toResult(value));
            } else if (objectCommand instanceof SetStateCommand) {
                SetStateCommand cmd = (SetStateCommand) objectCommand;
                if (o != null) {
                    o.setState(cmd.getField(), cmd.getValue());
                }
            } else if (objectCommand instanceof HasStateCommand) {
                String field = ((HasStateCommand) objectCommand).getField();
                boolean has = (o != null) ? o.hasState(field) : false;
                source.writeBooleanResponse(has);
            } else if (objectCommand instanceof CallActionCommand) {
                IAction action = ((CallActionCommand) objectCommand).getAction();
                boolean returnedValue = (o != null) ? o.call(action) : false;
                source.writeBooleanResponse(returnedValue);
            }
        } catch (IOException e) {

        }
    }
}
