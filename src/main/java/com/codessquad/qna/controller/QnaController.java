package com.codessquad.qna.controller;

import com.codessquad.qna.domain.Question;
import com.codessquad.qna.domain.User;
import com.codessquad.qna.service.QnaService;
import com.codessquad.qna.util.HttpSessionUtils;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/questions")
public class QnaController {

    private final QnaService qnaService;

    public QnaController(QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @PostMapping
    public String createQuestion(Question question, HttpSession session) {
        User sessionUser = HttpSessionUtils.getUserFromSession(session);
        if (sessionUser == null) {
            return "redirect:/";
        }

        question.setWriter(sessionUser);
        qnaService.save(question);
        return "redirect:/";
    }

    @GetMapping("/form")
    public String questionForm(HttpSession session) {
        if (!HttpSessionUtils.isLoginUser(session)) {
            return "redirect:/users/login";
        }
        return "/qna/form";
    }

    @GetMapping("/{id}")
    public String showQuestion(@PathVariable Long id, Model model) {
        Question question = qnaService.findQuestionById(id);
        model.addAttribute("question", question);
        return "/qna/show";
    }


}
