package org.bank.purchase.service;

import org.bank.purchase.entity.ApprovalResult;


public interface PurchaseApprovalService {

    ApprovalResult evaluatePurchaseApproval(String personalId, double requestedAmount, int paymentPeriodMonths) ;
}

