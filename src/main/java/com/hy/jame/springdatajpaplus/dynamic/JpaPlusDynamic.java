package com.hy.jame.springdatajpaplus.dynamic;


import java.lang.reflect.Parameter;

/**
 * author: HyJame
 * date:   2020/1/27
 * desc:   TODO
 */
public interface JpaPlusDynamic {

    boolean ignore(String argName, Object argValue, Parameter parameter);
}
