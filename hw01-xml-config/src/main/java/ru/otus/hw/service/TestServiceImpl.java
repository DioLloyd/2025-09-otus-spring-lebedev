package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

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
        String formattedQuestions = convertQuestionsToString(questions);
        ioService.printLine(formattedQuestions);
    }

    private String convertQuestionsToString(List<Question> questions) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            sb.append(String.format("%d. %s%n", i + 1, question.text()));
            List<Answer> answers = question.answers();
            for (int j = 0; j < answers.size(); j++) {
                Answer answer = answers.get(j);
                sb.append(String.format("   %d) %s%n", j + 1, answer.text()));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
