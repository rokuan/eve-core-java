package com.ideal.evecore.interpreter.data;

/**
 * Created by Christophe on 21/04/2017.
 */
public class EveNoneObject implements EveObject {
    private EveNoneObject() {

    }

    public static final EveNoneObject NONE = new EveNoneObject();
}
