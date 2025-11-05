package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final IOService ioService;

    @Override
    public Student determineCurrentStudent() {
        var lastName = ioService.readStringWithPrompt("Please input your last name");
        var firstName = ioService.readStringWithPrompt("Please input your first name");
        return new Student(firstName, lastName);
    }
}
