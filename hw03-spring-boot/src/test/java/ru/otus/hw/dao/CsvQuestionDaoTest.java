package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.helpers.QuestionHelper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CsvQuestionDao Integration Test")
public class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider fileNameProvider;

    @Test
    @DisplayName("Should correctly read questions from a CSV file")
    void shouldReadQuestionsFromCsvFile() {
        when(fileNameProvider.getTestFileName()).thenReturn("test-questions.csv");
        CsvQuestionDao dao = new CsvQuestionDao(fileNameProvider);
        List<Question> expectedQuestions = QuestionHelper.getQuestions();
        List<Question> questions = dao.findAll();
        assertThat(questions)
                .usingRecursiveComparison()
                .isEqualTo(expectedQuestions);
    }

    @Test
    @DisplayName("Should throw exception when file not found")
    void shouldThrowExceptionWhenFileNotFound() {
        TestFileNameProvider fileNameProvider = () -> "non-existent-file.csv";
        CsvQuestionDao dao = new CsvQuestionDao(fileNameProvider);
        assertThatThrownBy(dao::findAll)
                .isInstanceOf(QuestionReadException.class)
                .hasMessageContaining("File not found");
    }

}
