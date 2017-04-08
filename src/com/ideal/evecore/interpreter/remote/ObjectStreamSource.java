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

    public final void handleObjectCommand(final ObjectCommand command, final StreamSource source) throws IOException {
        findById(command.getObjectId()).foreach(new Handler<EveStructuredObject>() {
            @Override
            public void apply(EveStructuredObject eveStructuredObject) {
                EveStructuredObjectCommand objectCommand = command.getObjectCommand();
                ObjectMapper mapper = getMapper();
                try {
                    if (objectCommand instanceof GetTypeCommand) {
                        String type = eveStructuredObject.getType();
                        source.writeStringResponse(type);
                    } else if (objectCommand instanceof GetFieldCommand) {
                        String field = ((GetFieldCommand) objectCommand).getField();
                        Option<EveObject> value = eveStructuredObject.get(field);
                        source.writeResultResponse(mapper, Conversions.toResult(value));
                    } else if (objectCommand instanceof SetFieldCommand) {
                        SetFieldCommand cmd = (SetFieldCommand)objectCommand;
                        eveStructuredObject.set(cmd.getField(), cmd.getValue());
                    } else if (objectCommand instanceof HasFieldCommand) {
                        String field = ((HasFieldCommand) objectCommand).getField();
                        source.writeBooleanResponse(eveStructuredObject.has(field));
                    } else if (objectCommand instanceof GetStateCommand) {
                        String field = ((GetStateCommand) objectCommand).getField();
                        Option<String> value = eveStructuredObject.getState(field);
                        source.writeResultResponse(mapper, Conversions.toResult(value));
                    } else if (objectCommand instanceof SetStateCommand) {
                        SetStateCommand cmd = (SetStateCommand) objectCommand;
                        eveStructuredObject.setState(cmd.getField(), cmd.getValue());
                    } else if (objectCommand instanceof HasStateCommand) {
                        String field = ((HasStateCommand) objectCommand).getField();
                        source.writeBooleanResponse(eveStructuredObject.hasState(field));
                    }
                } catch (IOException e) {

                }
            }
        });
    }
}
