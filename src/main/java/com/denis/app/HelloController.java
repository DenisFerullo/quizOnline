package com.denis.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Controller
public class HelloController {

    @GetMapping("/quiz")
    public String showQuiz(Model model) {
        try {
            // Legge tutte le righe del file quiz.txt
            List<String> lines = Files.readAllLines(Path.of("quiz.txt"));
            model.addAttribute("questions", lines);
        } catch (IOException e) {
            model.addAttribute("questions", List.of("Errore nel caricamento del quiz"));
        }
        return "quiz";
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "hello";
    }
}
