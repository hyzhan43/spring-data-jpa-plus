package com.hy.jame.springdatajpaplus.aspect;

import com.hy.jame.springdatajpaplus.domain.User;
import com.hy.jame.springdatajpaplus.repository.JpaPlusRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * author: HyJame
 * date:   2020/1/13
 * desc:   TODO
 */
@Aspect
@Component
public class JpaAspect {

    @Pointcut("execution(public * com.hy.jame.springdatajpaplus.repository.*.*(..))" +
            "&& @annotation(com.hy.jame.springdatajpaplus.annotation.Optional)")
    public void jpaAspect() {
    }


    @SuppressWarnings("unchecked")
    @Around("jpaAspect()")
    public Object initJpa(ProceedingJoinPoint joinPoint) {
        JpaPlusRepository customRepository = (JpaPlusRepository) joinPoint.getThis();
        List<User> userList = customRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {

            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            // 参数名
            String[] argNames = methodSignature.getParameterNames();
            // 参数值
            Object[] argValues = joinPoint.getArgs();

            //用于暂时存放查询条件的集合
            List<Predicate> predicatesList = new ArrayList<>();

            for (int i = 0; i < argNames.length; i++) {
                if (argValues[i] != null){
                    // 获取比较的属性
                    Path namePath = root.get(argNames[i]);
                    Predicate predicate = criteriaBuilder.equal(namePath, argValues[i]);
                    predicatesList.add(predicate);
                }
            }

            //最终将查询条件拼好然后return
            Predicate[] predicates = new Predicate[predicatesList.size()];
            return criteriaBuilder.and(predicatesList.toArray(predicates));
        });

        System.out.println(userList);

        try {
            joinPoint.proceed();
            return userList;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
}
