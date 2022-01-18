package com.bridgelabz.fundoo_notes.repository;

import com.bridgelabz.fundoo_notes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    User findById(int id);
}
