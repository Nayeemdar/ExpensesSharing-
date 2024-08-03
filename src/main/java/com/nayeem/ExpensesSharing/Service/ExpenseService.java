package com.nayeem.ExpensesSharing.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.nayeem.ExpensesSharing.Model.Expense;
import com.nayeem.ExpensesSharing.Model.ExpenseSplit;
import com.nayeem.ExpensesSharing.Model.User;
import com.nayeem.ExpensesSharing.Repository.ExpenseRepository;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    @Transactional
    public Expense addExpense(Expense expense, List<ExpenseSplit> splits) {
        validateExpenseSplits(expense, splits);
        expense.setCreatedAt(LocalDateTime.now());
        expense.setSplits(splits);
        return expenseRepository.save(expense);
    }

    private void validateExpenseSplits(Expense expense, List<ExpenseSplit> splits) {
        BigDecimal totalSplitAmount = BigDecimal.ZERO;
        BigDecimal totalPercentage = BigDecimal.ZERO;

        for (ExpenseSplit split : splits) {
            split.setExpense(expense);
            if (expense.getSplitMethod() == Expense.SplitMethod.EXACT) {
                totalSplitAmount = totalSplitAmount.add(split.getAmount());
            } else if (expense.getSplitMethod() == Expense.SplitMethod.PERCENTAGE) {
                totalPercentage = totalPercentage.add(split.getPercentage());
            }
        }

        if (expense.getSplitMethod() == Expense.SplitMethod.EXACT && !totalSplitAmount.equals(expense.getTotalAmount())) {
            throw new IllegalArgumentException("Total split amount does not match the expense total");
        }

        if (expense.getSplitMethod() == Expense.SplitMethod.PERCENTAGE && !totalPercentage.equals(new BigDecimal("100"))) {
            throw new IllegalArgumentException("Total percentage must equal 100%");
        }
    }

    public List<Expense> getUserExpenses(Long userId) {
        User user = userService.getUserById(userId);
        return expenseRepository.findByPayerOrderByCreatedAtDesc(user);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

     public Map<String, BigDecimal> getOverallExpenses() {
        List<Expense> allExpenses = expenseRepository.findAll();
        Map<String, BigDecimal> overallExpenses = new HashMap<>();

        for (Expense expense : allExpenses) {
            String payerName = expense.getPayer().getName();
            BigDecimal amount = expense.getTotalAmount();

            overallExpenses.merge(payerName, amount, BigDecimal::add);
        }

        return overallExpenses;
    }

    public byte[] generateBalanceSheet() {
        List<Expense> allExpenses = expenseRepository.findAll();
        Map<String, BigDecimal> balances = new HashMap<>();

        for (Expense expense : allExpenses) {
            String payerName = expense.getPayer().getName();
            BigDecimal totalAmount = expense.getTotalAmount();

            balances.merge(payerName, totalAmount, BigDecimal::subtract);

            for (ExpenseSplit split : expense.getSplits()) {
                String participantName = split.getUser().getName();
                BigDecimal splitAmount = split.getAmount();

                balances.merge(participantName, splitAmount, BigDecimal::add);
            }
        }

        // Generate PDF using a library like iText or Apache PDFBox
        // For this example, we'll just create a simple string representation
        StringBuilder pdfContent = new StringBuilder("Balance Sheet\n\n");
        for (Map.Entry<String, BigDecimal> entry : balances.entrySet()) {
            pdfContent.append(entry.getKey()).append(": ")
                    .append(entry.getValue()).append("\n");
        }

        return pdfContent.toString().getBytes();
    }
    
}
