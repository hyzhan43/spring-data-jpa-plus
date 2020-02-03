package com.hy.jame.springdatajpaplus;

import com.hy.jame.springdatajpaplus.domain.Log;
import com.hy.jame.springdatajpaplus.repository.LogRepository;
import com.hy.jame.springdatajpaplus.utils.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static sun.nio.cs.Surrogate.is;

/**
 * author: HyJame
 * date:   2020/1/28
 * desc:   TODO
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LogRepositoryTest {

    @Autowired
    LogRepository logRepository;

    private Pageable pageable;

    @BeforeEach
    public void init() {
        pageable = PageRequest.of(1, 10);
    }


    @Test
    public void test_get_all_logs_with_username() {
//        Page<Log> logPage = logRepository.getByUsernameAndCreateTimeBetween("hyzhan",
//                DateUtils.stringFormat("2020-01-27 10:07:44"),
//                DateUtils.stringFormat("2020-01-27 14:07:44"),
//                pageable);
//
//        assertThat(logPage.getTotalElements()).isEqualTo(1);
    }

    /**
     * 测试 username 为null 查询
     */
    @Test
    public void test_get_all_logs_with_start_time_and_end_time() {

        Page<Log> logPage = logRepository.findByUsernameAndCreateTimeBetween(null,
                DateUtils.stringFormat("2020-01-30 12:08:53"),
                DateUtils.stringFormat("2020-01-31 14:08:53"),
                pageable);

        assertThat(logPage.getTotalElements()).isEqualTo(1);
    }

    /**
     * 测试 createTime 开始时间结束时间都为null, 查询
     */
    @Test
    public void test_get_log_by_username_and_all_of_null_create_time() {

        String hyzhan = "hyzhan";
        Page<Log> logPage = logRepository.findByUsernameAndCreateTimeBetween(hyzhan,
                null,
                null,
                pageable);

        assertThat(logPage.getTotalElements()).isEqualTo(logRepository.findByUsername(hyzhan).size());
    }


    /**
     * 测试 createTime 为结束时间为null, 查询
     */
    @Test
    public void test_get_log_by_username_and_end_create_time_null() {

        String hyzhan = "hyzhan";
        Page<Log> logPage = logRepository.findByUsernameAndCreateTimeBetween(hyzhan,
                null,
                DateUtils.stringFormat("2020-01-31 14:08:53"),
                pageable);

        assertThat(logPage.getTotalElements()).isEqualTo(logRepository.findByUsername(hyzhan).size());
    }

    /**
     * 测试 createTime, username 都为null 查询
     */
    @Test
    public void test_get_all_logs_with_null() {
        Page<Log> logPage = logRepository.findByUsernameAndCreateTimeBetween("",
                null,
                null,
                pageable);
        assertThat(logPage.getTotalElements()).isEqualTo(logRepository.findAll().size());
    }

    /**
     * 测试 正常like 模糊搜索
     */
    @Test
    public void test_get_log_by_message_and_username() {

        String message = "%小明%";

        Page<Log> logPage = logRepository.findByMessageAndUsernameAndCreateTimeBetween(
                message,
                "",
                null,
                null,
                pageable
        );

        assertThat(logPage.getTotalElements()).isEqualTo(logRepository.findByMessageLike(message).size());
    }

    /**
     * 测试message 为null, like 模糊搜索
     */
    @Test
    public void test_get_log_by_message_is_null() {

        String message = "";

        Page<Log> logPage = logRepository.findByMessageAndUsernameAndCreateTimeBetween(
                message,
                "",
                null,
                null,
                pageable
        );

        assertThat(logPage.getTotalElements()).isEqualTo(logRepository.findAll().size());
    }


    @Test
    public void test() {
        Date startDate = DateUtils.stringFormat("2020-01-27 12:07:44");
        Date endDate = DateUtils.stringFormat("2020-01-27 15:07:44");

        Date now = DateUtils.stringFormat("2020-01-27 13:07:44");

        assertThat(startDate.compareTo(now)).isEqualTo(-1);
        assertThat(endDate.compareTo(now)).isEqualTo(1);
    }
}
