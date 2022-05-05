package com.gukbit.service;

import com.gukbit.domain.UploadFile;
import com.gukbit.repository.UploadFileRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ImageService {

    @Autowired
    UploadFileRepository uploadFileRepository;

    private final Path rootLocation; // d:/image/

    public ImageService(String uploadPath) {
        this.rootLocation = Paths.get(uploadPath);
        System.out.println(rootLocation.toString());
    }

    public UploadFile store(MultipartFile file) throws Exception {
        //		 fileName : 예류2.jpg
        //		 filePath : d:/images/uuid-예류2.jpg
        //		 saveFileName : uuid-예류2.png
        //		 contentType : image/jpeg
        //		 size : 4994942
        //		 registerDate : 2020-02-06 22:29:57.748
        try {
            if(file.isEmpty()) {
                throw new Exception("Failed to store empty file " + file.getOriginalFilename());
            }

            String saveFileName = fileSave(rootLocation.toString(), file);
            UploadFile saveFile = new UploadFile();
            saveFile.setFileName(file.getOriginalFilename());
            saveFile.setSaveFileName(saveFileName);
            saveFile.setContentType(file.getContentType());
            saveFile.setSize(file.getResource().contentLength());
            saveFile.setRegisterDate(LocalDateTime.now());
            saveFile.setFilePath(rootLocation.toString().replace(File.separatorChar, '/') +'/' + saveFileName);
            uploadFileRepository.save(saveFile);
            return saveFile;

        } catch(IOException e) {
            throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
        }


    }

    public UploadFile load(Long fileId) {
        return uploadFileRepository.findById(fileId).get();
    }

    public String fileSave(String rootLocation, MultipartFile file) throws IOException {
        File uploadDir = new File(rootLocation);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // saveFileName 생성
        UUID uuid = UUID.randomUUID();
        String saveFileName = uuid.toString() + file.getOriginalFilename();
        File saveFile = new File(rootLocation, saveFileName);
        FileCopyUtils.copy(file.getBytes(), saveFile);

        return saveFileName;
    }



}
