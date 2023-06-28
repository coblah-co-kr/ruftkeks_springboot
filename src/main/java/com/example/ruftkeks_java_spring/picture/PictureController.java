package com.example.ruftkeks_java_spring.picture;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/picture")
@RequiredArgsConstructor
public class PictureController {

    @Autowired
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    private PictureRepository pictureRepository;

    private final PictureService pictureService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity uploadImg(
            @RequestParam("image") List<MultipartFile> files,
            @RequestParam("name") String name,
            @RequestParam("profileImg") String profileImg,
            @RequestParam("date") String date,
            @RequestParam("location") String location,
            @RequestParam("pictureContent") String pictureContent,
            @RequestParam("hashTag") List<String> hashTag
    ) {
        try {
            List<String> pictureList = new ArrayList<>();
            for (MultipartFile file: files) {
                String fileName = file.getOriginalFilename();
                String fileUrl = "https://" + bucket + "/test" + fileName;
                pictureList.add(fileName);
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());
                amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
            }

            pictureService.create(profileImg, name, date, location, pictureContent, hashTag, pictureList);
            return new ResponseEntity("저장되었습니다.", HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity getPictureInfo(

    ) {
        return new ResponseEntity(pictureService.getAllPictureInfo(), HttpStatus.OK);
    }

    @RequestMapping(value = "/get/image", method = RequestMethod.GET)
    public ResponseEntity getPicture(
        @RequestParam Long id
    ) {
        Optional<Picture> picture = pictureService.getById(id);
        List<String> pictureList = new ArrayList<>();
        if (!picture.isPresent()) {
            return new ResponseEntity("해당 데이터가 존재하지 않습니다.", HttpStatus.NO_CONTENT);
        }
        for (String pic:picture.get().getPictureList()) {
            URL url = amazonS3Client.getUrl(bucket, pic);
            String urlText = ""+url;
            pictureList.add(urlText);
        }
        return new ResponseEntity(pictureList, HttpStatus.OK);
    }

    @RequestMapping(value = "/comments", method = RequestMethod.PUT)
    public ResponseEntity updateComment(
            @RequestBody PictureImg pictureImg
    ) {
        if (pictureService.updateComment(pictureImg.getId(), pictureImg.getComments())) {
            return new ResponseEntity("저장되었습니다.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity("해당 스레드를 찾을 수 없습니다.", HttpStatus.NO_CONTENT);
        }
    }
}
