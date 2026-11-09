package com.rocky.realestate.property;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/properties/{propertyId}/images")
public class PropertyImageController {
  private final PropertyImageService service;

  public PropertyImageController(PropertyImageService service) {
    this.service = service;
  }

  @GetMapping
  public List<PropertyImageService.ImageResponse> list(@PathVariable UUID propertyId) {
    return service.list(propertyId);
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('OWNER','ADMIN')")
  public ResponseEntity<PropertyImageService.ImageResponse> upload(
      Authentication authentication,
      @PathVariable UUID propertyId,
      @RequestPart("file") MultipartFile file) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(service.upload((UUID) authentication.getPrincipal(), propertyId, file));
  }
}
