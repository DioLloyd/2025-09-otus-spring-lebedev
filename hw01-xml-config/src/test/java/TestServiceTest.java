import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.TestServiceImpl;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@DisplayName("Test service tests")
@ExtendWith(MockitoExtension.class)
public class TestServiceTest {

    @Mock
    private IOService ioService;
    @Mock
    private QuestionDao questionDao;
    @InjectMocks
    private TestServiceImpl testService;


    private static Stream<List<Question>> provideQuestions() {
        List<Question> questionList = List.of(
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
        return Stream.of(questionList);
    }

    @ParameterizedTest
    @MethodSource("provideQuestions")
    @DisplayName("Displaying a list of questions and answers")
    void displayQuestionsAndAnswersTest(List<Question> questions) {
        when(questionDao.findAll()).thenReturn(questions);
        testService.executeTest();
        verify(ioService, atLeast(1)).printLine("");
        verify(ioService, times(1)).printFormattedLine("Please answer the questions below%n");
        for (int i = 0; i < questions.size(); i++) {
            var question = questions.get(i);
            var answers = question.answers();
            verify(ioService, times(1))
                    .printFormattedLine("%d. %s", i + 1, question.text());
            for (int j = 0; j < answers.size(); j++) {
                verify(ioService, times(1))
                        .printFormattedLine("   %d) %s", j + 1, answers.get(j).text());
            }
        }
        verify(ioService, times(3)).printLine("");
    }

    @Test
    @DisplayName("Processing an empty list of questions")
    void processingEmptyQuestionList() {
        when(questionDao.findAll()).thenReturn(List.of());
        testService.executeTest();
        verify(ioService, times(1)).printLine("");
        verify(ioService, times(1)).printFormattedLine("Please answer the questions below%n");
        verify(questionDao, times(1)).findAll();
    }

}
