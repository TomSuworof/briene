package com.salat.briene.services;

import com.salat.briene.entities.*;
import com.salat.briene.exceptions.*;
import com.salat.briene.payload.request.SignupRequest;
import com.salat.briene.payload.request.UserDataRequest;
import com.salat.briene.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User loadUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public User loadUserById(UUID id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User getUserFromAuthentication(Authentication authentication) throws AnonymousUserException {
        if (authentication == null) {
            throw new AnonymousUserException();
        }
        return loadUserByUsername(authentication.getName());
    }

    public void saveUser(SignupRequest signupRequest)  throws DuplicatedUserException {
        if (existsByUsername(signupRequest.getUsername())) {
            throw new DuplicatedUserByUsernameException();
        }

        if (existsByEmail(signupRequest.getEmail())) {
            throw new DuplicatedUserByEmailException();
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setSecretQuestion(signupRequest.getSecretQuestion());
        user.setSecretAnswer(signupRequest.getSecretAnswer());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setRoles(Set.of(RoleEnum.USER.getAsObject()));
//        user.setId((long) user.hashCode());
        userRepository.save(user);
    }

    private boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void updateUser(UUID userId, UserDataRequest userData) throws UserNotFoundException {
        User userFromDB = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (userData.getEmail().isPresent()) {
            userFromDB.setEmail(userData.getEmail().get());
        }

        if (userData.getBio().isPresent()) {
            userFromDB.setBio(userData.getBio().get());
        }

        if (userData.getBookmarks().isPresent()) {
            userFromDB.setBookmarkedArticles(userData.getBookmarks().get());
        }

        if (userData.getPassword().isPresent()) {
            userFromDB.setPassword(userData.getPassword().get());
        }

        userRepository.save(userFromDB);
    }

    private void deleteUser(UUID userId) throws UserNotFoundException {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
        } else {
            throw new UserNotFoundException();
        }
    }

    public void changeRole(UUID userId, Role role) throws UserNotFoundException {
        User userFromDB = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        userFromDB.setRoles(new HashSet<>(){{
            add(role);
        }});

//        mailService.send(userFromDB.getEmail(), "role_change", role);
        userRepository.save(userFromDB);
    }

    public boolean isCurrentPasswordSameAs(UUID requiredUserId, String passwordAnother) {
        User requiredUser = this.loadUserById(requiredUserId);
        String requiredUserPassword = requiredUser.getPassword();
        return passwordEncoder.matches(passwordAnother, requiredUserPassword);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}