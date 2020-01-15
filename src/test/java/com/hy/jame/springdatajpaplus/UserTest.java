package com.hy.jame.springdatajpaplus;

import com.hy.jame.springdatajpaplus.domain.User;
import com.hy.jame.springdatajpaplus.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
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

    @Test
    public void test_find_user_by_name(){
        List<User> userList = userRepository.findByName("张三");
        System.out.println("分割线");
        List<User> userList2 = userRepository.getByName("张三");
    }

    @Test
    public void test_null_predicate_specification() {
        List<User> userList = getUserByName(null);
        assertThat(userList.size()).isEqualTo(userRepository.findAll().size());
    }

    @Test
    public void test_specification() {
        String name = "李四";
        List<User> userList = getUserByName(name);
        assertThat(userList.size()).isEqualTo(1);
    }

    private List<User> getUserByName(String name) {
        return userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            // 获取比较的属性
            Path<String> namePath = root.get("name");
            Predicate predicate = null;

            if (name != null && !name.isEmpty()) {
                predicate = criteriaBuilder.equal(namePath, name);
            }

            return predicate;
        });
    }

    @Test
    public void test_get_user_by_name() {
        String name = null;
        List<User> userList = userRepository.getByName(name);
        assertThat(userList.size()).isEqualTo(userRepository.findAll().size());

        name = "张三";
        userList = userRepository.getByName(name);
        assertThat(userList.size()).isEqualTo(1);
    }


    @Test
    public void test_get_user_by_name_and_sex() {
        String name = "李四";
        Integer sex = 1;
        List<User> userList = userRepository.getByNameAndSex(name, sex);
        assertThat(userList.size()).isEqualTo(1);
    }

    @Test
    public void test_get_user_by_name_and_return_optional(){
        String name = "李四";
        Optional<User> userList = userRepository.getOneByName(name);
        assertTrue(userList.isPresent());
    }

    @Test
    public void test_get_user_by_name_and_return_page(){
        String name = "李四";
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = userRepository.getPageByName(name, pageable);
        assertThat(userPage.getContent().size()).isEqualTo(1);
    }

    @Test
    public void test_get_user_by_name_and_error_sex(){
        String name = "李四";
        Integer sex = 99;
        List<User> userList = userRepository.getByNameAndSex(name, sex);
        assertThat(userList).isEmpty();
    }
}
