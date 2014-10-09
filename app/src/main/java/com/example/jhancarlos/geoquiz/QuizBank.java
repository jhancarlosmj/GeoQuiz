package com.example.jhancarlos.geoquiz;

import java.util.HashMap;
import java.util.Map;

public class QuizBank {

    private static Map<String, TrueFalse[]> mapQuiz = new HashMap<String, TrueFalse[]>();

    public static void addQuiz (String nameQuiz, TrueFalse[] quiz) {
        mapQuiz.put(nameQuiz, quiz);
    }

    public static TrueFalse[] getQuiz(String nameQuiz) {
        return mapQuiz.get(nameQuiz);
    }
}
