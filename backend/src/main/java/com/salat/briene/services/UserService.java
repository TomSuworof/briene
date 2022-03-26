package com.salat.briene.services;

import com.salat.briene.entities.*;
import com.salat.briene.exceptions.*;
import com.salat.briene.payload.request.SignupRequest;
import com.salat.briene.payload.request.UserDataRequest;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.payload.response.UserDTO;
import com.salat.briene.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public User loadUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User getUserFromAuthentication(Authentication authentication) {
        if (authentication == null) {
            throw new AnonymousUserException();
        }
        return loadUserByUsername(authentication.getName());
    }

    public void saveUser(SignupRequest signupRequest) {
        if (existsByUsername(signupRequest.getUsername())) {
            throw new DuplicatedUserByUsernameException();
        }

        if (existsByEmail(signupRequest.getEmail())) {
            throw new DuplicatedUserByEmailException();
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setRoles(Set.of(RoleEnum.USER.getAsObject()));
        userRepository.save(user);
    }

    private boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void updateUser(UUID userId, UserDataRequest userData) {
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
            userFromDB.setPassword(passwordEncoder.encode(userData.getPassword().get()));
        }

        userRepository.save(userFromDB);
    }

    @Deprecated
    // should be cascaded
    private void deleteUser(UUID userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
        } else {
            throw new UserNotFoundException();
        }
    }

    public void changeRole(UUID userId, Role role) throws EmailException {
        User userFromDB = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        userFromDB.setRoles(new HashSet<>(){{
            add(role);
        }});

        mailService.send(userFromDB.getEmail(), "role_change", role.getName());
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


    public PageResponseDTO<UserDTO> getUsersPaginated(Integer limit, Integer offset) {
        Page<User> users = getAllUsersPaginated(limit, offset);

        return new PageResponseDTO<>(
                offset > 0 && users.getTotalElements() > 0,
                (offset + limit) < users.getTotalElements(),
                users.stream().map(UserDTO::new).collect(Collectors.toList())
        );
    }

    private Page<User> getAllUsersPaginated(Integer limit, Integer offset) {
        return userRepository.findAll(PageRequest.of(offset / limit, limit));
    }
}