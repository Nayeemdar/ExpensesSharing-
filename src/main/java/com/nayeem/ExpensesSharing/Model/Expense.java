package com.nayeem.ExpensesSharing.Model;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payer_id", nullable = false)
    private User payer;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private SplitMethod splitMethod;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
    private List<ExpenseSplit> splits;

    public enum SplitMethod {
        EQUAL, EXACT, PERCENTAGE
    }
}
