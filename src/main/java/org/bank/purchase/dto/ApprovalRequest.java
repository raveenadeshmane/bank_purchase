package org.bank.purchase.dto;

import jakarta.validation.constraints.*;

public record ApprovalRequest(
     //   @Schema(description = "Customer's personal identification number", example = "12345678912")
        @NotBlank(message = "Personal ID is required")
        String personalId,
       // @Schema(description = "Requested purchase amount (€200-€5000)", example = "500.0")
        @DecimalMin(value="200.0", message = "Minimum amount is 200€")
        @DecimalMax(value="5000.0", message = "Maximum amount is 5000€")
        double requestedAmount,
     //   @Schema(description = "Requested payment adjustedPeriod in months (6-24)", example = "12")
        @Min(value = 6, message = "Minimum adjustedPeriod is 6 months")
        @Max(value = 24, message = "Maximum adjustedPeriod is 24 months")
        int paymentPeriodMonths) {}
