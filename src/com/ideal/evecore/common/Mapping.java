package com.ideal.evecore.common;

import com.ideal.evecore.universe.matcher.ValueMatcher;
import com.ideal.evecore.util.Pair;

import java.util.HashMap;

/**
 * Created by Christophe on 06/04/2017.
 */
public class Mapping<T> extends HashMap<String, T> {
    public Mapping() {

    }

    public Mapping(Pair<String, T>... pairs) {
        for (Pair<String, T> entry: pairs) {
            put(entry.first, entry.second);
        }
    }
}
