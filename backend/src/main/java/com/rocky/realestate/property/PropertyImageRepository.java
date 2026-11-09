package com.rocky.realestate.property;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, UUID> {
  List<PropertyImage> findByPropertyIdOrderBySortOrder(UUID propertyId);
}
