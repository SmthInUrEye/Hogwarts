package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    public Collection<Student> findByAgeBetween(int max, int min);

    public Collection<Student> findStudentsByFacultyId(Long facultyId);

    @Query(value = "select count(*) from student", nativeQuery = true)
    public int getAllStudentsAmount();

    @Query(value = "select avg(age) from student", nativeQuery = true)
    public int getAverageStudentsAge();

    @Query(value = "SELECT * FROM student order by id DESC limit 5", nativeQuery = true)
    public Collection<Student> getLastFiveStudents();

    public List<Student> findAll() ;
}
