package com.lithan.abc_cars.controllers;

import com.lithan.abc_cars.models.Cars;
import com.lithan.abc_cars.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.lithan.abc_cars.dtos.PasswordDto;
import com.lithan.abc_cars.dtos.RegistrationDto;
import com.lithan.abc_cars.dtos.UpdateUserDto;
import com.lithan.abc_cars.models.UserEntity;
import com.lithan.abc_cars.security.SecurityUtil;
import com.lithan.abc_cars.services.UserService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;
    private UserRepository userRepository;

    @Autowired
    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/aboutUs")
    public String aboutUsPage(Model model){
        String username = SecurityUtil.getSessionUser();

        UserEntity user = userService.findByUsername(username);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "aboutUs";
    }

    @GetMapping("/contactUs")
    public String contactUsPage(Model model){
        String username = SecurityUtil.getSessionUser();

        UserEntity user = userService.findByUsername(username);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "contactUs";
    }

    @GetMapping("login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationDto", new RegistrationDto());
        return "auth/register";
    }

    @GetMapping("/profile")
    public String profilePage(Model model) {
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "auth/profile";
    }

    @GetMapping("/updatePassword")
    public String showUpdatePasswordForm(Model model) {
        model.addAttribute("passwordDto", new PasswordDto());
        return "auth/updatePassword";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<UserEntity> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users/userList";
    }

    @PostMapping("/setAsAdmin")
    public String setAsAdmin(@RequestParam Long userId) {
        userService.setAsAdmin(userId);
        return "redirect:/"; // Redirect to the user list page after setting as admin
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registrationDto") RegistrationDto registrationDto,
            BindingResult result, Model model) {
        UserEntity existingUserEmail = userService.findByEmail(registrationDto.getEmail());
        if (existingUserEmail != null && existingUserEmail.getEmail() != null
                && !existingUserEmail.getEmail().isEmpty()) {
            return "redirect:/register?fail";
        }
        UserEntity existingUserUsername = userService.findByUsername(registrationDto.getUsername());
        if (existingUserUsername != null && existingUserUsername.getUsername() != null
                && !existingUserUsername.getUsername().isEmpty()) {
            return "redirect:/register?fail";
        }
        if (result.hasErrors()) {
            model.addAttribute("registrationDto", new RegistrationDto());
            return "auth/register";
        }
        userService.saveUser(registrationDto);
        return "redirect:/login";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@Valid @ModelAttribute("profileUpdateDto") UpdateUserDto updateUserDto,
            BindingResult result, Model model) {
        String userSession = SecurityUtil.getSessionUser();
        UserEntity userEntity = userService.findByUsername(userSession);
        if (result.hasErrors()) {

            model.addAttribute("user", userEntity);
            return "auth/profile";
        }
        String username = SecurityUtil.getSessionUser();
        UserEntity user = userService.findByUsername(username);
        userService.updateProfile(user, updateUserDto);
        return "redirect:/profile";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@Valid @ModelAttribute("passwordForm") PasswordDto passwordDto,
            BindingResult bindingResult, Model model) {
        String userSession = SecurityUtil.getSessionUser();
        UserEntity userEntity = userService.findByUsername(userSession);
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userEntity);
            return "auth/updatePassword";
        }

        try {
            userService.updatePassword(userEntity, passwordDto.getOldPassword(), passwordDto.getNewPassword());
            return "redirect:/profile?success";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("oldPassword", "error.passwordForm", e.getMessage());
            model.addAttribute("user", userEntity);
            model.addAttribute("error", e);
            return "redirect:/updatePassword?fail";
        }
    }
}
