package com.starbun.bot.repository;

import com.starbun.bot.domain.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Long> {

}
