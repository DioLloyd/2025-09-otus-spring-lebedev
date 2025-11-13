package ru.otus.hw.shell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestService;

import static org.mockito.Mockito.*;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
@DisplayName("Application commands tests")
public class ApplicationCommandsTest {

    @Autowired
    private ApplicationCommands applicationCommands;
    @MockitoBean
    private StudentService studentService;
    @MockitoBean
    private TestService testService;
    @MockitoBean
    private ResultService resultService;

    @Test
    @DisplayName("Should execute start command")
    void shouldExecuteStartCommand() {
        Student student = new Student("Ivan", "Ivanov");
        TestResult testResult = new TestResult(student);
        when(studentService.determineCurrentStudent()).thenReturn(student);
        when(testService.executeTestFor(student)).thenReturn(testResult);
        applicationCommands.startTesting();
        verify(studentService).determineCurrentStudent();
        verify(testService).executeTestFor(student);
        verify(resultService).showResult(testResult);
    }

}
