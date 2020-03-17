package com.hy.jame.simpledemo.domain;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * author: HyJame
 * date:   2020/1/4
 * desc:   TODO
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "t_user")
public class User {

    @Id
    private Long id;

    private String name;

    private Integer sex;

    private Integer age;
}
