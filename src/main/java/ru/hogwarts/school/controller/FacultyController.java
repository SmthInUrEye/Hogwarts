package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable Long id) {
        Faculty foundFaculty = facultyService.findFaculty(id);
        if (foundFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.editFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> getAllFaculties() {
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }

    @GetMapping("/by-color/{color}")
    public ResponseEntity<Collection<Faculty>> getAllFacultiesByColor(@RequestParam String color) {
        return ResponseEntity.ok(facultyService.getAllFacultiesByColor(color));
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<Faculty>> getAllFacultiesByNameOrColor(@RequestParam String query) {
        return ResponseEntity.ok(facultyService.getAllFacultiesByNameOrColor(query));
    }

    @GetMapping("/getStudents/{id}")
    public ResponseEntity<Collection<Student>> getAllStudentsInFaculty(@RequestParam Long facultyId) {
        return ResponseEntity.ok(facultyService.getAllStudentsInFaculty(facultyId));
    }
}
