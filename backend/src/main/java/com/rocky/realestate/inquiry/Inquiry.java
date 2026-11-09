package com.rocky.realestate.inquiry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "property_inquiry")
public class Inquiry {
  @Id private UUID id;
  @Column(name = "property_id", nullable = false) private UUID propertyId;
  @Column(name = "user_id", nullable = false) private UUID userId;
  @Column(nullable = false, columnDefinition = "TEXT") private String message;
  @Column(name = "created_at", nullable = false) private OffsetDateTime createdAt;

  protected Inquiry() {}

  public Inquiry(UUID propertyId, UUID userId, String message) {
    this.id = UUID.randomUUID();
    this.propertyId = propertyId;
    this.userId = userId;
    this.message = message;
    this.createdAt = OffsetDateTime.now();
  }

  public UUID getId() { return id; }
}
