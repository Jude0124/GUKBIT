package com.gukbit.controller;

import com.gukbit.domain.UploadFile;
import com.gukbit.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {

  @Autowired
  ImageService imageService;

  @Autowired
  ResourceLoader resourceLoader;

  @PostMapping("/image")
  public ResponseEntity<?> imageUpload(@RequestParam("file") MultipartFile file) {
    try {
      UploadFile uploadFile = imageService.store(file);
      return ResponseEntity.ok().body("/image/" + uploadFile.getId());
    } catch(Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/image/{fileId}")
  public ResponseEntity<?> serveFile(@PathVariable Long fileId){
    try {
      UploadFile uploadFile = imageService.load(fileId);
      Resource resource = resourceLoader.getResource("file:" + uploadFile.getFilePath());
      return ResponseEntity.ok().body(resource);
    } catch(Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }

  }
}
