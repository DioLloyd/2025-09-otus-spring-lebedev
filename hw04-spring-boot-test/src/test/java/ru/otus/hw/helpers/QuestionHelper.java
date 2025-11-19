package ru.otus.hw.helpers;

import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

public class QuestionHelper {

    public static List<Question> getQuestions() {
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

}
