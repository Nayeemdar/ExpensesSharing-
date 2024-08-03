package com.nayeem.ExpensesSharing.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nayeem.ExpensesSharing.Model.Expense;
import com.nayeem.ExpensesSharing.Model.User;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByPayerOrderByCreatedAtDesc(User payer);
}
