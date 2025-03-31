package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.CREATE_NEW;


@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }


    @Transactional
    public Avatar uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = studentService.findStudent(studentId);
        Path filePath = Path.of(avatarsDir, student + "." + StringUtils.getFilenameExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
         InputStream is = file.getInputStream();
         OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
         BufferedInputStream bis = new BufferedInputStream(is, 1024);
         BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }


        Avatar avatar = new Avatar();
        avatar.setFilePath(filePath.toString());
        avatar.setMediaType(file.getContentType());
        avatar.setFileSize(file.getSize());
        avatar.setData(file.getBytes());
        avatar.setStudent(student);

        return avatarRepository.save(avatar);
    }

    public Avatar getAvatarByStudentId(Long studentId) {
        return avatarRepository.findByStudentId(studentId);
    }


}
