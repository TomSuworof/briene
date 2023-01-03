package com.salat.briene.services;

import com.salat.briene.config.PasswordEncoder;
import com.salat.briene.entities.*;
import com.salat.briene.exceptions.*;
import com.salat.briene.payload.request.SignupRequest;
import com.salat.briene.payload.request.UserDataRequest;
import com.salat.briene.payload.response.PageResponseDTO;
import com.salat.briene.payload.response.UserDTO;
import com.salat.briene.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User loadUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    public User loadUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User getUserFromAuthentication(Authentication authentication) throws AnonymousUserException, UserNotFoundException {
        if (authentication == null) {
            throw new AnonymousUserException();
        }
        return loadUserByUsername(authentication.getName());
    }

    public UserDTO saveUser(SignupRequest signupRequest) {
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

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                mailService.sendRegistrationConfirm(user.getEmail(), user.getUsername());
            } catch (EmailException e) {
                throw new RuntimeException(e);
            }
        });

        return new UserDTO(user);
    }

    private boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDTO updateUser(UUID userId, UserDataRequest userData) {
        User userFromDB = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (userData.getAvatar().isPresent()) {
            userFromDB.setAvatar(userData.getAvatar().get());
        }

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
        return new UserDTO(userFromDB);
    }

    public UserDTO changeRole(UUID userId, Role role) {
        User userFromDB = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        userFromDB.setRoles(new HashSet<>(Collections.singletonList(role)));

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                mailService.sendRoleChanged(userFromDB.getEmail(), role.getName());
            } catch (EmailException e) {
                throw new RuntimeException(e);
            }
        });

        userRepository.save(userFromDB);
        return new UserDTO(userFromDB);
    }

    public boolean isCurrentPasswordSameAs(UUID requiredUserId, String passwordAnother) {
        User requiredUser = this.loadUserById(requiredUserId);
        String requiredUserPassword = requiredUser.getPassword();
        return passwordEncoder.matches(passwordAnother, requiredUserPassword);
    }

    @Deprecated
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public PageResponseDTO<UserDTO> getUsersPaginated(Integer limit, Integer offset) {
        Page<User> users = getAllUsersPaginated(limit, offset);

        return new PageResponseDTO<>(
                offset > 0 && users.getTotalElements() > 0,
                (offset + limit) < users.getTotalElements(),
                users.stream().map(UserDTO::new).toList(),
                users.getTotalElements());
    }

    private Page<User> getAllUsersPaginated(Integer limit, Integer offset) {
        return userRepository.findAll(PageRequest.of(offset / limit, limit));
    }
}