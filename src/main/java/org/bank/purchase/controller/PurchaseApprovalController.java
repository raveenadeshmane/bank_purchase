package org.bank.purchase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import org.bank.purchase.dto.ApprovalRequest;
import org.bank.purchase.dto.ApprovalResponse;
import org.bank.purchase.entity.ApprovalResult;
import org.bank.purchase.service.PurchaseApprovalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseApprovalController {
    private final PurchaseApprovalService service;

    public PurchaseApprovalController(PurchaseApprovalService service) {
        this.service = service;
    }

    @Operation(
            summary = "Get purchase approval decision",
            description = "Calculate maximum approved purchase amount based on customer profile",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Approval decision calculated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApprovalResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input parameters"
                    )
            }
    )
    @PostMapping("/approval")
    public ResponseEntity<ApprovalResponse> approvePurchase(@Valid @RequestBody @Schema(description = "Purchase request details") ApprovalRequest request) {
        ApprovalResult result = service.evaluatePurchaseApproval(request.personalId(), request.requestedAmount(), request.paymentPeriodMonths());
        return ResponseEntity.ok(new ApprovalResponse(result));
    }
}
