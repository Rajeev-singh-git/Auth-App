package com.substring.auth.auth_app_backend.controllers;


import com.substring.auth.auth_app_backend.dtos.UserDto;
import com.substring.auth.auth_app_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;


    @PostMapping()
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    @GetMapping()
    public ResponseEntity<Iterable<UserDto>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") String userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") String userId){
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable("userId") String userId){
        return ResponseEntity.ok(userService.updateUser(userDto,userId));
    }

}
