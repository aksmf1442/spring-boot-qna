package com.codessquad.qna.service;

import com.codessquad.qna.domain.User;
import com.codessquad.qna.repository.MemoryUserRepository;
import com.codessquad.qna.repository.UserRepository;
import java.util.List;

public class UserService {

    private UserRepository userRepository = new MemoryUserRepository();

    public void join(User user) {
        userRepository.save(user);
    }

    public List<User> findUserAll() {
        return userRepository.findUserALl();
    }

    public User findUserByUserId(String userId) {
        return userRepository.findUserByUserID(userId);
    }
}
