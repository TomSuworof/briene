//package com.salat.briene.controllers;
//
//import com.salat.briene.entities.User;
//import com.salat.briene.exceptions.UserFoundException;
//import com.salat.briene.services.UserService;
//import com.salat.briene.services.MailService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Controller
//@RequiredArgsConstructor
//public class RegistrationController {
//    private final UserService userService;
//    private final MailService mailService;
//
//    @GetMapping("/registration")
//    public String registration(Model model) {
//        model.addAttribute("userForm", new User());
//        return "registration";
//    }
//
//    @PostMapping("/registration")
//    public String addUser(@ModelAttribute("userForm") User userForm,
//                          BindingResult bindingResult,
//                          Model model) {
//        if (bindingResult.hasErrors()) {
//            return "registration";
//        }
//
//        try {
//            userService.saveUser(userForm);
//            mailService.send(userForm.getEmail(), "registration_confirm", userForm.getUsername());
//            return "redirect:/";
//        } catch (UserFoundException e) {
//            e.printStackTrace();
//            model.addAttribute("usernameError", "Username unavailable");
//            return "registration";
//        }
//    }
//}