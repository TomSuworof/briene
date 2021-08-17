//package com.salat.briene.controllers;
//
//import com.salat.briene.exceptions.UserNotFoundException;
//import com.salat.briene.services.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//@RequiredArgsConstructor
//public class AdminUsersController {
//    private final UserService userService;
//
//    @PostMapping("/admin/users")
//    public String changeRole(@RequestParam String action, @RequestParam Long userId, Model model) {
//        try {
//            switch (action) {
//                case "delete" -> userService.changeRole(userId, "blocked");
//                case "make_admin" -> userService.changeRole(userId, "admin");
//                case "make_user" -> userService.changeRole(userId, "user");
//            }
//            return "redirect:/admin";
//        } catch (UserNotFoundException e) {
//            e.printStackTrace();
//            model.addAttribute("error", "Error changing role");
//            return "admin";
//        }
//    }
//}