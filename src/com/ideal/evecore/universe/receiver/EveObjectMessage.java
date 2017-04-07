package com.ideal.evecore.universe.receiver;

import com.ideal.evecore.interpreter.data.EveStructuredObject;

/**
 * Created by chris on 06/04/2017.
 */
public class EveObjectMessage {
    private EveStructuredObject content;

    public EveObjectMessage(EveStructuredObject o){
        content = o;
    }

    public EveStructuredObject getContent(){
        return content;
    }
}
