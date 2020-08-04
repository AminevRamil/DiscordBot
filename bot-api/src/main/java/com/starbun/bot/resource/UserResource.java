package com.starbun.bot.resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserResource {

    // Можно ли здесь использовать код из impl, если IDEA говорит, что будет круговая зависимость?

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(long id) {
        log.info("UserResource.profile() by id {}", id);
        return Long.toString(id);
    }

}
