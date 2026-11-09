package com.rocky.realestate.favorite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "favorite")
public class Favorite {
  @Id private UUID id;
  @Column(name = "user_id", nullable = false) private UUID userId;
  @Column(name = "property_id", nullable = false) private UUID propertyId;
  @Column(name = "created_at", nullable = false) private OffsetDateTime createdAt;

  protected Favorite() {}

  public Favorite(UUID userId, UUID propertyId) {
    this.id = UUID.randomUUID();
    this.userId = userId;
    this.propertyId = propertyId;
    this.createdAt = OffsetDateTime.now();
  }

  public UUID getId() { return id; }
  public UUID getUserId() { return userId; }
  public UUID getPropertyId() { return propertyId; }
}
