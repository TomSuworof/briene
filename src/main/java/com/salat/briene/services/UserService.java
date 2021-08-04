package com.salat.briene.services;

import com.salat.briene.entities.User;
import com.salat.briene.entities.Role;
import com.salat.briene.exceptions.AnonymousUserException;
import com.salat.briene.exceptions.UserFoundException;
import com.salat.briene.exceptions.UserNotFoundException;
import com.salat.briene.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        UserDetails userFromDB = userRepository.findByUsername(username);
        if (userFromDB == null) {
            throw new UserNotFoundException();
        }
        return userFromDB;
    }

    public User getUserFromContext() throws AnonymousUserException {
        Object currentUserDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUserDetails.equals("anonymousUser")) {
            throw new AnonymousUserException();
        } else {
            return (User) currentUserDetails;
        }
    }

    public void saveUser(User user)  throws UserFoundException {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            throw new UserFoundException();
        }

        user.setId((long) user.hashCode());
        user.setRoles(Set.of(new Role(3L, "ROLE_USER")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void updateUser(User userFromForm, boolean passwordWasChanged) throws UserNotFoundException {
        User userFromDB = userRepository.findById(userFromForm.getId()).orElseThrow(UserNotFoundException::new);

        deleteUser(userFromForm.getId());

        userFromDB.setEmail(userFromForm.getEmail());

        if (passwordWasChanged) {
            updateWithPassword(userFromDB, userFromForm.getPasswordNew());
        } else {
            updateWithoutPassword(userFromDB);
        }
    }

    private void updateWithPassword(User userUpdated, String passwordNew) {
        userUpdated.setPassword(passwordEncoder.encode(passwordNew));
        userRepository.save(userUpdated);
    }

    private void updateWithoutPassword(User userUpdated) {
        userRepository.save(userUpdated);
    }

    private void deleteUser(Long userId) throws UserNotFoundException {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
        } else {
            throw new UserNotFoundException();
        }
    }

    public void changeRole(Long userId, String role) throws UserNotFoundException {
        User userFromDB = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        switch (role) {
            case "editor" -> userFromDB.setRoles(Collections.singleton(new Role(2L, "ROLE_EDITOR")));
            case "user" -> userFromDB.setRoles(Collections.singleton(new Role(3L, "ROLE_USER")));
            case "blocked" -> userFromDB.setRoles(Collections.singleton(new Role(0L, "ROLE_BLOCKED")));
        }
//        mailService.send(userFromDB.getEmail(), "role_change", role);
        userRepository.save(userFromDB);
    }

    public boolean isCurrentPasswordSameAs(String passwordAnother) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserPassword = currentUser.getPassword();
        return passwordEncoder.matches(passwordAnother, currentUserPassword);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean isUser(User user, String role) {
        if (user == null) {
            return false;
        } else if (role.equals("admin") && user.getRoles().contains(new Role((long) 1, "ROLE_ADMIN"))) {
            return true;
        } else if (role.equals("editor") && user.getRoles().contains(new Role((long) 2, "ROLE_EDITOR"))) {
            return true;
        } else if (role.equals("user") && user.getRoles().contains(new Role((long) 3, "ROLE_USER"))) {
            return true;
        } else if (role.equals("blocked") && user.getRoles().contains(new Role((long) 0, "ROLE_BLOCKED"))) {
            return true;
        } else {
            return false;
        }
    }
}