package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestService;

@ShellComponent(value = "Application commands")
@RequiredArgsConstructor
public class ApplicationCommands {

    private final StudentService studentService;

    private final TestService testService;

    private final ResultService resultService;

    private final LocalizedIOService ioService;


    @ShellMethod(value = "Start student testing", key = {"start", "test", "run"})
    public void startTesting() {
        try {
            Student student = studentService.determineCurrentStudent();
            TestResult testResult = testService.executeTestFor(student);
            resultService.showResult(testResult);
        } catch (
                QuestionReadException e) {
            ioService.printLineLocalized("TestRunnerService.error.loading.questions");
        }
    }

}
