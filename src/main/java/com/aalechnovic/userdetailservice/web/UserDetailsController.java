package com.aalechnovic.userdetailservice.web;

import com.aalechnovic.userdetailservice.crypto.EncryptingUserDetailsService;
import com.aalechnovic.userdetailservice.domain.UserDetails;
import com.aalechnovic.userdetailservice.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Web component responsible for processing GET/POST REST API Requests to save
 * and retrieve {@link UserDetails} via {@link UserDetailsResource}
 */
@RestController
@RequestMapping(path = "/api/v1/users-details")
public class UserDetailsController {

    private final EncryptingUserDetailsService encryptingUserDetailsService;

    public UserDetailsController(EncryptingUserDetailsService encryptingUserDetailsService) {
        this.encryptingUserDetailsService = encryptingUserDetailsService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDetailsResource create(@RequestBody UserDetailsResource newUserDetails) {
        var userDetails = getUserDetailsPairFrom(newUserDetails);

        return getResourceFrom(this.encryptingUserDetailsService.save(userDetails));

    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsResource get(@PathVariable Long id) {
        return this.encryptingUserDetailsService.findById(id)
                                                .map(UserDetailsController::getResourceFrom)
                                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                                                               "User details for id: [" + id + "] does not exist"));

    }

    protected static UserDetailsResource getResourceFrom(Pair<Long, UserDetails> userDetailsPair) {
        UserDetails userDetails = userDetailsPair.getSecond();
        return new UserDetailsResource(userDetailsPair.getFirst(),
                                       userDetails.email(),
                                       userDetails.name(),
                                       userDetails.surname());
    }

    protected static UserDetails getUserDetailsPairFrom(UserDetailsResource userDetailsResource) {
        return new UserDetails(userDetailsResource.getEmail(), userDetailsResource.getName(), userDetailsResource.getSurname());
    }
}
