package com.nayeem.ExpensesSharing.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.nayeem.ExpensesSharing.Model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
