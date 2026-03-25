package TD4;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
public class StudentController {

    private final StudentService studentService;
    private final StudentValidator studentValidator;

    public StudentController(StudentService studentService, StudentValidator studentValidator) {
        this.studentService = studentService;
        this.studentValidator = studentValidator;
    }

    @GetMapping("/welcome")
    public ResponseEntity<String> welcome(@RequestParam(value = "name", required = false) String name) {
        try {
            if (name == null || name.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            return ResponseEntity.ok("Welcome " + name);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(
            value = "/students",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> addStudents(@RequestBody List<Student> students) {
        try {
            // SRP : la validation n'est pas dans le controller.
            studentValidator.validate(students);
            studentService.addAll(students);
            return ResponseEntity.status(HttpStatus.CREATED).body(studentService.getAll());
        } catch (BadRequestException e) {
            // SRP : le Validator lève l'exception, le controller la transforme en ResponseEntity.
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/students")
    public ResponseEntity<?> listStudents(@RequestHeader(name = "Accept", required = false) String accept) {
        try {
            if (accept == null || accept.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            String normalized = accept.split(",")[0].trim();
            normalized = normalized.split(";")[0].trim().toLowerCase(Locale.ROOT);

            if (normalized.equals(MediaType.TEXT_PLAIN_VALUE)) {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(studentService.getRegisteredStudentsNames());
            }

            if (normalized.equals(MediaType.APPLICATION_JSON_VALUE)) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(studentService.getAll());
            }

            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Format non supporté.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

