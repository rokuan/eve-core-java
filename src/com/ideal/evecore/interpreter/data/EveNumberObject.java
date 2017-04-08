package com.ideal.evecore.interpreter.data;

/**
 * Created by Christophe on 06/04/2017.
 */
public final class EveNumberObject implements EveObject {
    private Number value;

    public EveNumberObject(Number n){
        value = n;
    }

    public Number getValue(){
        return value;
    }
}
