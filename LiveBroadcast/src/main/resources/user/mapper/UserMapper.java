package user.mapper;

import org.springframework.stereotype.Repository;
import user.model.User;

@Repository
public interface UserMapper {

    void saveUser(User user);

    void updateUser(User user);

    User findUser(String email);
}
