package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Collection<Student> getAllStudentsByAge(int age) {
        return studentRepository
         .findAll()
         .stream()
         .filter(student -> student.getAge() == age)
         .toList();
    }

    public Collection<Student> getStudentsByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    @Transactional(readOnly = true)
    public Optional<Faculty> getStudentFaculty(Long id) {
        return studentRepository
         .findById(id)
         .map(Student::getFaculty);
    }
}


