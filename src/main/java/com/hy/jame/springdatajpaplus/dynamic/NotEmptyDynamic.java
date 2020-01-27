package com.hy.jame.springdatajpaplus.dynamic;

import com.hy.jame.springdatajpaplus.annotation.NotEmpty;
import org.springframework.util.StringUtils;

import java.lang.reflect.Parameter;

/**
 * author: HyJame
 * date:   2020/1/27
 * desc:   TODO
 */
public class NotEmptyDynamic implements JpaPlusDynamic {

    @Override
    public boolean ignore(String argName, Object argValue, Parameter parameter) {
        return parameter.isAnnotationPresent(NotEmpty.class) && (StringUtils.isEmpty(argValue));
    }
}
