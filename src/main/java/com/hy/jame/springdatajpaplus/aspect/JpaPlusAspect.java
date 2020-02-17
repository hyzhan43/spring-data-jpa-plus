package com.hy.jame.springdatajpaplus.aspect;

import com.hy.jame.springdatajpaplus.utils.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * author: HyJame
 * date:   2020/2/3
 * desc:   TODO
 */
@Aspect
public class JpaPlusAspect {

    @Pointcut("@annotation(com.hy.jame.springdatajpaplus.annotation.Dynamic)")
    public void dynamic() {
    }

    @Around("dynamic()")
    public Object realJpaQuery(ProceedingJoinPoint joinPoint) {
        Log.show("拦截器开始执行------------->>>>>>>");
        return JpaPlusAspectProcessor.create().handle(joinPoint);
    }
}
