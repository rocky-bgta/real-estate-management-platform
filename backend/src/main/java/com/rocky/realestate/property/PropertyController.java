package com.rocky.realestate.property;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {
  private final PropertyService service;

  public PropertyController(PropertyService service) {
    this.service = service;
  }

  @GetMapping
  public Page<PropertyResponse> list(
      @RequestParam(required = false) ListingType listingType,
      @RequestParam(required = false) PropertyCategory category,
      @RequestParam(required = false) String city,
      @RequestParam(required = false) String area,
      @RequestParam(required = false) BigDecimal minPrice,
      @RequestParam(required = false) BigDecimal maxPrice,
      @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return service.published(listingType, category, city, area, minPrice, maxPrice, pageable);
  }

  @GetMapping("/{id}")
  public PropertyResponse get(@PathVariable UUID id) {
    return service.getPublished(id);
  }

  @GetMapping("/mine")
  @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
  public List<PropertyResponse> mine(Authentication authentication) {
    return service.mine((UUID) authentication.getPrincipal());
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
  public ResponseEntity<PropertyResponse> create(
      Authentication authentication, @Valid @RequestBody PropertyRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(service.create((UUID) authentication.getPrincipal(), request));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
  public PropertyResponse update(
      Authentication authentication,
      @PathVariable UUID id,
      @Valid @RequestBody PropertyRequest request) {
    return service.update((UUID) authentication.getPrincipal(), id, request);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
  public ResponseEntity<Void> delete(Authentication authentication, @PathVariable UUID id) {
    service.delete((UUID) authentication.getPrincipal(), id);
    return ResponseEntity.noContent().build();
  }
}
