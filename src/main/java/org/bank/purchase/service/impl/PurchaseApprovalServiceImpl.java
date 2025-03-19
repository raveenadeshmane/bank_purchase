package org.bank.purchase.service.impl;

import org.bank.purchase.entity.ApprovalResult;
import org.bank.purchase.service.PurchaseApprovalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PurchaseApprovalServiceImpl implements PurchaseApprovalService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseApprovalServiceImpl.class);
    private static final ApprovalResult DEFAULT_APPROVAL_RESULT = new ApprovalResult(false, 0.0, 0);

    @Override
    public ApprovalResult evaluatePurchaseApproval(String personalId, double requestedAmount, int paymentPeriodMonths) {
        logger.debug("Starting approval calculation for personalId: {}", personalId);
        if (isIneligible(personalId)) {
            logger.warn("Denied: Customer {} is ineligible", personalId);
            return DEFAULT_APPROVAL_RESULT;
        }

        int financialFactor = getFinancialCapacityFactor(personalId);
        logger.info("Financial factor {} determined for {}", financialFactor, personalId);

        boolean approvalResult = calculateApprovalResult(financialFactor, requestedAmount, paymentPeriodMonths); // Score >= 1 when (factor/amount)*months >= 1

        if (approvalResult) {
            return calculateApprovalAmount(financialFactor, paymentPeriodMonths);
        }

        for (int months = paymentPeriodMonths + 1; months <= 24; months++) {
            approvalResult = calculateApprovalResult(financialFactor, requestedAmount, months);
            if (approvalResult) {
                return calculateApprovalAmount(financialFactor, months);
            }
        }

        return DEFAULT_APPROVAL_RESULT;
    }

    private ApprovalResult calculateApprovalAmount(int financialFactor, int months) {
        double approvalAmount = (financialFactor * months);
        return new ApprovalResult(true, Math.min(approvalAmount, 5000.0), months);
    }

    private boolean calculateApprovalResult(int financialFactor, double requestedAmount, int months) {
        return ((financialFactor / requestedAmount) * months) >= 1; // Score >= 1 when (factor/amount)*months >= 1
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

