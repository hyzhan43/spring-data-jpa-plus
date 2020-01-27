package com.hy.jame.springdatajpaplus.aspect;

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

    @Autowired
    JpaPlusDynamicManager jpaPlusDynamicManager;


//    @Pointcut("execution(public * com.hy.jame.springdatajpaplus.repository.*.*(..))" +
//            "&& @annotation(com.hy.jame.springdatajpaplus.annotation.Dynamic)")
//    public void jpaAspect() {
//    }

    @Pointcut("@annotation(com.hy.jame.springdatajpaplus.annotation.Dynamic)")
    public void jpaAspect() {
    }

    @SuppressWarnings("unchecked")
    @Around("jpaAspect()")
    public Object realJpaQuery(ProceedingJoinPoint joinPoint) {
        JpaPlusRepository jpaPlusRepository = (JpaPlusRepository) joinPoint.getThis();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        Parameter[] parameters = methodSignature.getMethod().getParameters();

        // 参数名
        String[] argNames = methodSignature.getParameterNames();
        // 参数值
        Object[] argValues = joinPoint.getArgs();

        Method method = methodSignature.getMethod();

        Class returnClazz = method.getReturnType();

        Object result = null;

        Specification specification = getSpecification(argNames, argValues, parameters);

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
            //用于暂时存放查询条件的集合
            List<Predicate> predicatesList = new ArrayList<>();

            for (int i = 0; i < argNames.length; i++) {

                Object argValue = argValues[i];
                String argName = argNames[i];
                Parameter parameter = parameters[i];

                if (argValue instanceof Pageable || hasIgnoreConditions(argName, argValue, parameter)) {
                    continue;
                }

                // 获取比较的属性
                Path namePath = root.get(argName);
                Predicate predicate = criteriaBuilder.equal(namePath, argValue);
                predicatesList.add(predicate);
            }

            //最终将查询条件拼好然后return
            Predicate[] predicates = new Predicate[predicatesList.size()];
            return criteriaBuilder.and(predicatesList.toArray(predicates));
        };
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