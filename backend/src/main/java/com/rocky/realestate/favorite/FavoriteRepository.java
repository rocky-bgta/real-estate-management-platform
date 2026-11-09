package com.rocky.realestate.favorite;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
  List<Favorite> findByUserId(UUID userId);
  Optional<Favorite> findByUserIdAndPropertyId(UUID userId, UUID propertyId);
}
