package com.hy.jame.springdatajpaplus.repository;

import com.hy.jame.springdatajpaplus.annotation.Optional;
import com.hy.jame.springdatajpaplus.domain.User;

import java.util.List;

/**
 * author: HyJame
 * date:   2020/1/4
 * desc:   TODO
 */
public interface UserRepository extends JpaPlusRepository<User, Long> {

    List<User> findByNameLike(String name);

    @Optional
    List<User> getByNameAndSex(String name, Integer sex);

    @Optional
    List<User> getByName(String name);
}
