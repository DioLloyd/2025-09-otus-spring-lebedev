package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CsvQuestionDao Integration Test")
public class CsvQuestionDaoTest {

    @Test
    @DisplayName("Should correctly read questions from a CSV file")
    void shouldReadQuestionsFromCsvFile() {
        TestFileNameProvider fileNameProvider = () -> "test-questions.csv";
        CsvQuestionDao dao = new CsvQuestionDao(fileNameProvider);
        List<Question> questions = dao.findAll();
        assertThat(questions).hasSize(2);
        Question firstQuestion = questions.get(0);
        assertThat(firstQuestion.text()).isEqualTo("Who wrote The Brothers Karamazov?");
        assertThat(firstQuestion.answers()).hasSize(4);
        Answer firstAnswer = firstQuestion.answers().get(0);
        assertThat(firstAnswer.text()).isEqualTo("Fyodor Mikhailovich Dostoevsky");
        assertThat(firstAnswer.isCorrect()).isTrue();
        Answer secondAnswer = firstQuestion.answers().get(1);
        assertThat(secondAnswer.text()).isEqualTo("Alexander Sergeevich Pushkin");
        assertThat(secondAnswer.isCorrect()).isFalse();
        Question secondQuestion = questions.get(1);
        assertThat(secondQuestion.text()).isEqualTo("At what traffic light should I cross the road?");
        assertThat(secondQuestion.answers()).hasSize(3);
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
