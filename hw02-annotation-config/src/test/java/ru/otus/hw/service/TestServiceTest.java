package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.helpers.QuestionHelper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test service tests")
public class TestServiceTest {

    @Mock
    private IOService ioService;
    @Mock
    private QuestionDao questionDao;
    @InjectMocks
    private TestServiceImpl testService;


    @Test
    @DisplayName("Should execute testing for student")
    void shouldExecuteTestingForStudent() {
        Student student = new Student("Ivan", "Ivanov");
        List<Question> questions = QuestionHelper.getQuestions();
        when(questionDao.findAll()).thenReturn(questions);
        when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(1)
                .thenReturn(4);
        TestResult testResult = testService.executeTestFor(student);
        verify(ioService, atLeast(1)).printLine("");
        verify(ioService, times(1)).printFormattedLine("Please answer the questions below%n");
        for (Question question : questions) {
            var answers = question.answers();
            verify(ioService, times(1)).printFormattedLine("Question: %s", question.text());
            for (int k = 0; k < answers.size(); k++) {
                verify(ioService, times(1))
                        .printFormattedLine("   %d. %s", k + 1, answers.get(k).text());
            }
        }
        verify(ioService, times(2))
                .readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString());
        assertThat(testResult.getRightAnswersCount()).isEqualTo(2);
        assertThat(testResult.getAnsweredQuestions()).hasSize(2);
        assertThat(testResult.getStudent()).isEqualTo(student);
    }

    @Test
    @DisplayName("Should handle empty question list")
    void shouldHandleEmptyQuestionList() {
        Student student = new Student("Ivan", "Ivanov");
        when(questionDao.findAll()).thenReturn(List.of());
        TestResult testResult = testService.executeTestFor(student);
        assertThat(testResult.getRightAnswersCount()).isEqualTo(0);
        assertThat(testResult.getAnsweredQuestions()).isEmpty();
        assertThat(testResult.getStudent()).isEqualTo(student);
        verify(ioService, times(1)).printLine("");
        verify(ioService, times(1)).printFormattedLine("Please answer the questions below%n");
        verify(questionDao, times(1)).findAll();
        verify(ioService, never()).printFormattedLine(startsWith("Question:"), anyString());
        verify(ioService, never()).printFormattedLine(startsWith("    %d."), anyInt(), anyString());
        verify(ioService, never()).readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString());
    }

    @Test
    @DisplayName("Should handle question read exception")
    void shouldHandleQuestionReadException() {
        Student student = new Student("Ivan", "Ivanov");
        when(questionDao.findAll()).thenThrow(new QuestionReadException("File not found"));
        assertThatThrownBy(() -> testService.executeTestFor(student))
                .isInstanceOf(QuestionReadException.class)
                .hasMessageContaining("File not found");

        verify(ioService).printLine("");
        verify(ioService).printFormattedLine("Please answer the questions below%n");
        verify(ioService, never()).printFormattedLine(startsWith("Question:"), anyString());
        verify(ioService, never()).printFormattedLine(startsWith("   %d."), anyInt(), anyString());
        verify(ioService, never()).readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString());
    }

}
