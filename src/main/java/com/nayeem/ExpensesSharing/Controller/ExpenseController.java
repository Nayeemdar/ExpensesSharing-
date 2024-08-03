package com.nayeem.ExpensesSharing.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.nayeem.ExpensesSharing.Model.Expense;
import com.nayeem.ExpensesSharing.Model.ExpenseSplit;
import com.nayeem.ExpensesSharing.Service.ExpenseService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Tag(name = "Expense Management", description = "Operations pertaining to expenses in the application")
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping
    @Operation(summary = "Add a new expense", description = "Creates a new expense with the given details")
    @ApiResponse(responseCode = "200", description = "Successfully created the expense", 
                 content = @Content(schema = @Schema(implementation = Expense.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<Expense> addExpense(
            @Parameter(description = "Expense details", required = true) @Validated @RequestBody ExpenseRequest expenseRequest) {
        Expense expense = expenseService.addExpense(expenseRequest.getExpense(), expenseRequest.getSplits());
        return ResponseEntity.ok(expense);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user expenses", description = "Retrieves all expenses for a specific user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user expenses")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<List<Expense>> getUserExpenses(
            @Parameter(description = "ID of the user", required = true) @PathVariable Long userId) {
        return ResponseEntity.ok(expenseService.getUserExpenses(userId));
    }

    @GetMapping("/overall")
    @Operation(summary = "Get overall expenses", description = "Retrieves overall expenses for all users")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved overall expenses")
    public ResponseEntity<Map<String, BigDecimal>> getOverallExpenses() {
        Map<String, BigDecimal> overallExpenses = expenseService.getOverallExpenses();
        return ResponseEntity.ok(overallExpenses);
    }

    @GetMapping("/balance-sheet")
    @Operation(summary = "Download balance sheet", description = "Generates and downloads a PDF balance sheet")
    @ApiResponse(responseCode = "200", description = "Successfully generated balance sheet")
    @ApiResponse(responseCode = "500", description = "Error generating balance sheet")
    public ResponseEntity<ByteArrayResource> downloadBalanceSheet() {
        byte[] balanceSheetData = expenseService.generateBalanceSheet();
        ByteArrayResource resource = new ByteArrayResource(balanceSheetData);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=balance_sheet.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(balanceSheetData.length)
                .body(resource);
    }

    @Data
    private static class ExpenseRequest {
        @Schema(description = "Expense details")
        private Expense expense;
        
        @Schema(description = "List of expense splits")
        private List<ExpenseSplit> splits;
    }
}