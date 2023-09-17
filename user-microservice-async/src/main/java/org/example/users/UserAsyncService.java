package org.example.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.AsyncWebRequest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
@Slf4j
public class UserAsyncService {

    private final String userDetailsMicroservice = "http://localhost:8071/api/v1/users/";
    private final String userCareerMicroservice = "http://localhost:8072/api/v1/users/";

    @Async
    public CompletableFuture<UserDetailsDto> getUserDetailsUsingRestTemplate(int id) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserDetailsDto> response
                = restTemplate.getForEntity(userDetailsMicroservice + id, UserDetailsDto.class);

        var userDetails = response.getBody();
        log.info("user={}", userDetails);
        return CompletableFuture.completedFuture(userDetails);
    }

    @Async
    public CompletableFuture<UserCareerDto> getUserCareerUsingRestTemplate(int id) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserCareerDto> response
                = restTemplate.getForEntity(userCareerMicroservice + id, UserCareerDto.class);

        var userCareer = response.getBody();
        log.info("userCareer={}", userCareer);
        return CompletableFuture.completedFuture(userCareer);
    }
}
