package com.hy.jame.springdatajpaplus.dynamic;

import com.hy.jame.springdatajpaplus.annotation.NotNull;

import java.lang.reflect.Parameter;

/**
 * author: HyJame
 * date:   2020/1/27
 * desc:   TODO
 */
public class NotNullDynamic implements JpaPlusDynamic {

    @Override
    public boolean ignore(String argName, Object argValue, Parameter parameter) {
        return parameter.isAnnotationPresent(NotNull.class) && argValue == null;
    }
}
