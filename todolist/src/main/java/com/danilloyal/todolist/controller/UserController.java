package com.danilloyal.todolist.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.danilloyal.todolist.model.UserModel;
import com.danilloyal.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public ResponseEntity<List<UserModel>> findAll(){
        List<UserModel> allUsers = this.userRepository.findAll();
        return ResponseEntity.ok().body(allUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserModel>> findById(@PathVariable UUID id) throws Exception{
        if(id == null)throw new Exception("Id not found");
        var user0 = userRepository.findById(id);
        if(user0.isEmpty())throw new Exception("User not found");
        return ResponseEntity.ok().body(user0);
    }
    @PostMapping("/create")
    public ResponseEntity<UserModel> create(@RequestBody UserModel userModel) throws Exception{
        var user = this.userRepository.findByUsername(userModel.getUsername());
        if(user != null)throw new Exception("Username uavailable");
        var pwcripto = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(pwcripto);
        var user0 = this.userRepository.save(userModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(user0);
    }
}
