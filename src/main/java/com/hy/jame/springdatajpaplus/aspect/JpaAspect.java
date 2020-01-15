package com.hy.jame.springdatajpaplus.aspect;

import com.hy.jame.springdatajpaplus.domain.User;
import com.hy.jame.springdatajpaplus.repository.JpaPlusRepository;
import com.hy.jame.springdatajpaplus.utils.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * author: HyJame
 * date:   2020/1/13
 * desc:   TODO
 */
@Aspect
@Component
public class JpaAspect {

    @Pointcut("execution(public * com.hy.jame.springdatajpaplus.repository.*.*(..))" +
            "&& @annotation(com.hy.jame.springdatajpaplus.annotation.Dynamic)")
    public void jpaAspect() {
    }

    @SuppressWarnings("unchecked")
    @Around("jpaAspect()")
    public Object initJpa(ProceedingJoinPoint joinPoint) {

        JpaPlusRepository jpaPlusRepository = (JpaPlusRepository) joinPoint.getThis();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 参数名
        String[] argNames = methodSignature.getParameterNames();
        // 参数值
        Object[] argValues = joinPoint.getArgs();

        Specification specification = ((root, criteriaQuery, criteriaBuilder) -> {
            //用于暂时存放查询条件的集合
            List<Predicate> predicatesList = new ArrayList<>();

            for (int i = 0; i < argNames.length; i++) {

                Object args = argValues[i];

                if (args == null || args instanceof Pageable) {
                    continue;
                }

                // 获取比较的属性
                Path namePath = root.get(argNames[i]);
                Predicate predicate = criteriaBuilder.equal(namePath, args);
                predicatesList.add(predicate);
            }
            //最终将查询条件拼好然后return
            Predicate[] predicates = new Predicate[predicatesList.size()];
            return criteriaBuilder.and(predicatesList.toArray(predicates));
        });

        Method method = methodSignature.getMethod();

        Class<?> returnClazz = method.getReturnType();

        Object result = null;

        // 返回值 是否是 List
        if (returnClazz.isAssignableFrom(List.class)) {
            Log.show("是 List 类型！");
            result = jpaPlusRepository.findAll(specification);
        } else if (returnClazz.isAssignableFrom(Optional.class)) {
            Log.show("是 Optional 类型！");
            result = jpaPlusRepository.findOne(specification);
        } else if (returnClazz.isAssignableFrom(Page.class)) {
            result = executePage(jpaPlusRepository, argValues, specification);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private Object executePage(JpaPlusRepository jpaPlusRepository, Object[] argValues, Specification specification) {
        Pageable pageable = getPageable(argValues);

        if (pageable == null) {
            throw new IllegalArgumentException("method no pageable param");
        }

        Log.show("是 Page 类型！" + pageable);
        return jpaPlusRepository.findAll(specification, pageable);
    }

    private Pageable getPageable(Object[] argValues) {
        for (Object argValue : argValues) {
            if (argValue instanceof Pageable) {
                return (Pageable) argValue;
            }
        }
        return null;
    }
}
