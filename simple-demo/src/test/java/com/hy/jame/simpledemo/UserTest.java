package com.hy.jame.simpledemo;

import com.hy.jame.simpledemo.domain.User;
import com.hy.jame.simpledemo.repository.UserRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * author: HyJame
 * date:   2020/1/4
 * desc:   TODO
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    /**
     * 测试 查询 user, 如果 name为null, 就查询全部
     */
    @Test
    public void test_get_user_by_null_name() {
        List<User> userList = userRepository.getByName(null);
        AssertionsForClassTypes.assertThat(userList.size()).isEqualTo(userRepository.findAll().size());
    }

    /**
     * 测试 查询 user, 如果 name为null或者 ""空串 , 就查询全部
     */
    @Test
    public void test_get_user_by_empty_name() {
        List<User> userList = userRepository.getByName("");
        AssertionsForClassTypes.assertThat(userList.size()).isEqualTo(userRepository.findAll().size());
    }

    /**
     * 测试 查询 user, 如果name 是 有值，则添加到查询条件 正常查询
     */
    @Test
    public void test_get_user_by_name() {
        String name = "张三";
        List<User> userList = userRepository.getByName(name);
        AssertionsForClassTypes.assertThat(userList.size()).isEqualTo(1);
    }

    @Test
    public void test_get_user_by_name_and_sex() {
        String name = "李四";
        Integer sex = 1;
        List<User> userList = userRepository.getByNameAndSex(name, sex);
        AssertionsForClassTypes.assertThat(userList.size()).isEqualTo(1);
    }

    /**
     * 测试 repository 添加 @Dynamic 注解返回 Optional值
     */
    @Test
    public void test_get_user_by_name_and_return_optional() {
        String name = "李四";
        Optional<User> userList = userRepository.getOneByName(name);
        assertTrue(userList.isPresent());
    }

    /**
     * 测试 repository 添加 @Dynamic 注解返回 page 值
     */
    @Test
    public void test_get_user_by_name_and_return_page() {
        String name = "李四";
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = userRepository.getPageByName(name, pageable);
        AssertionsForClassTypes.assertThat(userPage.getContent().size()).isEqualTo(1);
    }

    @Test
    public void test_get_user_by_name_and_error_sex() {
        String name = "李四";
        Integer sex = 99;
        List<User> userList = userRepository.getByNameAndSex(name, sex);
        assertTrue(userList.isEmpty());
    }

    /**
     * 测试between注解, 正常查询
     */
    @Test
    public void test_get_user_by_age_between_10_and_20() {
        List<User> userList = userRepository.findByAgeBetween(15, 25);
        AssertionsForClassTypes.assertThat(userList.size()).isEqualTo(1);
    }

    /**
     * 测试between注解, begin, end 均为null值, 则查询全部
     */
    @Test
    public void test_get_user_by_age_between_null() {
        List<User> userList = userRepository.findByAgeBetween(null, null);
        AssertionsForClassTypes.assertThat(userList.size()).isEqualTo(userRepository.findAll().size());
    }

    /**
     * 测试between注解, begin, end 其中一个为null值, 则查询全部
     */
    @Test
    public void test_get_user_by_age_between_one_null() {
        List<User> userList = userRepository.findByAgeBetween(null, 25);
        AssertionsForClassTypes.assertThat(userList.size()).isEqualTo(userRepository.findAll().size());
    }

    @Test
    public void test_get_user_by_name_and_age_between() {
        List<User> userList = userRepository.findByNameAndAgeBetween(null, 15, 25);
        AssertionsForClassTypes.assertThat(userList.size()).isEqualTo(1);
    }

    @Test
    public void test_get_user_by_name_and_age_between_and_pageable() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<User> userList = userRepository.findByNameAndAgeBetween(null, 15, 25, pageable);
        AssertionsForClassTypes.assertThat(userList.getTotalElements()).isEqualTo(1);
    }
}
