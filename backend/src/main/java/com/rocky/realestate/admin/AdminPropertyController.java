package com.rocky.realestate.admin;

import com.rocky.realestate.property.PropertyResponse;
import com.rocky.realestate.property.PropertyService;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/properties")
public class AdminPropertyController {
  private final PropertyService service;

  public AdminPropertyController(PropertyService service) {
    this.service = service;
  }

  @GetMapping("/pending")
  public Page<PropertyResponse> pending(Pageable pageable) {
    return service.pending(pageable);
  }

  @PostMapping("/{id}/approve")
  public PropertyResponse approve(@PathVariable UUID id) {
    return service.approve(id);
  }

  @PostMapping("/{id}/reject")
  public PropertyResponse reject(@PathVariable UUID id) {
    return service.reject(id);
  }
}
