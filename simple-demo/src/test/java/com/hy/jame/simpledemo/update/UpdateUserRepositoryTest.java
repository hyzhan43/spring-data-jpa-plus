package com.hy.jame.simpledemo.update;

import com.hy.jame.simpledemo.domain.User;
import com.hy.jame.simpledemo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * author: HyJame
 * date:   2020/3/17
 * desc:   TODO
 */
@SpringBootTest
public class UpdateUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test_update_user_by_null() {
        Optional<User> userOptional = userRepository.findById(1L);
        if (userOptional.isPresent()){
            User user = userOptional.get();

            user.setName(null);
            User newUser = userRepository.save(user);
            assertNull(newUser.getName());
        }
    }
}
