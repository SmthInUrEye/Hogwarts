package ru.hogwarts.school.repositories;

import org.hibernate.query.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Avatar;

import java.awt.print.Pageable;
import java.util.Collection;
import java.util.List;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    public Avatar findByStudentId(Long studentId);

    public List<Avatar> findAll();
}
