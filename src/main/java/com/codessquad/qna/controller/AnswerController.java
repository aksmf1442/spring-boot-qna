package com.codessquad.qna.controller;

import com.codessquad.qna.domain.Answer;
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

@Controller
public class AnswerController {

    private final AnswerService answerService;
    private final QnaService qnaService;

    public AnswerController(AnswerService answerService, QnaService qnaService) {
        this.answerService = answerService;
        this.qnaService = qnaService;
    }

    @PostMapping("/questions/{questionId}/answers")
    public String createAnswer(@PathVariable Long questionId, Answer answer, HttpSession session) {
        if (!HttpSessionUtils.isLoginUser(session)) {
            return "redirect:/users/login";
        }

        Question question = answerService.findQuestionByQuestionId(questionId);
        User user = HttpSessionUtils.getUserFromSession(session);
        answer.initSetting(question, user);
        answerService.save(answer);
        return "redirect:/questions/" + questionId;
    }

    @GetMapping("/questions/{questionId}/answers/{answerId}")
    public String updateAnswerForm(@PathVariable Long questionId, @PathVariable Long answerId,
        Model model, HttpSession session) {

        if (!HttpSessionUtils.isLoginUser(session)) {
            return "redirect:/users/login";
        }

        User sessionUser = HttpSessionUtils.getUserFromSession(session);
        Answer answer = answerService.findById(answerId);
        User user = answer.getWriter();

        if (!sessionUser.isMatchingId(user.getId())) {
            throw new DoNotAccessException();
        }

        model.addAttribute("compareId", user.getId());
        model.addAttribute("question", qnaService.findQuestionById(questionId));
        model.addAttribute("answers", answerService.findAll());
        return "/qna/show";
    }

    @PutMapping("/questions/{questionId}/answers/{answerId}")
    public String updateAnswer(@PathVariable Long questionId, @PathVariable Long answerId,
        String contents, HttpSession session) {

        if (!HttpSessionUtils.isLoginUser(session)) {
            return "redirect:/users/login";
        }

        User sessionUser = HttpSessionUtils.getUserFromSession(session);
        Answer answer = answerService.findById(answerId);
        User user = answer.getWriter();

        if (!sessionUser.isMatchingId(user.getId())) {
            throw new DoNotAccessException();
        }

        answerService.update(answer, contents);

        return "redirect:/questions/" + questionId;
    }

    @DeleteMapping("/questions/{questionId}/answers/{answerId}")
    public String deleteAnswer(@PathVariable Long questionId, @PathVariable Long answerId,
        HttpSession session) {

        if (!HttpSessionUtils.isLoginUser(session)) {
            return "redirect:/users/login";
        }

        User sessionUser = HttpSessionUtils.getUserFromSession(session);
        Answer answer = answerService.findById(answerId);
        User user = answer.getWriter();

        if (!sessionUser.isMatchingId(user.getId())) {
            throw new DoNotAccessException();
        }

        answerService.delete(answer);

        return "redirect:/questions/" + questionId;
    }
}
