package com.hy.jame.springdatajpaplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * author: HyJame
 * date:   2020/1/5
 * desc:   TODO
 */
@NoRepositoryBean
public interface JpaPlusRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

}
