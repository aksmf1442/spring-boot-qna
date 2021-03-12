package com.codessquad.qna.service;

import com.codessquad.qna.domain.Answer;
import com.codessquad.qna.domain.Question;
import com.codessquad.qna.repository.AnswerRepository;
import com.codessquad.qna.repository.QuestionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    AnswerRepository answerRepository;
    QuestionRepository questionRepository;

    public AnswerService(AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }


    public void save(Answer answer) {
        answerRepository.save(answer);
    }

    public List<Answer> findAll() {
        return answerRepository.findAll();
    }

    public Question findQuestionByQuestionId(Long questionId) {
        return questionRepository.findById(questionId).orElse(null);
    }

    public Answer findById(Long answerId) {
        return answerRepository.findById(answerId).orElse(null);
    }

    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }

    public void update(Answer answer, String contents) {
        answer.setContents(contents);
        answerRepository.save(answer);
    }
}
