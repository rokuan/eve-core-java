package com.ideal.evecore.interpreter.data;

/**
 * Created by Christophe on 06/04/2017.
 */
public final class EveBooleanObject implements EveObject {
    private boolean value;

    public EveBooleanObject(boolean b){
        value = b;
    }

    public boolean getValue(){
        return value;
    }
}
