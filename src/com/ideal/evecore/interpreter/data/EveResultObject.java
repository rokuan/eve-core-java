package com.ideal.evecore.interpreter.data;

import com.ideal.evecore.util.Result;

/**
 * Created by chris on 21/04/2017.
 */
public class EveResultObject {
    public static Result<EveObject> ok() {
        return Result.<EveObject>ok(EveNoneObject.NONE);
    }

    public static Result<EveObject> ok(EveObject o) {
        return Result.ok(o);
    }

    public static Result<EveObject> ko(Throwable t) {
        return Result.ko(t);
    }
}
