package com.rocky.realestate.property;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;
public record PropertyRequest(@NotNull UUID ownerId, @NotBlank @Size(max=200) String title, String description, @NotNull PropertyCategory category, @NotNull ListingType listingType, @NotNull @DecimalMin("0.0") BigDecimal price, @NotBlank String city, @NotBlank String area, @Positive BigDecimal sizeSqFt, @PositiveOrZero Integer bedrooms, @PositiveOrZero Integer bathrooms) {}
