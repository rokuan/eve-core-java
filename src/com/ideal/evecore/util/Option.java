package com.ideal.evecore.util;

/**
 * Created by chris on 06/04/2017.
 */
public abstract class Option<T> {
    abstract public boolean isEmpty();
    abstract public boolean isDefined();
    abstract public T get();
    abstract public T getOrElse(T other);
    abstract public T orNull();
    abstract public <R> Option<R> map(Transformer<T, R> transformer);
    abstract public Option<T> filter(Predicate<T> predicate);
    abstract public <M extends T, O> Option<O> collect(Matcher<M, ? extends O>... matchers);

    static public <T> Option<T> apply(T t) {
        if(t == null){
            return new None<T>();
        } else {
            return new Some<T>(t);
        }
    }

    static public <T> Option<T> empty() {
        return new None<T>();
    }

    static public class Some<T> extends Option<T> {
        private T value;

        public Some(T t){
            value = t;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean isDefined() {
            return true;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public T getOrElse(T other) {
            return value;
        }

        @Override
        public T orNull() {
            return value;
        }

        @Override
        public <R> Option<R> map(Transformer<T, R> transformer) {
            return new Some<R>(transformer.apply(value));
        }

        @Override
        public Option<T> filter(Predicate<T> predicate) {
            return predicate.matches(value) ? new Some<T>(value) : new None<T>();
        }

        @Override
        public <M extends T, O> Option<O> collect(Matcher<M, ? extends O>... matchers) {
            for(Matcher<M, ? extends O> matcher: matchers){
                try {
                    return new Some<O>(matcher.getIfMatches(value));
                } catch (Matcher.NoMatchException e) {

                }
            }
            return new None<O>();
        }
    }

    static public class None<T> extends Option<T> {
        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean isDefined() {
            return false;
        }

        @Override
        public T get() {
            return null;
        }

        @Override
        public T getOrElse(T other) {
            return other;
        }

        @Override
        public T orNull() {
            return null;
        }

        @Override
        public <R> Option<R> map(Transformer<T, R> transformer) {
            return new None<R>();
        }

        @Override
        public Option<T> filter(Predicate<T> predicate) {
            return new None<T>();
        }

        @Override
        public <M extends T, O> Option<O> collect(Matcher<M, ? extends O>... matchers) {
            return new None<O>();
        }
    }
}
