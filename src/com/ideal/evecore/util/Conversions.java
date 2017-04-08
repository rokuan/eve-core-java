package com.ideal.evecore.util;

/**
 * Created by Christophe on 08/04/2017.
 */
public class Conversions {
    public static <T> Result<T> toResult(Option<T> o) {
        return o.map(new Transformer<T, Result<T>>() {
            @Override
            public Result<T> apply(T t) {
                return Result.ok(t);
            }
        }).getOrElse(Result.<T>ko("No result"));
    }

    public static <T> Option<T> toOption(Result<T> r) {
        if (r.isSuccess()) {
            return Option.apply(r.get());
        } else {
            return Option.empty();
        }
    }
}
