package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class InfoController {

    @Autowired
    private Environment environment;

    @GetMapping("/port")
    public String getServerPort() {
        String port = environment.getProperty("local.server.port");
        return "Current server port: " + port;
    }

    @GetMapping("/profile")
    public String getActualProfile() {
        String[] profiles = environment.getActiveProfiles();
        return "Current server profiles: " + String.join(", ", profiles);
    }
}

