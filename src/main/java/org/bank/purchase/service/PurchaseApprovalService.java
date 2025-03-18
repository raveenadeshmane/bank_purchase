package org.bank.purchase.service;

import org.bank.purchase.entity.ApprovalResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PurchaseApprovalService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseApprovalService.class);
    private static final ApprovalResult DEFAULT_APPROVAL_RESULT = new ApprovalResult(false, 0.0, 0);

    public ApprovalResult calculateApproval(String personalId, double requestedAmount, int paymentPeriodMonths) {
        logger.debug("Starting approval calculation for personalId: {}", personalId);
        if (isIneligible(personalId)) {
            logger.warn("Denied: Customer {} is ineligible", personalId);
            return DEFAULT_APPROVAL_RESULT;
        }

        int financialFactor = getFinancialCapacityFactor(personalId);
        logger.info("Financial factor {} determined for {}", financialFactor, personalId);

        double approvalResult = calculateApprovalResult(financialFactor, requestedAmount, paymentPeriodMonths); // Score >= 1 when (factor/amount)*months >= 1

        if (approvalResult >= 1) {
            double approvedAmount = calculateApprovalAmount(financialFactor, paymentPeriodMonths);
            return new ApprovalResult(true, Math.min(approvedAmount, 5000.0), paymentPeriodMonths);
        }

        for (int months = paymentPeriodMonths + 1; months <= 24; months++) {
            approvalResult = calculateApprovalResult(financialFactor, requestedAmount, months);
            if (approvalResult >= 1) {
                double approvedAmount = calculateApprovalAmount(financialFactor, months);
                return new ApprovalResult(true, Math.min(approvedAmount, 5000.0), months);
            }
        }

        return DEFAULT_APPROVAL_RESULT;
    }

    private double calculateApprovalAmount(int financialFactor, int paymentPeriodMonths) {
        return (financialFactor * paymentPeriodMonths);
    }

    private double calculateApprovalResult(int financialFactor, double requestedAmount, int months) {
        return (financialFactor / requestedAmount) * months; // Score >= 1 when (factor/amount)*months >= 1
    }

    private boolean isIneligible(String personalId) {
        return switch (personalId) {
            case "12345678901" -> true;
            case "12345678912", "12345678923", "12345678934" -> false;
            default -> true;
        };
    }

    private int getFinancialCapacityFactor(String personalId) {
        return switch (personalId) {
            case "12345678912" -> 50;
            case "12345678923" -> 100;
            case "12345678934" -> 500;
            default -> throw new IllegalArgumentException("Invalid personal ID");
        };
    }
}

