package com.bridgelabz.fundoo_notes.user.repository;

import com.bridgelabz.fundoo_notes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import javax.swing.text.html.Option;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
//    User findByEmail(String email);
    
    Optional<User> findByEmail(String email);

    User findById(int id);
}
