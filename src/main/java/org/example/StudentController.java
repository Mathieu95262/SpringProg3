package org.example;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/welcome")
    public String welcome(@RequestParam("name") String name) {
        return "Welcome " + name;
    }

    @PostMapping(value = "/students", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String addStudents(@RequestBody List<Student> students) {
        studentService.addAll(students);
        return studentService.getRegisteredStudentsNames();
    }

    @GetMapping(value = "/students", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> listStudents(
            @RequestHeader(name = "Accept", required = false, defaultValue = "text/plain") String accept
    ) {
        // On ne supporte que text/plain (par défaut), comme demandé.
        String normalized = accept == null ? "text/plain" : accept.split(";")[0].trim();
        if (!normalized.equals("text/plain")) {
            return ResponseEntity.badRequest().body("Format non supporté.");
        }

        return ResponseEntity.ok(studentService.getRegisteredStudentsNames());
    }
}

