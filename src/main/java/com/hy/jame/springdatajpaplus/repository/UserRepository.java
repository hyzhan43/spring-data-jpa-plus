package com.hy.jame.springdatajpaplus.repository;

import com.hy.jame.springdatajpaplus.annotation.Between;
import com.hy.jame.springdatajpaplus.annotation.Dynamic;
import com.hy.jame.springdatajpaplus.annotation.NotEmpty;
import com.hy.jame.springdatajpaplus.annotation.NotNull;
import com.hy.jame.springdatajpaplus.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * author: HyJame
 * date:   2020/1/4
 * desc:   TODO
 */
public interface UserRepository extends JpaPlusRepository<User, Long> {

    List<User> findByName(String name);

    @Dynamic
    List<User> getByNameAndSex(String name, Integer sex);

    @Dynamic
    List<User> getByName(@NotEmpty String name);

    @Dynamic
    Optional<User> getOneByName(String name);

    @Dynamic
    Page<User> getPageByName(String name, Pageable pageable);

//    @Dynamic
//    List<User> findBySexBetween(@Between @NotNull Integer begin,@Between @NotNull Integer end);

    @Dynamic
    List<User> findByAgeBetween(@Between("age") @NotNull Integer begin,
                                @Between("age") @NotNull Integer end);

    @Dynamic
    List<User> findByNameAndAgeBetween(@NotNull String name,
                                       @Between("age") @NotNull Integer begin,
                                       @Between("age") @NotNull Integer end);

    @Dynamic
    Page<User> findByNameAndAgeBetween(@NotNull String name,
                                       @Between("age") @NotNull Integer begin,
                                       @Between("age") @NotNull Integer end,
                                       Pageable pageable);
}
