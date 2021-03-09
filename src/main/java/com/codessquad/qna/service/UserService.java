package com.codessquad.qna.service;

import com.codessquad.qna.domain.User;
import com.codessquad.qna.repository.UserRepository;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void join(User user) {
        validateUserDuplication(user);
        userRepository.save(user);
    }

    private void validateUserDuplication(User user) {
        if (userRepository.existsUserByUserId(user.getUserId())) {
            throw new ExistedUserException();
        }
    }

    public List<User> findUserAll() {
        return userRepository.findAll();
    }

    public void updateUserData(User originUser, User user) {
        originUser.update(user);
        userRepository.save(originUser);
    }

    public User findUserByUserId(String userId) {
        return userRepository.findUserByUserId(userId);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
