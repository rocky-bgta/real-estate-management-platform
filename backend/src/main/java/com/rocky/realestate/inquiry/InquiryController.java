package com.rocky.realestate.inquiry;

import com.rocky.realestate.property.PropertyRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/properties/{propertyId}/inquiries")
public class InquiryController {
  private final InquiryRepository inquiries;
  private final PropertyRepository properties;

  public InquiryController(InquiryRepository inquiries, PropertyRepository properties) {
    this.inquiries = inquiries;
    this.properties = properties;
  }

  @PostMapping
  @Transactional
  public ResponseEntity<InquiryResponse> create(
      Authentication authentication, @PathVariable UUID propertyId, @RequestBody InquiryRequest request) {
    if (!properties.existsById(propertyId)) return ResponseEntity.notFound().build();
    Inquiry inquiry =
        inquiries.save(new Inquiry(propertyId, (UUID) authentication.getPrincipal(), request.message()));
    return ResponseEntity.status(HttpStatus.CREATED).body(new InquiryResponse(inquiry.getId()));
  }

  public record InquiryRequest(@NotBlank @Size(max = 2000) String message) {}
  public record InquiryResponse(UUID id) {}
}
