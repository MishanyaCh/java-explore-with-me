package ru.practicum.user.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    List<User> findUsersByIdIn(List<Integer> ids, Pageable pageable);
}
