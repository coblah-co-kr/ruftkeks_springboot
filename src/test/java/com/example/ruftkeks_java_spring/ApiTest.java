package com.example.ruftkeks_java_spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;


@SpringBootTest
public class ApiTest {

    @Test
    void getApi() throws URISyntaxException {
        // API 엔드포인트 URL
        String url = "https://coblah.atlassian.net/rest/agile/1.0/board/8/backlog";

        // Basic 인증 정보
        String username = "grant@coblah.co.kr";
        String password = "Bearer ATATT3xFfGF0heifJ6VIKya5Go4f6juuFHMOCeVAoianqzX7icDPRo5pChYUBAZUd7nF8gag8W3s_COl1zlbZZ0kEGOvNlmn2TPVdd0DRKRgXRGan0Va7m5SUFQ4LDRRqNz_gBJAGV8gdx586bnp69fdmuJ4opl6VoDKXNw3L_lKae5N8Tu1fOQ=9E6920F6";

        // Basic 인증 헤더 생성
        String authHeader = createBasicAuthHeader(username, password);

        // RestTemplate 인스턴스 생성
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authHeader);

        // 요청 엔티티 생성
        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, new URI(url));

        // API 호출
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        // 응답 처리
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String responseBody = responseEntity.getBody();
            System.out.println("API 응답: " + responseBody);
        } else {
            System.out.println("API 호출 실패: " + responseEntity.getStatusCode());
        }
    }

    private static String createBasicAuthHeader(String username, String password) {
        String credentials = username + ":" + password;
        String encodeCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        return "Basic " + encodeCredentials;
    }


}
