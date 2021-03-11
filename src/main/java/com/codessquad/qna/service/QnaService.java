package com.codessquad.qna.service;

import com.codessquad.qna.domain.Question;
import com.codessquad.qna.repository.QuestionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class QnaService {

    private final QuestionRepository questionRepository;

    public QnaService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void save(Question question) {
        questionRepository.save(question);
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Question findQuestionById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public void updateQuestionData(Question question, Question newQuestion) {
        question.update(newQuestion);
        questionRepository.save(question);
    }

    public void delete(Question question) {
        questionRepository.deleteById(question.getId());
    }
}
