package org.example.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserService {

    private final String userDetailsMicroservice = "http://localhost:8071/api/v1/users/";
    private final String userCareerMicroservice = "http://localhost:8072/api/v1/users/";

    private WebClient webClient;

    public Mono<UserDto> getUserById(int id) {

        Mono<UserDetailsDto> userDetails = getUserDetails(id);
        Mono<UserCareerDto> userCareer = getCareerDetails(id);

        return userDetails.zipWith(userCareer)
                .map(tuple -> build(tuple.getT1(), tuple.getT2()));
    }

    private UserDto build(UserDetailsDto user, UserCareerDto career) {
        return UserDto.builder()
                .id(user.id())
                .name(user.name())
                .org(user.org())
                .skills(career.skills())
                .build();
    }

    private Mono<UserDetailsDto> getUserDetails(int id) {
        webClient = WebClient.builder()
                .baseUrl(userDetailsMicroservice)
                .build();

        return webClient.get()
                .uri(id + "")
                .retrieve()
                .bodyToMono(UserDetailsDto.class);
    }

    private Mono<UserCareerDto> getCareerDetails(int id) {
        webClient = WebClient.builder()
                .baseUrl(userCareerMicroservice)
                .build();

        return webClient.get()
                .uri(id + "")
                .retrieve()
                .bodyToMono(UserCareerDto.class);
    }


    @Deprecated
    private UserDetailsDto getUserDetailsUsingRestTemplate(int id) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserDetailsDto> response
                = restTemplate.getForEntity(userDetailsMicroservice + id, UserDetailsDto.class);

        var userDetails = response.getBody();
        log.info("user={}", userDetails);
        return userDetails;
    }

    @Deprecated
    private UserCareerDto getUserCareerUsingRestTemplate(int id) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserCareerDto> response
                = restTemplate.getForEntity(userCareerMicroservice + id, UserCareerDto.class);

        var userCareer = response.getBody();
        log.info("userCareer={}", userCareer);
        return userCareer;
    }

}
