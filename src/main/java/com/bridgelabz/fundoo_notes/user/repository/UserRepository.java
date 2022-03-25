package com.bridgelabz.fundoo_notes.user.repository;

import com.bridgelabz.fundoo_notes.entity.User;

//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findByEmail(String email);

    User findById(int id);
}
