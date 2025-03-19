package org.bank.purchase.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import org.bank.purchase.entity.ApprovalResult;

public record ApprovalResponse(
        @Schema(description = "Approval status", example = "true")
        boolean approved,
        @Schema(description = "Approved amount (0 if denied)", example = "300.0")
        double approvedAmount,
        @Schema(description = "Adjusted adjustedPeriod", example = "6")
        int adjustedPeriod) {
    public ApprovalResponse(ApprovalResult approvalResult) {
        this(approvalResult.isApproved(), approvalResult.approvedAmount(), approvalResult.adjustedPeriod());
    }
}
