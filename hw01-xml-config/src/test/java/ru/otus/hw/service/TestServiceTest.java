package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Test service tests")
@ExtendWith(MockitoExtension.class)
public class TestServiceTest {

    @Mock
    private IOService ioService;
    @Mock
    private QuestionDao questionDao;
    @InjectMocks
    private TestServiceImpl testService;


    private List<Question> getQuestions() {
        return List.of(
                new Question("Is there life on Mars?", List.of(
                        new Answer("Science doesn't know this yet", true),
                        new Answer("Certainly. The red UFO is from Mars. And green is from Venus", false),
                        new Answer("Absolutely not", false)
                )),
                new Question("How many days are there in a week?", List.of(
                        new Answer("8", false),
                        new Answer("6", false),
                        new Answer("42", false),
                        new Answer("7", true)
                ))
        );
    }

    private String prepareExpectedString(List<Question> questions) {
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

    @Test
    @DisplayName("Displaying a list of questions and answers")
    void displayQuestionsAndAnswersTest() {
        List<Question> questions = getQuestions();
        String expectedString = prepareExpectedString(questions);
        when(questionDao.findAll()).thenReturn(questions);
        testService.executeTest();
        verify(ioService, atLeast(1)).printLine("");
        verify(ioService, times(1)).printFormattedLine("Please answer the questions below%n");
        verify(ioService, times(1)).printLine(expectedString);
    }

    @Test
    @DisplayName("Processing an empty list of questions")
    void processingEmptyQuestionList() {
        when(questionDao.findAll()).thenReturn(List.of());
        testService.executeTest();
        verify(ioService, atLeast(1)).printLine("");
        verify(ioService, times(1)).printFormattedLine("Please answer the questions below%n");
        verify(questionDao, times(1)).findAll();
        verify(ioService, never()).printLine(contains("1."));
        verify(ioService, never()).printLine(contains("   "));
    }

}
