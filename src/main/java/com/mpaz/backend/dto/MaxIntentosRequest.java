package com.mpaz.backend.dto;

import jakarta.validation.constraints.Min;

public record MaxIntentosRequest(@Min(1) int maxIntentos) {}
