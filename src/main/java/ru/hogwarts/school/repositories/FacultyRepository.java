package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    public Collection<Faculty> findByNameIgnoreCaseContainingOrColorIgnoreCaseContaining(String name, String color);

    public Collection<Faculty> findByColorIgnoreCaseContaining(String color);

    public List<Faculty> findAll() ;

}
