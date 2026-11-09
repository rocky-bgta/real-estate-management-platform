package com.rocky.realestate.property;
import java.math.BigDecimal; import java.util.UUID;
public record PropertyResponse(UUID id, UUID ownerId, String title, String description, PropertyCategory category, ListingType listingType, BigDecimal price, String city, String area, BigDecimal sizeSqFt, Integer bedrooms, Integer bathrooms, PropertyStatus status) {
 public static PropertyResponse from(Property p){return new PropertyResponse(p.getId(),p.getOwnerId(),p.getTitle(),p.getDescription(),p.getCategory(),p.getListingType(),p.getPrice(),p.getCity(),p.getArea(),p.getSizeSqFt(),p.getBedrooms(),p.getBathrooms(),p.getStatus());}
}
