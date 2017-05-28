package com.ideal.evecore.interpreter.data;

import com.rokuan.calliopecore.sentence.IAction;

/**
 * Created by Christophe on 28/05/2017.
 */
public abstract class AbstractEveStructuredObject implements EveStructuredObject {
    @Override
    public final boolean call(IAction action) {
        try {
            boolean result = call(action.getAction());
            if (result) {
                if (action.isStateBound()) {
                    return setState(action.getBoundState(), action.getState());
                }
            }
            return result;
        } catch (Exception e) {
            return false;
        }
    }

    abstract protected boolean call(IAction.ActionType action);
}
