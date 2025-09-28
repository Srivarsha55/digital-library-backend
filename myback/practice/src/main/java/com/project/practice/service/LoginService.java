package com.project.practice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.practice.model.Login;
import com.project.practice.repository.LoginRepository;
import com.project.practice.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private LoginRepository loginRepository;

    // Method to create a new Login record
    public Login createLogin(Login login) {
        return loginRepository.save(login);
    }

    // Method to get all Login records
    public List<Login> getAllLogins() {
        return loginRepository.findAll();
    }

    // Method to get a Login record by ID
    public Optional<Login> getLoginById(Long id) {
        return loginRepository.findById(id);
    }

    // Method to delete a Login record by ID
    public void deleteLogin(Long id) {
        loginRepository.deleteById(id);
    }

    // Method to find a Login by username
    public Login findByUsername(String username) {
        return loginRepository.findByUsername(username);
    }

    // Method to find a Login by email
    public Login findByEmail(String email) {
        return loginRepository.findByEmail(email);
    }

    // Method to check if a username already exists
    public boolean usernameExists(String username) {
        return loginRepository.findByUsername(username) != null;
    }

    // Method to check if an email already exists
    public boolean emailExists(String email) {
        return loginRepository.findByEmail(email) != null;
    }

    // Method to update Login details (including username, email, password, etc.)
    public Login updateLogin(Long id, Login loginDetails) {
        Login existingLogin = loginRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existingLogin.setUsername(loginDetails.getUsername());
        existingLogin.setEmail(loginDetails.getEmail());

        // Only update password if it is provided and not empty
        if (loginDetails.getPassword() != null && !loginDetails.getPassword().isEmpty()) {
            existingLogin.setPassword(loginDetails.getPassword());
        }

        existingLogin.setGender(loginDetails.getGender());
        existingLogin.setPhoneNumber(loginDetails.getPhoneNumber());
        existingLogin.setBirthday(loginDetails.getBirthday());
        
        // Set the role to 'user' (fixed role)
        existingLogin.setRole("user");

        existingLogin.setMembership(loginDetails.getMembership());

        return loginRepository.save(existingLogin);
    }

    // Method to update the password (new password)
    public void updatePassword(Long id, String newPassword) {
        Login existingLogin = loginRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existingLogin.setPassword(newPassword);
        loginRepository.save(existingLogin);
    }

    // New method to update only privacy details (phone number, gender, birthday)
    public Login updatePrivacy(Long id, Login privacyDetails) {
        Login existingLogin = loginRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existingLogin.setPhoneNumber(privacyDetails.getPhoneNumber());
        existingLogin.setGender(privacyDetails.getGender());
        existingLogin.setBirthday(privacyDetails.getBirthday());

        return loginRepository.save(existingLogin);
    }

    // New method to update password after validating the current password
    public boolean updatePassword(Long id, String currentPassword, String newPassword) {
        Login existingLogin = loginRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Validate the current password
        if (!existingLogin.getPassword().equals(currentPassword)) {
            return false; // Return false if current password is incorrect
        }

        existingLogin.setPassword(newPassword);
        loginRepository.save(existingLogin);
        return true;
    }
}
