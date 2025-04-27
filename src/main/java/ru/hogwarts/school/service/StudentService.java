package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;

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

    public Collection<Student> getAllStudentsNameStartsWithA() {
        List<Student> allStudents = studentRepository.findAll();

        return allStudents
          .parallelStream()
          .filter(student -> student.getName().startsWith("А"))
          .peek(student -> logger.debug("Filtered student: {}, age: {}", student.getName(), student.getAge()))
          .sorted(Comparator.comparing(Student::getName))
          .collect(Collectors.toCollection(ArrayList::new));
    }

    public OptionalDouble getAverageStudentsAgeInDouble() {
        List<Student> allStudents = studentRepository.findAll();

        return allStudents
          .parallelStream()
          .mapToInt(Student::getAge)
          .average();
    }

    public String printParallelStudents() {
        List<Student> allStudents = studentRepository.findAll();

        System.out.println("Main thread: " + Thread.currentThread().getName());
        System.out.println("1:" + allStudents.get(1).getName());
        System.out.println("2:" + allStudents.get(2).getName());

        Thread thread1 = new Thread(() -> {
            System.out.println("Second thread: " + Thread.currentThread().getName());
            System.out.println("3:" + allStudents.get(3).getName());
            System.out.println("4:" + allStudents.get(4).getName());
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("Third thread: " + Thread.currentThread().getName());
            System.out.println("5:" + allStudents.get(5).getName());
            System.out.println("6:" + allStudents.get(6).getName());
        });

        thread1.start();
        thread2.start();

        return "Print parallel is over";
    }

    public synchronized void printStudentName(int number, Student student) {
        System.out.println(number + ": " + student.getName());
    }

    public String printSynchronizedStudents() {
        List<Student> allStudents = studentRepository.findAll();

        System.out.println("Main thread: " + Thread.currentThread().getName());
        printStudentName(1, allStudents.get(1));
        printStudentName(2, allStudents.get(2));

        Thread thread1 = new Thread(() -> {
            System.out.println("Second thread: " + Thread.currentThread().getName());
            printStudentName(3, allStudents.get(3));
            printStudentName(4, allStudents.get(4));
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("Third thread: " + Thread.currentThread().getName());
            printStudentName(5, allStudents.get(5));
            printStudentName(6, allStudents.get(6));
        });

        try {
            thread1.start();
            thread1.join();
            thread2.start();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "Print synchronized is over";
    }

}

