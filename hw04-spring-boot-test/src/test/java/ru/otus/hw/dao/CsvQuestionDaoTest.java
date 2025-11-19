package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.helpers.QuestionHelper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false", classes = CsvQuestionDao.class)
@DisplayName("CsvQuestionDao Integration Test")
public class CsvQuestionDaoTest {

    @MockitoBean
    private TestFileNameProvider fileNameProvider;
    @Autowired
    private CsvQuestionDao csvQuestionDao;


    @Test
    @DisplayName("Should correctly read questions from a CSV file")
    void shouldReadQuestionsFromCsvFile() {
        when(fileNameProvider.getTestFileName()).thenReturn("test-questions.csv");
        List<Question> expectedQuestions = QuestionHelper.getQuestions();
        List<Question> questions = csvQuestionDao.findAll();
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
