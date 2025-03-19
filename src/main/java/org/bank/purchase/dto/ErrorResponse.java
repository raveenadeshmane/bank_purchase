package org.bank.purchase.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Error response structure")
public record ErrorResponse(
        @Schema(description = "API path where error occurred")
        String path,

        @Schema(description = "List of error messages")
        List<String> errors,

        @Schema(description = "HTTP status code")
        int status,

        @Schema(description = "Error type classification")
        String error
) {
}