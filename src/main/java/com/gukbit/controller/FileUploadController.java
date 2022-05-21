package com.gukbit.controller;

import com.gukbit.domain.UploadFile;
import com.gukbit.repository.UploadFileRepository;
import com.gukbit.service.ImageService;
import java.net.MalformedURLException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class FileUploadController {
  UploadFileRepository uploadFileRepository;
  @Autowired
  ImageService imageService;

  @Autowired
  ResourceLoader resourceLoader;

  @PostMapping("/image")
  @ResponseBody
  public ResponseEntity<?> imageUpload(@RequestParam("file") MultipartFile file) {
    String rootLocation = "src/main/resources/static/images/board";
    try {
      UploadFile uploadFile = imageService.store(rootLocation, file);
      return ResponseEntity.ok().body("/image/" + uploadFile.getId());
    } catch(Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/image/{fileId}")
  @ResponseBody
  public ResponseEntity<?> loadFile(@PathVariable Long fileId){
    try {
      UploadFile uploadFile = imageService.load(fileId);
      Resource resource = resourceLoader.getResource("file:" + uploadFile.getFilePath());
      return ResponseEntity.ok().body(resource);
    } catch(Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

  @ResponseBody
  @GetMapping("/image/fileName/{saveFileName}")
  public Resource loadFileByFilename(@PathVariable String saveFileName)
      throws MalformedURLException {
      switch (saveFileName) {
        case "1":
          return new UrlResource(
              "file:" + "src/main/resources/static/images/mypage/basic-profile-docker.jpg");
        case "2":
          return new UrlResource(
              "file:" + "src/main/resources/static/images/mypage/basic-profile-github.jpg");
        case "3":
          return new UrlResource(
              "file:" + "src/main/resources/static/images/mypage/basic-profile-mysql.png");
      }
      UploadFile savedFile = imageService.loadByFileName(saveFileName);
      return new UrlResource("file:" + savedFile.getFilePath());
  }

  @RequestMapping("/imageViewer")
  public String loadImageViewer(@RequestParam String imageName, Model model){
    model.addAttribute("imageName", imageName);
    return "view/imageViewer";
  }
}