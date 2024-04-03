package com.danilloyal.todolist.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.danilloyal.todolist.model.UserModel;
import com.danilloyal.todolist.model.UserNotFoundException;
import com.danilloyal.todolist.model.UnavailableUsernameException;
import com.danilloyal.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/")
    public ResponseEntity<List<UserModel>> findAll(){
        List<UserModel> allUsers = this.userRepository.findAll();
        return ResponseEntity.ok().body(allUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserModel>> findById(@PathVariable UUID id) throws RunTimeException{
        if(id == null)throw new Exception("Id not found");
        var user0 = userRepository.findById(id).orElseThrow(() => new UserNotFoundException("User not found"));
        return ResponseEntity.ok().body(user0);
    }
    @PostMapping("/create")
    public ResponseEntity<UserModel> create(@RequestBody UserModel userModel) throws RunTimeException{
        var user = this.userRepository.findByUsername(userModel.getUsername()).orElseThrow(() => new UnavailableUsernameException("Username unavailable"));
        var pwcripto = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(pwcripto);
        var user0 = this.userRepository.save(userModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(user0);
    }
}
