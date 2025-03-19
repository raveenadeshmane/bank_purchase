package org.bank.purchase.controller;

import org.bank.purchase.entity.ApprovalResult;
import org.bank.purchase.service.PurchaseApprovalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PurchaseApprovalController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({ValidationAutoConfiguration.class})
class PurchaseApprovalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PurchaseApprovalService service;

    @Test
    void shouldReturnApprovalDecision() throws Exception {
        given(service.evaluatePurchaseApproval(anyString(), anyDouble(), anyInt()))
                .willReturn(new ApprovalResult(true, 1000.0, 12));

        mockMvc.perform(post("/api/purchase/approval")
                        .contentType("application/json")
                        .content("""
                                {
                                    "personalId": "12345678912",
                                    "requestedAmount": 500.0,
                                    "paymentPeriodMonths": 12
                                }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approved").value(true))
                .andExpect(jsonPath("$.approvedAmount").value(1000.0));
    }

    @Test
    void shouldValidateRequestConstraints() throws Exception {
        mockMvc.perform(post("/api/purchase/approval")
                        .contentType("application/json")
                        .content("""
                                {
                                    "requestedAmount": 100.0,
                                    "paymentPeriodMonths": 5
                                }"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.length()").value(3));
    }
}