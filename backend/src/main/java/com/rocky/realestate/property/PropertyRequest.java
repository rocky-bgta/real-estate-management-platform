package com.rocky.realestate.property;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record PropertyRequest(
    @NotBlank @Size(max = 200) String title,
    String description,
    @NotNull PropertyCategory category,
    @NotNull ListingType listingType,
    @NotNull @DecimalMin("0.0") BigDecimal price,
    @NotBlank String city,
    @NotBlank String area,
    @Positive BigDecimal sizeSqFt,
    @PositiveOrZero Integer bedrooms,
    @PositiveOrZero Integer bathrooms) {}
