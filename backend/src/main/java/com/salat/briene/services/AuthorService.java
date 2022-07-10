package com.salat.briene.services;

import com.salat.briene.entities.Article;
import com.salat.briene.entities.User;
import com.salat.briene.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final UserRepository userRepository;

    private final MailService mailService;

    public void subscribe(User follower, User author) {
        assert !follower.equals(author);

        author.getFollowers().add(follower);
        follower.getFollowings().add(author);

        userRepository.save(author);
        userRepository.save(follower);
    }

    public void unsubscribe(User follower, User author) {
        assert !follower.equals(author);

        author.getFollowers().remove(follower);
        follower.getFollowings().remove(author);

        userRepository.save(author);
        userRepository.save(follower);
    }

    public boolean isFollowing(User follower, User author) {
        return follower.getFollowings().contains(author);
    }

    public void notifyAboutNewArticle(Article newArticle) throws EmailException {
        Set<User> followers = newArticle.getAuthor().getFollowers();

        for (User follower : followers) {
            mailService.sendNotificationAboutNewArticle(follower.getEmail(), newArticle);
        }
    }
}
