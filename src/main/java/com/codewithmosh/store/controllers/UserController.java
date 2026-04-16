package com.codewithmosh.store.controllers;


import com.codewithmosh.store.dtos.RegisterUserRequest;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    //method: get
    @GetMapping
    public Iterable<UserDto> getUsers(
//            @RequestHeader(required = false, name = "x-auth-token") String authToken, extract request header
            @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy) { // request param not required
                                                                // set default value, we cannot pass null as parameter
                                                                // set name when we change the param name the code still works

//        return userRepository.findAll().stream()
//                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail()))
//                .toList();

        //System.out.println(authToken);

        if (!Set.of("name", "email").contains(sortBy)) // sort by name as default if the param is wrong
            sortBy = "name";

        return userRepository.findAll(Sort.by(sortBy)).stream().map(userMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
       var user = userRepository.findById(id).orElse(null);
       if (user == null) {
           //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
           return ResponseEntity.notFound().build();
       }

//       var userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
       //return new ResponseEntity<>(user, HttpStatus.OK);
       return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder) {
        var user = userMapper.toEntity(request);
        user = userRepository.save(user);
        var userDto = userMapper.toDto(user);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();

        return ResponseEntity.created(uri).body(userDto);
    }
}
