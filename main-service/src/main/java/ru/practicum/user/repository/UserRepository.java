package ru.practicum.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM users u WHERE u.id in :ids /*:pageable*/", nativeQuery = true)
    Page<User> findByIds(Long[] ids, Pageable pageable);

}
