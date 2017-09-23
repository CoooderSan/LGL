package com.user.mapper;

import org.springframework.stereotype.Repository;
import com.user.model.User;

@Repository
public interface UserMapper {

    void saveUser(User user);

    void updateUser(User user);

    User findUser(String email);
}
