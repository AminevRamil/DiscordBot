package com.starbun.bot.resource.impl;

import com.starbun.bot.domain.User;
import com.starbun.bot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserResourceImpl {

    private final UserService userService;

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public void save(Long id, String username) {
        User user = new User(id, username, null, null);
        userService.save(user);
        log.info("saving: {}", user);
    }
}
