package com.codessquad.qna.controller;

import com.codessquad.qna.domain.PasswordVerifier;
import com.codessquad.qna.domain.User;
import com.codessquad.qna.exception.ExistedUserException;
import com.codessquad.qna.service.UserService;
import com.codessquad.qna.util.HttpSessionUtils;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public String createUser(User user, RedirectAttributes redirect) {
        try {
            userService.join(user);
        } catch (ExistedUserException e) {
            redirect.addFlashAttribute("fail", true);
            return "redirect:/users/form";
        }
        return "redirect:/users";
    }

    @GetMapping("")
    public String userList(Model model) {
        List<User> users = userService.findUserAll();
        model.addAttribute("users", users);
        return "/user/list";
    }

    @GetMapping("/{id}")
    public String userProfile(@PathVariable Long id, Model model) {
        User user = userService.findById(id);

        if (user == null) {
            return "redirect:/users";
        }
        model.addAttribute("user", user);
        return "/user/profile";
    }

    @GetMapping("/{id}/form")
    public String updateUserForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!HttpSessionUtils.isLoginUser(session)) {
            return "redirect:/users/login";
        }

        User sessionUser = HttpSessionUtils.getUserFromSession(session);

        if (!sessionUser.isMatchingId(id)) {
            return "redirect:/users";
        }

        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "/user/updateForm";
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, User user,
        PasswordVerifier passwordVerifier, RedirectAttributes redirect, HttpSession session) {

        if (!HttpSessionUtils.isLoginUser(session)) {
            return "redirect:/users/login";
        }

        User sessionUser = HttpSessionUtils.getUserFromSession(session);

        if (!sessionUser.isMatchingId(id)) {
            return "redirect:/users";
        }

        User originUser = userService.findById(id);

        if (originUser.isMatchingPassword(passwordVerifier.getReceivedPassword())) {
            userService.updateUserData(originUser, user);
            return "redirect:/users";
        }

        redirect.addFlashAttribute("fail", true);
        return "redirect:/users/" + originUser.getId() + "/form";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("sessionUser");
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "/user/login";
    }

    @PostMapping("/login")
    public String login(String userId, String password, RedirectAttributes redirect,
        HttpSession session) {
        User user = userService.findUserByUserId(userId);

        if (user != null && user.isMatchingPassword(password)) {
            session.setAttribute("sessionUser", user);
            return "redirect:/";
        }

        redirect.addFlashAttribute("fail", true);
        return "redirect:/users/login";
    }
}
