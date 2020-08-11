package com.starbun.bot.exceptions;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController {

    @RequestMapping(value = "/runtimeException", method = RequestMethod.GET)
    public void throwException( ) {
        throw new RuntimeException();
    }
}
