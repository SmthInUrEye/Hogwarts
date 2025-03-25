package ru.hogwarts.school.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class FacultyService {
  private Map<Long, Faculty> facultyStorage = new HashMap<>();
  private long facultyCount = 0;

  public Faculty createFaculty(Faculty faculty) {
    faculty.setId(++facultyCount);
    facultyStorage.put(facultyCount, faculty);
    return (faculty);
  }

  public Faculty findFaculty(Long id) {
    return facultyStorage.get(id);
  }

  public Faculty deleteFaculty(Long id) {
    return (facultyStorage.remove(id));
  }

  public Faculty editFaculty(Faculty faculty) {
    if (facultyStorage.containsKey(faculty.getId())) {
      facultyStorage.put(faculty.getId(), faculty);
      return faculty;
    }
    return null;
  }

  public Collection<Faculty> getAllFaculties() {
    return facultyStorage.values();
  }
}

