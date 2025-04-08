package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentService studentService;

    @Test
    public void testPostStudent() throws Exception {
        Student student = new Student();
        student.setName("Тестировщик");
        student.setAge(32);

        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(student)))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.name").value("Тестировщик"))
         .andExpect(jsonPath("$.age").value(32));
    }

    @Test
    public void testGetStudents() throws Exception {
        Student student1 = new Student("Тестировщик", 32);
        Student student2 = new Student("Василий", 45);

        when(studentService.getAllStudents()).thenReturn(List.of(student1, student2));

        mockMvc.perform(get("/student"))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$[0].name").value("Тестировщик"))
         .andExpect(jsonPath("$[0].age").value(32))
         .andExpect(jsonPath("$[1].name").value("Василий"))
         .andExpect(jsonPath("$[1].age").value(45));

    }

    @Test
    public void testEditStudent() throws Exception {
        Student updatedStudent = new Student("Замена тестировщику", 15);

        when(studentService.editStudent(any(Student.class))).thenReturn(updatedStudent);

        mockMvc.perform(put("/student")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(updatedStudent)))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.name").value("Замена тестировщику"))
         .andExpect(jsonPath("$.age").value(15));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        mockMvc.perform(delete("/student/1"))
         .andExpect(status().isOk());
    }

    @Test
    public void testGetStudentByAge() throws Exception {
        Student student1 = new Student("Гермиона", 25);
        Student student2 = new Student("Малфой", 25);
        when(studentService.getAllStudentsByAge(25)).thenReturn(List.of(student1, student2));

        mockMvc.perform(get("/student/by-age/25"))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$[0].name").value("Гермиона"))
         .andExpect(jsonPath("$[0].age").value(25))
         .andExpect(jsonPath("$[1].name").value("Малфой"))
         .andExpect(jsonPath("$[1].age").value(25));
    }

    @Test
    public void testGetStudentByAgeBetween() throws Exception {
        Student student1 = new Student("Гермиона", 21);
        Student student2 = new Student("Малфой", 29);
        when(studentService.getStudentsByAgeBetween(20, 30)).thenReturn(List.of(student1, student2));

        mockMvc.perform(get("/student/between-age/20/30"))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$[0].name").value("Гермиона"))
         .andExpect(jsonPath("$[0].age").value(21))
         .andExpect(jsonPath("$[1].name").value("Малфой"))
         .andExpect(jsonPath("$[1].age").value(29));
    }

    @Test
    public void testGetStudentFaculty() throws Exception {
        Faculty faculty = new Faculty("Гриффиндор", "Красный");
        when(studentService.getStudentFaculty(1L)).thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/student/1/faculty"))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.name").value("Гриффиндор"))
         .andExpect(jsonPath("$.color").value("Красный"));
    }

    @Test
    public void testAssignStudentToFaculty() throws Exception {
        Faculty faculty = new Faculty("Слизерин", "Зеленый");
        Student student = new Student("Никита", 23);
        faculty.setId(1L);
        student.setId(1L);

        when(studentService.assignStudentToFaculty(1L, 1L)).thenReturn(student);

        mockMvc.perform(put("/student/{studentId}/assign/{facultyId}", student.getId(), faculty.getId()))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.id").value(1L))
         .andExpect(jsonPath("$.name").value("Никита"));

    }

}
