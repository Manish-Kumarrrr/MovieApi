package com.movieflix.movieAPI.controllers;

import com.movieflix.movieAPI.service.FileService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService){
        this.fileService=fileService;
    }

    @Value("${project.poster}")
    private String path;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFileHandler(@RequestPart MultipartFile file)throws IOException{
        String uploadedFileName=fileService.uploadFile(path,file);
        return new ResponseEntity<>("File Uploaded : "+uploadedFileName, HttpStatus.OK);
    }

    @GetMapping("/{fileName}")
    public void serveFileHandler(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        InputStream resourceFile=fileService.getResourceFile(path,fileName);
//        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.setContentType(MediaType.ALL_VALUE);
        StreamUtils.copy(resourceFile,response.getOutputStream());
    }
}
