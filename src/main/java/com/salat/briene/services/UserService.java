package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.User;
import com.salat.briene.entities.Role;
import com.salat.briene.exceptions.AnonymousUserException;
import com.salat.briene.exceptions.UserFoundException;
import com.salat.briene.exceptions.UserNotFoundException;
import com.salat.briene.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User loadUserByUsername(String username) throws UserNotFoundException {
        User userFromDB = userRepository.findByUsername(username);

        if (userFromDB == null) {
            throw new UserNotFoundException();
        }

        return userFromDB;
    }

    public User getUserFromContext() throws AnonymousUserException {
        Authentication currentUserDetails = SecurityContextHolder.getContext().getAuthentication();
        if (currentUserDetails instanceof AnonymousAuthenticationToken) {
            throw new AnonymousUserException();
        } else {
            return (User) currentUserDetails.getPrincipal();
        }
    }

    public void saveUser(User user)  throws UserFoundException {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            throw new UserFoundException();
        }

        user.setId((long) user.hashCode());
        user.setRoles(Set.of(new Role(2L, "ROLE_USER")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void updateUser(Long userId, Map<String, ?> userData) throws UserNotFoundException {
        User userFromDB = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (userData.containsKey("email")) {
            userFromDB.setEmail((String) userData.get("email"));
        }

        if (userData.containsKey("bookmarks")) {
            userFromDB.setBookmarkedArticles((Set<Article>) userData.get("bookmarks"));
        }

        if (userData.containsKey("password")) {
            userFromDB.setPassword(passwordEncoder.encode((String) userData.get("password")));
        }

        userRepository.save(userFromDB);
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
            case "blocked" -> userFromDB.setRoles(new HashSet<>(){{
                add(new Role(0L, "ROLE_BLOCKED"));
            }});

            case "admin" -> userFromDB.setRoles(new HashSet<>(){{
                add(new Role(1L, "ROLE_ADMIN"));
            }});

            case "user" -> userFromDB.setRoles(new HashSet<>(){{
                add(new Role(2L, "ROLE_USER"));
            }});
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
}