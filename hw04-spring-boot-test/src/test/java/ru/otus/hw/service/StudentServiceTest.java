package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.domain.Student;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
@DisplayName("Student service tests")
public class StudentServiceTest {

    @MockitoBean
    private LocalizedIOService ioService;
    @Autowired
    private StudentService studentService;

    @Test
    @DisplayName("Should determine current student")
    void shouldDetermineCurrentStudent() {
        when(ioService.readStringWithPromptLocalized("StudentService.input.first.name"))
                .thenReturn("Ivan");
        when(ioService.readStringWithPromptLocalized("StudentService.input.last.name"))
                .thenReturn("Ivanov");
        Student student = studentService.determineCurrentStudent();
        assertThat(student.firstName()).isEqualTo("Ivan");
        assertThat(student.lastName()).isEqualTo("Ivanov");
        verify(ioService).readStringWithPromptLocalized("StudentService.input.first.name");
        verify(ioService).readStringWithPromptLocalized("StudentService.input.last.name");
    }
}
