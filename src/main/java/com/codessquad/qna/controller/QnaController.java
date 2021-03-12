package com.codessquad.qna.controller;

import com.codessquad.qna.domain.Question;
import com.codessquad.qna.domain.User;
import com.codessquad.qna.exception.DoNotAccessException;
import com.codessquad.qna.service.AnswerService;
import com.codessquad.qna.service.QnaService;
import com.codessquad.qna.util.HttpSessionUtils;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/questions")
public class QnaController {

    private final QnaService qnaService;
    private final AnswerService answerService;

    public QnaController(QnaService qnaService, AnswerService answerService) {
        this.qnaService = qnaService;
        this.answerService = answerService;
    }

    @PostMapping
    public String createQuestion(Question question, HttpSession session) {
        if (!HttpSessionUtils.isLoginUser(session)) {
            return "redirect:/";
        }

        User sessionUser = HttpSessionUtils.getUserFromSession(session);
        question.setWriter(sessionUser);
        System.out.println(question.getWriter());

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
        model.addAttribute("answers", answerService.findAll());
        return "/qna/show";
    }

    @GetMapping("/{id}/form")
    public String updateQuestionForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!HttpSessionUtils.isLoginUser(session)) {
            return "redirect:/users/login";
        }

        User sessionUser = HttpSessionUtils.getUserFromSession(session);
        Question question = qnaService.findQuestionById(id);
        User user = question.getWriter();

        if (!sessionUser.isMatchingId(user.getId())) {
            throw new DoNotAccessException();
        }

        model.addAttribute("question", question);

        return "/qna/updateForm";
    }

    @PutMapping("/{id}")
    public String updateQuestion(@PathVariable Long id, Question newQuestion, HttpSession session) {
        if (!HttpSessionUtils.isLoginUser(session)) {
            return "redirect:/users/login";
        }

        User sessionUser = HttpSessionUtils.getUserFromSession(session);
        Question question = qnaService.findQuestionById(id);
        User user = question.getWriter();

        if (!sessionUser.isMatchingId(user.getId())) {
            throw new DoNotAccessException();
        }

        qnaService.updateQuestionData(question, newQuestion);

        return "redirect:/questions/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteQuestion(@PathVariable Long id, HttpSession session) {
        if (!HttpSessionUtils.isLoginUser(session)) {
            return "redirect:/users/login";
        }

        User sessionUser = HttpSessionUtils.getUserFromSession(session);
        Question question = qnaService.findQuestionById(id);
        User user = question.getWriter();

        if (!sessionUser.isMatchingId(user.getId())) {
            throw new DoNotAccessException();
        }

        qnaService.delete(question);
        return "redirect:/";
    }

}
