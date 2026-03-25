package TD4;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Vérifie la présence des attributs obligatoires des objets Student.
 * En cas de problème, lève une {@link BadRequestException}.
 */
@Service
public class StudentValidator {

    private static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    public void validate(List<Student> newStudents) {
        if (newStudents == null) {
            // Hors de la question du SRP mais cohérent: les attributs requis ne peuvent pas exister.
            throw new BadRequestException("NewStudents cannot be null");
        }

        for (Student newStudent : newStudents) {
            if (newStudent == null) {
                throw new BadRequestException("NewStudent cannot be null");
            }

            if (isNullOrBlank(newStudent.reference())) {
                throw new BadRequestException("NewStudent.reference cannot be null");
            }
            if (isNullOrBlank(newStudent.firstName())) {
                throw new BadRequestException("NewStudent.firstName cannot be null");
            }
            if (isNullOrBlank(newStudent.lastName())) {
                throw new BadRequestException("NewStudent.lastName cannot be null");
            }
        }
    }
}

