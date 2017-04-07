package com.ideal.evecore.interpreter.data;

import java.util.Arrays;
import java.util.List;

/**
 * Created by chris on 06/04/2017.
 */
public class EveObjectList implements EveObject {
    private List<EveObject> values;

    public EveObjectList(List<EveObject> l){
        values = l;
    }

    public EveObjectList(EveObject... os){
        this(Arrays.asList(os));
    }

    public List<EveObject> getValues(){
        return values;
    }
}
