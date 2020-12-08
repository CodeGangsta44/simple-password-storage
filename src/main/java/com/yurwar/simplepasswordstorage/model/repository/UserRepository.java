package com.yurwar.simplepasswordstorage.model.repository;

import com.yurwar.simplepasswordstorage.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
}
