package org.learning.resource.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/resources")
public class ResourceController {

    @PostMapping(consumes = "audio/mpeg")
    public String uploadResource(@RequestParam("file") MultipartFile file) {
        return "";
    }

}
