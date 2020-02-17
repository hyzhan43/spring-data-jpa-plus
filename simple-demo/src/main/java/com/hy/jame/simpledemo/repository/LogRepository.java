package com.hy.jame.simpledemo.repository;

import com.hy.jame.simpledemo.domain.Log;
import com.hy.jame.springdatajpaplus.annotation.Between;
import com.hy.jame.springdatajpaplus.annotation.Dynamic;
import com.hy.jame.springdatajpaplus.annotation.NotNull;
import com.hy.jame.springdatajpaplus.repository.JpaPlusRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * author：  HyZhan
 * create：  2019/4/14
 * desc：    TODO
 */
public interface LogRepository extends JpaPlusRepository<Log, Integer> {

    /**
     * "if(?1 !='',username=?1,1=1)"
     * 如果 ?1 (第一个参数username) 不为空 就将参数传入查询条件, 为空时就 1=1 为真, 不加入查询条件(对查询结果不产生作用)
     */
//    @Query(value = "select * from lin_log where " +
//            "if(?1 !='',username=?1,1=1) and " +
//            "if(?2 !='' and ?3 !='',create_time between ?2 and ?3,1=1)",
//            nativeQuery = true)
//    Page<Log> getByUsernameAndCreateTimeBetween(String username, Date start, Date end, Pageable pageable);

    @Dynamic
    Page<Log> getByUsernameAndCreateTimeBetween(@NotNull String username,
                                                @Between("createTime") @NotNull Date start,
                                                @Between("createTime") @NotNull Date end,
                                                Pageable pageable);

    /**
     * "message like concat('%',?1,'%')"
     * concat()函数用于将多个字符串连接成一个字符串,拼接模糊条件 ?1代表第一参数(keyword)
     */
    @Query(value = "select * from lin_log where " +
            "message like concat('%',?1,'%') and " +
            "if(?2 !='',username=?2,1=1) and " +
            "if(?3 !='' and ?4 !='',create_time between ?3 and ?4,1=1)",
            nativeQuery = true)
    Page<Log> getLogByKeywordAndNameAndTime(String keyword, String username, Date start, Date end, Pageable pageable);
}
