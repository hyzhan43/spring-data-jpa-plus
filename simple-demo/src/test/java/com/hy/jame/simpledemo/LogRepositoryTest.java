package com.hy.jame.simpledemo;

import com.hy.jame.simpledemo.domain.Log;
import com.hy.jame.simpledemo.repository.LogRepository;
import com.hy.jame.springdatajpaplus.aspect.JpaPlusAspect;
import com.hy.jame.springdatajpaplus.utils.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Autowired
    JpaPlusAspect jpaPlusAspect;

    @BeforeEach
    public void init() {
        pageable = PageRequest.of(1, 10);
    }

    @Test
    public void test_get_all_logs_with_username() {
        Page<Log> logPage = logRepository.getByUsernameAndCreateTimeBetween("hyzhan",
                DateUtils.stringFormat("2020-01-27 10:07:44"),
                DateUtils.stringFormat("2020-01-27 14:07:44"),
                pageable);

        assertThat(logPage.getTotalElements(), is(1L));
    }

    @Test
    public void test_get_all_logs_with_start_time_and_end_time() {
        Page<Log> logPage = logRepository.getByUsernameAndCreateTimeBetween(null,
                DateUtils.stringFormat("2020-01-30 12:08:53"),
                DateUtils.stringFormat("2020-01-30 14:08:53"),
                pageable);

        assertThat(logPage.getTotalElements(), is(1L));
    }

    @Test
    public void test_get_all_logs_with_null() {
        Page<Log> logPage = logRepository.getByUsernameAndCreateTimeBetween("",
                null,
                null,
                pageable);
        assertNotNull(logPage);
        assertThat(logPage.getTotalElements(), is((long) logRepository.findAll().size()));
    }
}
