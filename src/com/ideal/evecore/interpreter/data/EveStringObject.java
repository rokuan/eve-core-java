package com.ideal.evecore.interpreter.data;

/**
 * Created by Christophe on 06/04/2017.
 */
public final class EveStringObject implements EveObject {
    private String value;

    public EveStringObject(String s){
        value = s;
    }

    public String getValue(){
        return value;
    }
}
