package com.rocky.realestate.property;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "property")
public class Property {
  @Id private UUID id;

  @Column(name = "owner_id", nullable = false)
  private UUID ownerId;

  @Column(nullable = false, length = 200)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PropertyCategory category;

  @Enumerated(EnumType.STRING)
  @Column(name = "listing_type", nullable = false)
  private ListingType listingType;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal price;

  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  private String area;

  @Column(name = "size_sq_ft", precision = 12, scale = 2)
  private BigDecimal sizeSqFt;

  private Integer bedrooms;
  private Integer bathrooms;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PropertyStatus status;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  protected Property() {}

  public Property(UUID ownerId, PropertyRequest request) {
    this.id = UUID.randomUUID();
    this.ownerId = ownerId;
    apply(request);
    this.status = PropertyStatus.PENDING_APPROVAL;
    this.createdAt = OffsetDateTime.now();
    this.updatedAt = this.createdAt;
  }

  public void update(PropertyRequest request) {
    apply(request);
    status = PropertyStatus.PENDING_APPROVAL;
    updatedAt = OffsetDateTime.now();
  }

  private void apply(PropertyRequest request) {
    title = request.title();
    description = request.description();
    category = request.category();
    listingType = request.listingType();
    price = request.price();
    city = request.city();
    area = request.area();
    sizeSqFt = request.sizeSqFt();
    bedrooms = request.bedrooms();
    bathrooms = request.bathrooms();
  }

  public UUID getId() { return id; }
  public UUID getOwnerId() { return ownerId; }
  public String getTitle() { return title; }
  public String getDescription() { return description; }
  public PropertyCategory getCategory() { return category; }
  public ListingType getListingType() { return listingType; }
  public BigDecimal getPrice() { return price; }
  public String getCity() { return city; }
  public String getArea() { return area; }
  public BigDecimal getSizeSqFt() { return sizeSqFt; }
  public Integer getBedrooms() { return bedrooms; }
  public Integer getBathrooms() { return bathrooms; }
  public PropertyStatus getStatus() { return status; }
  public void approve() { status = PropertyStatus.PUBLISHED; updatedAt = OffsetDateTime.now(); }
  public void reject() { status = PropertyStatus.REJECTED; updatedAt = OffsetDateTime.now(); }
}
