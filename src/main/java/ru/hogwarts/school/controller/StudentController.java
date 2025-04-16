package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.OptionalDouble;
import java.util.stream.IntStream;

@RestController
@RequestMapping("student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student foundStudent = studentService.findStudent(id);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/by-age/{age}")
    public ResponseEntity<Collection<Student>> getAllStudentsByAge(@PathVariable int age) {
        return ResponseEntity.ok(studentService.getAllStudentsByAge(age));
    }

    @GetMapping("/between-age/{min}/{max}")
    public ResponseEntity<Collection<Student>> getAllStudentsBetweenAge(@PathVariable int min, @PathVariable int max) {
        return ResponseEntity.ok(studentService.getStudentsByAgeBetween(min, max));
    }

    @GetMapping("/{id}/faculty")

    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Long id) {
        return studentService
         .getStudentFaculty(id)
         .map(ResponseEntity::ok)
         .orElse(ResponseEntity
          .notFound()
          .build());
    }

    @PutMapping("/{studentId}/assign/{facultyId}")
    public ResponseEntity<Student> assignStudentToFaculty(@PathVariable Long studentId, @PathVariable Long facultyId) {
        return ResponseEntity.ok(studentService.assignStudentToFaculty(studentId, facultyId));
    }

    @GetMapping("/all-students-amount")
    public ResponseEntity<Integer> getAllStudentsAmount() {
        return ResponseEntity.ok(studentService.getAllStudentsAmount());
    }

    @GetMapping("/students-avg-age")
    public ResponseEntity<Integer> getStudentsAverageAge() {
        return ResponseEntity.ok(studentService.getAverageStudentsAge());
    }

    @GetMapping("/last-five-students")
    public ResponseEntity<Collection<Student>> getLastFiveStudents() {
        return ResponseEntity.ok(studentService.getLastFiveStudents());
    }

    @GetMapping("/students-with-A")
    public ResponseEntity<Collection<Student>> getAllStudentsNameStartsWithA() {
        return ResponseEntity.ok(studentService.getAllStudentsNameStartsWithA());
    }

    @GetMapping("/avg-students-age-double")
    public ResponseEntity<OptionalDouble> getAverageStudentsAgeInDouble() {
        return ResponseEntity.ok(studentService.getAverageStudentsAgeInDouble());
    }

    @GetMapping("/try-to-optim")
    public ResponseEntity <Integer> getOptimaFormula() {
        int sum = IntStream.rangeClosed(1, 1_000_000).parallel().sum();
        return ResponseEntity.ok(sum);
    }


}
