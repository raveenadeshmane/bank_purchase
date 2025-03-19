package org.bank.purchase.service;

import org.bank.purchase.entity.ApprovalResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PurchaseApprovalServiceTest {

    @InjectMocks
    private PurchaseApprovalService service;

    @Test
    void shouldDenyIneligibleCustomer() {
        ApprovalResult result = service.calculateApproval("12345678901", 300, 12);
        assertFalse(result.isApproved());
        assertEquals(0.0, result.approvedAmount());
    }

    @Test
    void shouldApproveMaxAmount() {
        ApprovalResult result = service.calculateApproval("12345678934", 1000, 6);
        assertTrue(result.isApproved());
        assertEquals(3000.0, result.approvedAmount()); // 50 * 24 = 1200
    }

    @Test
    void shouldAdjustPaymentPeriodWhenNeeded() {
        // Test case where requested amount 500 is too high for 6 months
        ApprovalResult result = service.calculateApproval("12345678923", 1500.0, 8);
        assertTrue(result.isApproved());
        assertEquals(1500.0, result.approvedAmount());
    }

    @Test
    void shouldAdjustPaymentPeriodWhenNeeded2() {
        // Test case where requested amount 500 is too high for 6 months
        ApprovalResult result = service.calculateApproval("12345678912", 500.0, 6);
        assertTrue(result.isApproved());
        assertEquals(500.0, result.approvedAmount()); // 50*10 = 500
    }

    @Test
    void shouldHandleMinimumAmountApproval() {
        ApprovalResult result = service.calculateApproval("12345678923", 100, 6);
        assertTrue(result.isApproved());
        assertEquals(600.0, result.approvedAmount()); // 100 * 6 = 600
    }

    @Test
    void shouldCapAtMaximumAmount() {
        ApprovalResult result = service.calculateApproval("12345678934", 10000, 24);
        assertTrue(result.isApproved());
        assertEquals(5000.0, result.approvedAmount()); // 500*24=12000 → capped at 5000
    }
}