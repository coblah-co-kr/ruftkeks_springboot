package com.example.ruftkeks_java_spring.mypage;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.ruftkeks_java_spring.account.Account;
import com.example.ruftkeks_java_spring.account.AccountRepository;
import com.example.ruftkeks_java_spring.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/api/my-page")
public class MyPageController {

    @Autowired
    private AccountRepository accountRepository;

    private final AccountService accountService;

    @Autowired
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity updateMyBaseInfo(@RequestHeader HttpHeaders headers, @RequestBody MyInfo myInfo) {
        String token = headers.getFirst("Authorization");
        Optional<Account> account = this.accountService.getMyInfo(token);
        if (!account.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Account account1 = account.get();
        account1.updateBase(
                myInfo.getEmail(), myInfo.getAddress(),
                myInfo.getPhone(), myInfo.getLinks(),
                myInfo.getLongitude(), myInfo.getLatitude()
        );
        this.accountRepository.save(account1);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/update/profile_img", method = RequestMethod.PUT)
    public ResponseEntity updateProfileImg(
            @RequestHeader HttpHeaders headers,
            @RequestParam("profileImg") MultipartFile file,
            @RequestParam("name") String name
    ) {
        String token = headers.getFirst("Authorization");
        Optional<Account> account = this.accountService.getMyInfo(token);
        if (!account.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Account account1 = account.get();
        if (name == "") {
            return new ResponseEntity<>("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
        String fileName = "profile_img/" + name;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        try {
            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        account1.updateProfileImg(fileName);
        this.accountRepository.save(account1);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/update/overview_img", method = RequestMethod.PUT)
    public ResponseEntity updateOverviewImg(
            @RequestHeader HttpHeaders headers,
            @RequestParam("overviewImg") MultipartFile file,
            @RequestParam("name") String name
            ) {
        String token = headers.getFirst("Authorization");
        Optional<Account> account = this.accountService.getMyInfo(token);
        if (!account.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Account account1 = account.get();
        if (name == "") {
            return new ResponseEntity<>("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
        String fileName = "overview_img/" + name;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        try {
            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        account1.updateOverviewImg(fileName);
        this.accountRepository.save(account1);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
