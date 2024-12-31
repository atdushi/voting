package com.github.atdushi.user.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.atdushi.common.BaseRepository;
import com.github.atdushi.common.error.NotFoundException;
import com.github.atdushi.user.model.User;

import java.util.Optional;

import static com.github.atdushi.app.config.SecurityConfig.PASSWORD_ENCODER;

@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User> {
    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(String email);

    @Transactional
    default User prepareAndSave(User user) {
        user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return save(user);
    }

    default User getExistedByEmail(String email) {
        return findByEmailIgnoreCase(email).orElseThrow(() -> new NotFoundException("User with email=" + email + " not found"));
    }
}