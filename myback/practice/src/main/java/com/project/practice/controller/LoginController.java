package com.project.practice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.practice.model.Login;
import com.project.practice.service.LoginService;
import com.project.practice.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<?> createLogin(@RequestBody Login login) {
        if (loginService.usernameExists(login.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Username already exists"));
        }
        if (loginService.emailExists(login.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Email already exists"));
        }

        Login newLogin = loginService.createLogin(login);
        return ResponseEntity.ok(Map.of("success", true, "message", "Registration successful", "login", newLogin));
    }

    @GetMapping
    public List<Login> getAllLogins() {
        return loginService.getAllLogins();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Login> getLoginById(@PathVariable Long id) {
        return loginService.getLoginById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLogin(@PathVariable Long id, @RequestBody Login loginDetails) {
        try {
            Login updatedLogin = loginService.updateLogin(id, loginDetails);
            return ResponseEntity.ok(Map.of("success", true, "message", "User details updated", "login", updatedLogin));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/{id}/privacy")
    public ResponseEntity<?> updatePrivacy(@PathVariable Long id, @RequestBody Login privacyDetails) {
        try {
            Login updatedLogin = loginService.updatePrivacy(id, privacyDetails);
            return ResponseEntity.ok(Map.of("success", true, "message", "Privacy details updated", "login", updatedLogin));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // New endpoint to update password
    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestBody Map<String, String> passwordDetails) {
        String currentPassword = passwordDetails.get("currentPassword");
        String newPassword = passwordDetails.get("newPassword");

        if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "New password cannot be empty"));
        }

        try {
            boolean isUpdated = loginService.updatePassword(id, currentPassword, newPassword);
            if (isUpdated) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Password updated successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Current password is incorrect"));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLogin(@PathVariable Long id) {
        if (loginService.getLoginById(id).isPresent()) {
            loginService.deleteLogin(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/check")
    public ResponseEntity<?> checkLogin(@RequestBody Login login) {
        Login existingLogin = loginService.findByUsername(login.getUsername());
        if (existingLogin != null && existingLogin.getPassword().equals(login.getPassword())) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Login successful!", "role", existingLogin.getRole()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Invalid username or password"));
        }
    }
}
