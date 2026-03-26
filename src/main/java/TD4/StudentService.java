package TD4;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final List<Student> students = new ArrayList<>();

    public synchronized void addAll(List<Student> newStudents) {
        if (newStudents == null || newStudents.isEmpty()) {
            return;
        }
        students.addAll(newStudents);
    }

    public synchronized List<Student> getAll() {
        return new ArrayList<>(students);
    }

    public synchronized String getRegisteredStudentsNames() {
        return students.stream()
                .map(s -> s.firstName() + " " + s.lastName())
                .collect(Collectors.joining(", "));
    }
}

