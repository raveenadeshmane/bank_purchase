package org.bank.purchase.entity;

public record ApprovalResult(boolean isApproved, double approvedAmount, int adjustedPeriod) {}
