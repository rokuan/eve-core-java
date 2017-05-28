package com.ideal.evecore.util;

/**
 * Created by Christophe on 06/04/2017.
 */
public abstract class Matcher<T, R> {
    private Class<T> clazz;
    private Transformer<T, R> transformer;

    static public class NoMatchException extends Exception {

    }

    public <V> R getIfMatches(V value) throws NoMatchException {
        if (value == null || !clazz.isAssignableFrom(value.getClass())) {
            throw new NoMatchException();
        }
        return transformer.apply((T) value);
    }

    protected Matcher(Class<T> c, Transformer<T, R> t) {
        clazz = c;
        transformer = t;
    }

    static public class SimpleMatcher<T, R> extends Matcher<T, R> {
        public SimpleMatcher(Class<T> c, Transformer<T, R> t) {
            super(c, t);
        }
    }

    static public class AllMatcher<R> extends Matcher<Object, R> {
        public AllMatcher(Transformer<Object, R> t) {
            super(Object.class, t);
        }
    }
}
