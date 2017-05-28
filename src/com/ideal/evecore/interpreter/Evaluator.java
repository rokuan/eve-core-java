package com.ideal.evecore.interpreter;

import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.util.Result;
import com.rokuan.calliopecore.sentence.structure.InterpretationObject;

/**
 * Created by Christophe on 22/04/2017.
 */
public interface Evaluator {
    /**
     * Interprets the object
     *
     * @param o The object to evaluate
     * @return The result of the interpretation if the operation is successful
     */
    Result<EveObject> eval(InterpretationObject o);
}
