package com.hy.jame.springdatajpaplus.aspect;

import com.hy.jame.springdatajpaplus.annotation.Between;
import com.hy.jame.springdatajpaplus.annotation.Like;
import com.hy.jame.springdatajpaplus.dynamic.JpaPlusDynamicManager;
import com.hy.jame.springdatajpaplus.repository.JpaPlusRepository;
import com.hy.jame.springdatajpaplus.utils.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * author: HyJame
 * date:   2020/1/28
 * desc:   TODO
 */
public class JpaPlusAspectProcessor {

    private JpaPlusDynamicManager jpaPlusDynamicManager;

    public static JpaPlusAspectProcessor create() {
        return new JpaPlusAspectProcessor();
    }

    @SuppressWarnings("unchecked")
    public Object handle(ProceedingJoinPoint joinPoint) {
        jpaPlusDynamicManager = new JpaPlusDynamicManager();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        Parameter[] parameters = methodSignature.getMethod().getParameters();

        // 参数名
        String[] argNames = methodSignature.getParameterNames();
        // 参数值
        Object[] argValues = joinPoint.getArgs();

        Class returnClazz = methodSignature.getMethod().getReturnType();

        Object result = null;

        Specification specification = getSpecification(argNames, argValues, parameters);

        JpaPlusRepository jpaPlusRepository = (JpaPlusRepository) joinPoint.getThis();

        // 返回值 是否是 List
        if (returnClazz.isAssignableFrom(List.class)) {
            result = jpaPlusRepository.findAll(specification);
        } else if (returnClazz.isAssignableFrom(Optional.class)) {
            result = jpaPlusRepository.findOne(specification);
        } else if (returnClazz.isAssignableFrom(Page.class)) {
            result = executePage(jpaPlusRepository, argValues, specification);
        }

        return result;
    }

    /**
     * 构造查询条件
     *
     * @param argNames   方法参数名集合
     * @param argValues  方法参数值集合
     * @param parameters 方法参数信息集合
     */
    private Specification getSpecification(String[] argNames, Object[] argValues, Parameter[] parameters) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicatesList = parser(argNames, argValues, parameters, root, criteriaBuilder);

            //最终将查询条件拼好然后return
            Predicate[] predicates = new Predicate[predicatesList.size()];
            return criteriaBuilder.and(predicatesList.toArray(predicates));
        };
    }

    private List<Predicate> parser(String[] argNames,
                                   Object[] argValues,
                                   Parameter[] parameters,
                                   Root root,
                                   CriteriaBuilder criteriaBuilder) {

        //用于暂时存放查询条件的集合
        List<Predicate> predicatesList = new ArrayList<>();
        for (int i = 0; i < argNames.length; i++) {

            Object argValue = argValues[i];
            String argName = argNames[i];
            Parameter parameter = parameters[i];

            if (argValue instanceof Pageable) {
                continue;
            }

            if (parameter.isAnnotationPresent(Between.class)) {
                Between between = parameter.getAnnotation(Between.class);
                // 根据 Between 指定的 value, 为参数名
                argName = between.value();

                Path namePath = root.get(argName);

                i++;
                Parameter nextParameter = parameters[i];
                Comparable nextArgValue = (Comparable) argValues[i];

                if (hasIgnoreConditions(argName, argValue, parameter) || hasIgnoreConditions(argName, nextArgValue, nextParameter)) {
                    continue;
                }

                @SuppressWarnings("unchecked")
                Predicate predicate = criteriaBuilder.between(namePath, (Comparable) argValue, nextArgValue);
                predicatesList.add(predicate);
                continue;
            }

            if (hasIgnoreConditions(argName, argValue, parameter)) {
                continue;
            }

            Path namePath = root.get(argName);

            Predicate predicate;
            if (parameter.isAnnotationPresent(Like.class)) {
                predicate = criteriaBuilder.like(namePath, (String) argValue);
            } else {
                predicate = criteriaBuilder.equal(namePath, argValue);
            }

            predicatesList.add(predicate);
        }

        return predicatesList;
    }


    /**
     * 判断是否有 自定义注解 验证器
     *
     * @param argName   参数名
     * @param argValue  参数值
     * @param parameter 参数信息
     */
    private boolean hasIgnoreConditions(String argName, Object argValue, Parameter parameter) {
        return jpaPlusDynamicManager.getJpaPlusIgnoreList().stream()
                .anyMatch(jpaPlusDynamic -> jpaPlusDynamic.ignore(argName, argValue, parameter));
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

    /**
     * 获取 方法里面 pageable 参数值
     *
     * @param argValues 方法参数集合
     * @return pageable 参数值
     */
    private Pageable getPageable(Object[] argValues) {
        return (Pageable) Arrays.stream(argValues)
                .filter(argValue -> argValue instanceof Pageable)
                .findFirst().orElse(null);
    }
}
