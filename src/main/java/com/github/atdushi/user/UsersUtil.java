package com.github.atdushi.user;

import lombok.experimental.UtilityClass;
import com.github.atdushi.user.model.Role;
import com.github.atdushi.user.model.User;
import com.github.atdushi.user.to.UserTo;

@UtilityClass
public class UsersUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }
}