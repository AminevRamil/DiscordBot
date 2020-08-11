package com.starbun.bot.service;

import com.starbun.bot.domain.User;
import com.starbun.bot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void save(User user) {
        log.info("Saving user {}", user);
        userRepository.save(user);
    }
}
