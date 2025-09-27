package ru.event.manager.users.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {

    @GetMapping
    public void getAllUsers() {

    }

    @PostMapping
    public void createUser() {

    }

    @DeleteMapping("/{id}")
    public void deleteUserById() {

    }

    @GetMapping("/{$id}")
    public void getUserById(@PathVariable("id") Long id) {

    }
    
    @PutMapping("/{id}")
    public void updateUserById() {
        
    }
}
