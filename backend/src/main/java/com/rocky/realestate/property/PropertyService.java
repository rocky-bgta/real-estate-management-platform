package com.rocky.realestate.property;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyService {
  private final PropertyRepository repository;

  public PropertyService(PropertyRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public PropertyResponse create(PropertyRequest r) {
    return PropertyResponse.from(
        repository.save(
            new Property(
                r.ownerId(),
                r.title(),
                r.description(),
                r.category(),
                r.listingType(),
                r.price(),
                r.city(),
                r.area(),
                r.sizeSqFt(),
                r.bedrooms(),
                r.bathrooms())));
  }

  @Transactional(readOnly = true)
  public Page<PropertyResponse> published(Pageable pageable) {
    return repository
        .findAll((root, q, cb) -> cb.equal(root.get("status"), PropertyStatus.PUBLISHED), pageable)
        .map(PropertyResponse::from);
  }

  @Transactional
  public PropertyResponse approve(UUID id) {
    Property p = repository.findById(id).orElseThrow(() -> new PropertyNotFoundException(id));
    p.approve();
    return PropertyResponse.from(p);
  }

  @Transactional
  public PropertyResponse reject(UUID id) {
    Property p = repository.findById(id).orElseThrow(() -> new PropertyNotFoundException(id));
    p.reject();
    return PropertyResponse.from(p);
  }
}
