package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questions = questionDao.findAll();
        printQuestions(questions);
    }

    private void printQuestions(List<Question> questions) {
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            ioService.printFormattedLine("%d. %s", i + 1, question.text());
            List<Answer> answers = question.answers();
            printAnswers(answers);
            ioService.printLine("");
        }
    }

    private void printAnswers(List<Answer> answers) {
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            ioService.printFormattedLine("   %d) %s", i + 1, answer.text());
        }
    }

}
