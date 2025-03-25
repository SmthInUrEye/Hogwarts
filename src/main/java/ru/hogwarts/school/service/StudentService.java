package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {

  private Map<Long, Student> studentStorage = new HashMap<>();
  private long studentCount = 0;

  public Student createStudent(Student student) {
    student.setId(++studentCount);
    studentStorage.put(studentCount, student);
    return (student);
  }

  public Student findStudent(Long id) {
    return studentStorage.get(id);
  }

  public Student deleteStudent(Long id) {
    return (studentStorage.remove(id));
  }

  public Student editStudent(Student student) {
    if (studentStorage.containsKey(student.getId())) {
      studentStorage.put(student.getId(), student);
      return student;
    }
    return (null);
  }

  public Collection<Student> getAllStudents() {
    return studentStorage.values();
  }

  public Collection<Student> getAllStudentsByAge(int age) {
    return studentStorage.values()
            .stream()
            .filter(student -> student.getAge() == age)
            .toList();
  }
}


