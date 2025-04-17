package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class StudentService {
    Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final FacultyService facultyService;
    private final StudentRepository studentRepository;

    public StudentService(FacultyService facultyService, StudentRepository studentRepository) {
        this.facultyService = facultyService;
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Was invoked method for finding student");
        return studentRepository.findById(id).orElse(null);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student");
        studentRepository.deleteById(id);
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        return studentRepository.save(student);
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for find all students");
        Collection<Student> allStudents;
        allStudents = studentRepository.findAll();
        if (allStudents.isEmpty()) {
            logger.warn("No students found");
        }
        return allStudents;
    }

    public Collection<Student> getAllStudentsByAge(int age) {
        logger.info("Was invoked method for find all students by age");
        Collection<Student> allStudents;

        allStudents = studentRepository
         .findAll()
         .stream()
         .peek(student -> logger.debug("Checking student: {}, age: {}", student.getName(), student.getAge()))
         .filter(student -> student.getAge() == age)
         .toList();

        if (allStudents.isEmpty()) {
            logger.warn("No students found with age {}", age);
        }
        return allStudents;
    }

    public Collection<Student> getStudentsByAgeBetween(int min, int max) {
        logger.info("Was invoked method for find all students between ages");
        return studentRepository.findByAgeBetween(min, max);
    }

    @Transactional(readOnly = true)
    public Optional<Faculty> getStudentFaculty(Long id) {
        logger.info("Was invoked method for get student faculty");
        return studentRepository
         .findById(id)
         .map(Student::getFaculty);
    }

    @Transactional
    public Student assignStudentToFaculty(Long studentId, Long facultyId) {
        logger.info("Was invoked method for assign student to faculty");
        Student student = studentRepository.findById(studentId).orElseThrow(() -> {
            logger.error("Student was not found");
            return new NullPointerException();
        });

        Faculty faculty = facultyService.findFaculty(facultyId);
        student.setFaculty(faculty);

        return studentRepository.save(student);
    }

    public int getAllStudentsAmount() {
        logger.info("Was invoked method for get amount of students");
        return studentRepository.getAllStudentsAmount();
    }

    public int getAverageStudentsAge() {
        logger.info("Was invoked method for get students average age");
        return studentRepository.getAverageStudentsAge();
    }

    public Collection<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last 5 added students");
        return studentRepository.getLastFiveStudents();
    }
}


