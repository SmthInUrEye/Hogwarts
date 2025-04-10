package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.engine.spi.CollectionEntry;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @GetMapping(value = "/{studentId}/avatar-from-db")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long studentId) {
        Avatar avatar = avatarService.getAvatarByStudentId(studentId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @Transactional
    @GetMapping(value = "/{studentId}/avatar-from-file")
    public void downloadAvatar(@PathVariable Long studentId, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.getAvatarByStudentId(studentId);
        Path path = Path.of(avatar.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping(value = "/get-all")
    public ResponseEntity<List<Map<String, Object>>> getAllAvatars(
      @RequestParam("page") Integer pageNumber,
      @RequestParam("size") Integer pageSize) {

        Collection<Avatar> avatarDataList = avatarService.getAllAvatarsByPages(pageNumber, pageSize);

        List<Map<String, Object>> result = avatarDataList.stream().map(avatar -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", avatar.getId());
            map.put("path", avatar.getFilePath());
            map.put("imageUrl", "/avatars/" + avatar.getId());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);

    }
}
