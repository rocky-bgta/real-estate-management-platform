package com.rocky.realestate.property;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyService {
  private final PropertyRepository repository;

  public PropertyService(PropertyRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public PropertyResponse create(UUID ownerId, PropertyRequest request) {
    return PropertyResponse.from(repository.save(new Property(ownerId, request)));
  }

  @Transactional(readOnly = true)
  public Page<PropertyResponse> published(
      ListingType listingType,
      PropertyCategory category,
      String city,
      String area,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      Pageable pageable) {
    return repository
        .findAll(
            (root, query, cb) -> {
              List<Predicate> filters = new ArrayList<>();
              filters.add(cb.equal(root.get("status"), PropertyStatus.PUBLISHED));
              if (listingType != null) filters.add(cb.equal(root.get("listingType"), listingType));
              if (category != null) filters.add(cb.equal(root.get("category"), category));
              if (city != null && !city.isBlank()) {
                filters.add(cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%"));
              }
              if (area != null && !area.isBlank()) {
                filters.add(cb.like(cb.lower(root.get("area")), "%" + area.toLowerCase() + "%"));
              }
              if (minPrice != null) filters.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
              if (maxPrice != null) filters.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
              return cb.and(filters.toArray(Predicate[]::new));
            },
            pageable)
        .map(PropertyResponse::from);
  }

  @Transactional(readOnly = true)
  public PropertyResponse getPublished(UUID id) {
    Property property = find(id);
    if (property.getStatus() != PropertyStatus.PUBLISHED) throw new PropertyNotFoundException(id);
    return PropertyResponse.from(property);
  }

  @Transactional(readOnly = true)
  public List<PropertyResponse> mine(UUID ownerId) {
    return repository.findByOwnerIdOrderByCreatedAtDesc(ownerId).stream()
        .map(PropertyResponse::from)
        .toList();
  }

  @Transactional
  public PropertyResponse update(UUID ownerId, UUID id, PropertyRequest request) {
    Property property = owned(ownerId, id);
    property.update(request);
    return PropertyResponse.from(property);
  }

  @Transactional
  public void delete(UUID ownerId, UUID id) {
    repository.delete(owned(ownerId, id));
  }

  @Transactional
  public PropertyResponse approve(UUID id) {
    Property property = find(id);
    property.approve();
    return PropertyResponse.from(property);
  }

  @Transactional
  public PropertyResponse reject(UUID id) {
    Property property = find(id);
    property.reject();
    return PropertyResponse.from(property);
  }

  @Transactional(readOnly = true)
  public Page<PropertyResponse> pending(Pageable pageable) {
    return repository
        .findAll(
            (root, query, cb) -> cb.equal(root.get("status"), PropertyStatus.PENDING_APPROVAL),
            pageable)
        .map(PropertyResponse::from);
  }

  private Property owned(UUID ownerId, UUID id) {
    Property property = find(id);
    if (!property.getOwnerId().equals(ownerId)) {
      throw new AccessDeniedException("You do not own this property");
    }
    return property;
  }

  private Property find(UUID id) {
    return repository.findById(id).orElseThrow(() -> new PropertyNotFoundException(id));
  }
}
