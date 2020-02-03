package com.hy.jame.springdatajpaplus.aspect;

import com.hy.jame.springdatajpaplus.annotation.Between;
import com.hy.jame.springdatajpaplus.dynamic.JpaPlusDynamicManager;
import com.hy.jame.springdatajpaplus.repository.JpaPlusRepository;
import com.hy.jame.springdatajpaplus.utils.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * author: HyJame
 * date:   2020/1/13
 * desc:   TODO
 */
@Aspect
@Component
public class JpaPlusAspect {


//    JpaPlusAspectProcessor jpaPlusAspectProcessor;

//    @Pointcut("execution(public * com.hy.jame.springdatajpaplus.repository.*.*(..))" +
//            "&& @annotation(com.hy.jame.springdatajpaplus.annotation.Dynamic)")
//    public void jpaAspect() {
//    }

    @Pointcut("@annotation(com.hy.jame.springdatajpaplus.annotation.Dynamic)")
    public void jpaAspect() {
    }

    @Around("jpaAspect()")
    public Object realJpaQuery(ProceedingJoinPoint joinPoint) {
        Log.show("拦截器开始执行------------->>>>>>>");
        return JpaPlusAspectProcessor.create().handle(joinPoint);
    }


}
