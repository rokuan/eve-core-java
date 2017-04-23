package com.ideal.evecore.interpreter;

import com.ideal.evecore.interpreter.data.EveObject;
import com.ideal.evecore.util.Result;
import com.rokuan.calliopecore.sentence.structure.InterpretationObject;

/**
 * Created by Christophe on 22/04/2017.
 */
public interface Evaluator {
    Result<EveObject> eval(InterpretationObject o);
}
