package com.rocky.realestate.property;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "property_image")
public class PropertyImage {
  @Id private UUID id;

  @Column(name = "property_id", nullable = false)
  private UUID propertyId;

  @Column(name = "object_key", nullable = false, length = 500)
  private String objectKey;

  @Column(name = "sort_order", nullable = false)
  private int sortOrder;

  protected PropertyImage() {}

  public PropertyImage(UUID propertyId, String objectKey, int sortOrder) {
    this.id = UUID.randomUUID();
    this.propertyId = propertyId;
    this.objectKey = objectKey;
    this.sortOrder = sortOrder;
  }

  public UUID getId() { return id; }
  public UUID getPropertyId() { return propertyId; }
  public String getObjectKey() { return objectKey; }
  public int getSortOrder() { return sortOrder; }
}
