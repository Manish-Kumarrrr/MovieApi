package com.movieflix.movieAPI.auth.repositories;

import com.movieflix.movieAPI.auth.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);


    @Modifying
    @Transactional
    @Query("update user u set u.password=?2 where u.email=?1")
    void updatePassword(String email,String password);
}
