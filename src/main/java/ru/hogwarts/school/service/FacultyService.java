package ru.hogwarts.school.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;
import java.util.Collections;

@Transactional
@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Collection<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> getAllFacultiesByColor(String color) {
        return facultyRepository.findByColorIgnoreCaseContaining(color);
    }

    public Collection<Faculty> getAllFacultiesByNameOrColor(String query) {
        return facultyRepository.findByNameIgnoreCaseContainingOrColorIgnoreCaseContaining(query, query);
    }

    public Collection<Student> getAllStudentsInFaculty(Long facultyId) {
        return facultyRepository
          .findById(facultyId)
          .map(Faculty::getStudents)
          .orElse(Collections.emptyList());
    }

}

