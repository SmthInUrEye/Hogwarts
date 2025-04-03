package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() throws Exception {
        Assertions
          .assertThat(studentController).isNotNull();
    }

    @Test
    public void testDefaultMessage() throws Exception {
        Assertions
          .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/", String.class))
          .isEqualTo("Hello Spring!");
    }


    @Test
    public void testPostStudent() throws Exception {
        Student student = new Student();
        student.setName("Тестировщик");
        student.setAge(32);

        Assertions
          .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/student", student, String.class))
          .isNotNull();

    }

    @Test
    public void testGetStudents() throws Exception {

        Assertions
          .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student", String.class))
          .isNotNull().contains("Тестировщик", "32");
    }

    @Test
    public void testEditStudentAndGetStudentInfo() throws Exception {
        Student originalStudent = new Student();
        originalStudent.setName("Исходное имя");
        originalStudent.setAge(20);

        Student savedStudent = restTemplate.postForObject(
          "http://localhost:" + port + "/student",
          originalStudent,
          Student.class);

        Student updatedStudent = new Student();
        updatedStudent.setId(savedStudent.getId());
        updatedStudent.setName("Замена тестировщику");
        updatedStudent.setAge(15);

        restTemplate.put(
          "http://localhost:" + port + "/student",
          updatedStudent, String.class);

        Student result = restTemplate.getForObject(
          "http://localhost:" + port + "/student/" + savedStudent.getId(),
          Student.class);

        assertThat(result)
          .isNotNull()
          .extracting(
            Student::getId,
            Student::getName,
            Student::getAge)
          .containsExactly(
            savedStudent.getId(),
            "Замена тестировщику",
            15);
    }

    @Test
    public void testDeleteStudent() throws Exception {
        restTemplate.delete("http://localhost:" + port + "/student/8");

        Assertions
          .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/8", String.class))
          .isNullOrEmpty();
    }

    @Test
    public void testGetAllStudents() throws Exception {
        Student student1 = new Student();
        student1.setName("Гермиона");
        student1.setAge(25);
        restTemplate.postForObject("http://localhost:" + port + "/student", student1, String.class);

        Student student2 = new Student();
        student2.setName("Малфой");
        student2.setAge(25);
        restTemplate.postForObject("http://localhost:" + port + "/student", student2, String.class);

        Assertions
          .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student", String.class))
          .isNotNull()
          .contains(student1.getName())
          .contains(student2.getName());
    }

    @Test
    public void testGetStudentByAge() throws Exception {
        Student student1 = new Student();
        student1.setName("Гермиона");
        student1.setAge(25);
        restTemplate.postForObject("http://localhost:" + port + "/student", student1, String.class);

        Student student2 = new Student();
        student2.setName("Малфой");
        student2.setAge(25);
        restTemplate.postForObject("http://localhost:" + port + "/student", student2, String.class);

        Assertions
          .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/by-age/" + student1.getAge(), String.class))
          .isNotNull()
          .contains(student1.getName())
          .contains(student2.getName());

    }

    @Test
    public void testGetStudentByAgeBetweenMinAndMax() throws Exception {
        Student student1 = new Student();
        student1.setName("Гермиона");
        student1.setAge(21);
        restTemplate.postForObject("http://localhost:" + port + "/student", student1, String.class);

        Student student2 = new Student();
        student2.setName("Малфой");
        student2.setAge(29);
        restTemplate.postForObject("http://localhost:" + port + "/student", student2, String.class);

        Assertions
          .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/between-age/" + "20" + "/" + "30", String.class))
          .isNotNull()
          .contains(student1.getName())
          .contains(student2.getName());
    }

    @Test
    public void testGetStudentFaculty() throws Exception {

        Faculty faculty = new Faculty();
        faculty.setId(4L);
        faculty.setName("Грифиндор");

        Student student = new Student();
        student.setName("Гермиона");
        student.setAge(21);

        Faculty savedFaculty = restTemplate.postForObject(
          "http://localhost:" + port + "/faculty",
          faculty,
          Faculty.class);

        Student savedStudent = restTemplate.postForObject(
          "http://localhost:" + port + "/student",
          student,
          Student.class);

        restTemplate.put(
          "http://localhost:" + port + "/student/" + savedStudent.getId() + "/assign/" + savedFaculty.getId(),
          null);

        Faculty studentFaculty = restTemplate.getForObject(
          "http://localhost:" + port + "/student/" + savedStudent.getId() + "/faculty",
          Faculty.class);

        assertThat(studentFaculty)
          .isNotNull()
          .extracting(Faculty::getName)
          .isEqualTo("Грифиндор");
    }
    
}
