package com.salat.briene.controllers;

import com.salat.briene.entities.User;
import com.salat.briene.exceptions.UserNotFoundException;
import com.salat.briene.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PersonalAreaController {
    private final UserService userService;

    @GetMapping("/personal_area")
    public String returnPersonalPage(Model model) {
        User currentUser = userService.getUserFromContext();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("show_admin_page", userService.isUser(currentUser, "admin"));
        return "personal_area";
    }

    @PostMapping("/personal_area")
    public String updateUserFromForm(@ModelAttribute("userForm") User userFromForm, Model model) {
        User currentUser = userService.getUserFromContext();
        model.addAttribute("currentUser", currentUser);

        if (userService.isCurrentPasswordSameAs(userFromForm.getPassword())) {
            userFromForm.setId(currentUser.getId());
            userFromForm.setUsername(currentUser.getUsername());
            boolean passwordWasChanged;
            if (userFromForm.getPasswordNew().isEmpty() && userFromForm.getPasswordNewConfirm().isEmpty()) {
                passwordWasChanged = false;
            } else {
                if (userFromForm.getPasswordNew().equals(userFromForm.getPasswordNewConfirm())) {
                    passwordWasChanged = true;
                } else {
                    model.addAttribute("error", "Passwords do not match");
                    return "personal_area";
                }
            }
            try {
                userService.updateUser(userFromForm, passwordWasChanged);
                return "redirect:/logout";
            } catch (UserNotFoundException e) {
                e.printStackTrace();
                model.addAttribute("error", "Something went wrong");
                return "personal_area";
            }
        } else {
            model.addAttribute("error", "Wrong password");
            return "personal_area";
        }
    }

    @GetMapping("/personal_area/delete_account")
    public String deleteAccount() {
        User currentUser = userService.getUserFromContext();
        userService.changeRole(currentUser.getId(), "blocked");
        return "redirect:/logout";
    }
}