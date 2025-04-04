package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    String localhost = "http://localhost:";

    @Test
    public void contextLoads() throws Exception {
        assertThat(facultyController).isNotNull();
    }

    @Test
    public void testPostFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Тестировщики");
        faculty.setColor("Green");

        assertThat(this.restTemplate.postForObject(localhost + port + "/faculty", faculty, String.class))
         .isNotNull();
    }

    @Test
    public void testGetFaculties() throws Exception {
        assertThat(this.restTemplate.getForObject(localhost + port + "/faculty", String.class))
         .isNotNull()
         .contains("Тестировщики", "Green");
    }

    @Test
    public void testEditFacultyAndFacultyInfo() throws Exception {

        Faculty originalFaculty = new Faculty("Когтеврань", "Жёлтый");
        Faculty savedFaculty = restTemplate.postForObject(localhost + port + "/faculty", originalFaculty, Faculty.class);

        Faculty updatedFaculty = new Faculty("Замена когтевраню", "Белый");
        updatedFaculty.setId(savedFaculty.getId());
        restTemplate.put(localhost + port + "/faculty", updatedFaculty, String.class);

        Faculty result = restTemplate.getForObject(localhost + port + "/faculty/" + savedFaculty.getId(), Faculty.class);

        assertThat(result)
         .isNotNull()
         .extracting(Faculty::getId,
          Faculty::getName,
          Faculty::getColor)
         .containsExactly(savedFaculty.getId(),
          "Замена когтевраню", "Белый");
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        restTemplate.delete(localhost + port + "/faculty/15");

        Assertions.
         assertThat(this.restTemplate.getForObject(localhost + port + "/faculty/15", String.class))
         .isNullOrEmpty();
    }

    @Test
    public void testGetAllFaculties() throws Exception {
        Faculty faculty1 = new Faculty("Слизерин", "Зеленый");
        restTemplate.postForObject(localhost + port + "/faculty", faculty1, String.class);

        Faculty faculty2 = new Faculty("Грифондий", "Салатовый");
        restTemplate.postForObject(localhost + port + "/faculty", faculty2, String.class);

        Assertions
         .assertThat(this.restTemplate.getForObject(localhost + port + "/faculty", String.class))
         .isNotNull()
         .contains(faculty1.getName())
         .contains(faculty2.getName());
    }



}
