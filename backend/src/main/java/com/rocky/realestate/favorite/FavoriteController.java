package com.rocky.realestate.favorite;

import com.rocky.realestate.property.PropertyRepository;
import com.rocky.realestate.property.PropertyResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/favorites")
public class FavoriteController {
  private final FavoriteRepository favorites;
  private final PropertyRepository properties;

  public FavoriteController(FavoriteRepository favorites, PropertyRepository properties) {
    this.favorites = favorites;
    this.properties = properties;
  }

  @GetMapping
  @Transactional(readOnly = true)
  public List<PropertyResponse> list(Authentication authentication) {
    UUID userId = (UUID) authentication.getPrincipal();
    return favorites.findByUserId(userId).stream()
        .map(Favorite::getPropertyId)
        .map(properties::findById)
        .flatMap(java.util.Optional::stream)
        .map(PropertyResponse::from)
        .toList();
  }

  @PostMapping("/{propertyId}")
  @Transactional
  public ResponseEntity<Void> add(Authentication authentication, @PathVariable UUID propertyId) {
    UUID userId = (UUID) authentication.getPrincipal();
    if (!properties.existsById(propertyId)) return ResponseEntity.notFound().build();
    if (favorites.findByUserIdAndPropertyId(userId, propertyId).isEmpty()) {
      favorites.save(new Favorite(userId, propertyId));
    }
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{propertyId}")
  @Transactional
  public ResponseEntity<Void> remove(Authentication authentication, @PathVariable UUID propertyId) {
    UUID userId = (UUID) authentication.getPrincipal();
    favorites.findByUserIdAndPropertyId(userId, propertyId).ifPresent(favorites::delete);
    return ResponseEntity.noContent().build();
  }
}
