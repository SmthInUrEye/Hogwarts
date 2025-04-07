package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FacultyService facultyService;

    @MockitoBean
    private FacultyRepository facultyRepository;

    @MockitoBean
    private StudentRepository studentRepository;

    @Test
    public void testPostFaculty() throws Exception {
        Faculty faculty = new Faculty("Тестировщики", "Green");

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(faculty)))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.name").value("Тестировщики"))
         .andExpect(jsonPath("$.color").value("Green"));
    }

    @Test
    public void testGetFaculty() throws Exception {
        Faculty faculty = new Faculty("Грифиндор", "Красный");
        faculty.setId(1L);

        when(facultyService.findFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1"))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.id").value(1))
         .andExpect(jsonPath("$.name").value("Грифиндор"))
         .andExpect(jsonPath("$.color").value("Красный"));
    }

    @Test
    public void testEditFaculty() throws Exception {
        Faculty updatedFaculty = new Faculty("Слизерин", "Зеленый");
        updatedFaculty.setId(1L);

        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(updatedFaculty);

        mockMvc.perform(put("/faculty")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(updatedFaculty)))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$.name").value("Слизерин"))
         .andExpect(jsonPath("$.color").value("Зеленый"));
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        mockMvc.perform(delete("/faculty/1"))
         .andExpect(status().isOk());
    }

    @Test
    public void testGetAllFaculties() throws Exception {
        Faculty faculty1 = new Faculty("Гриффиндор", "Красный");
        Faculty faculty2 = new Faculty("Слизерин", "Зеленый");
        faculty1.setId(1L);
        faculty2.setId(2L);

        when(facultyService.getAllFaculties()).thenReturn(List.of(faculty1, faculty2));

        mockMvc.perform(get("/faculty"))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$[0].name").value("Гриффиндор"))
         .andExpect(jsonPath("$[0].color").value("Красный"))
         .andExpect(jsonPath("$[1].name").value("Слизерин"))
         .andExpect(jsonPath("$[1].color").value("Зеленый"));

    }

    @Test
    public void testFacultySearch() throws Exception {
        Faculty faculty = new Faculty("Поиск", "Цвет");
        faculty.setId(1L);

        when(facultyService.getAllFacultiesByNameOrColor(any(String.class))).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty/search").param("query", "цвет"))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$[0].name").value("Поиск"))
         .andExpect(jsonPath("$[0].color").value("Цвет"));
    }

    @Test
    public void testGetStudentsByFacultyId() throws Exception {

        Faculty faculty = new Faculty("Тест", "Цвет");
        Student student1 = new Student("Гермиона", 21);
        Student student2 = new Student("Малфой", 29);
        faculty.setId(1L);
        student1.setId(1L);
        student2.setId(2L);

        when(facultyService.getAllStudentsInFaculty(1L)).thenReturn(List.of(student1, student2));

        mockMvc.perform(get("/faculty/getStudents").param("facultyId", "1"))
         .andExpect(status().isOk())
         .andExpect(jsonPath("$[0].name").value("Гермиона"))
         .andExpect(jsonPath("$[1].name").value("Малфой"))
         .andExpect(jsonPath("$[0].age").value(21))
         .andExpect(jsonPath("$[1].age").value(29));
    }
}





