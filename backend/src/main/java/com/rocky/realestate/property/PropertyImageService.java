package com.rocky.realestate.property;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PropertyImageService {
  private final PropertyRepository properties;
  private final PropertyImageRepository images;
  private final MinioClient minio;
  private final String bucket;

  public PropertyImageService(
      PropertyRepository properties,
      PropertyImageRepository images,
      MinioClient minio,
      @Value("${app.storage.bucket:property-images}") String bucket) {
    this.properties = properties;
    this.images = images;
    this.minio = minio;
    this.bucket = bucket;
  }

  @Transactional
  public ImageResponse upload(UUID userId, UUID propertyId, MultipartFile file) {
    Property property =
        properties.findById(propertyId).orElseThrow(() -> new PropertyNotFoundException(propertyId));
    if (!property.getOwnerId().equals(userId)) {
      throw new org.springframework.security.access.AccessDeniedException("You do not own this property");
    }
    if (file.isEmpty() || file.getContentType() == null || !file.getContentType().startsWith("image/")) {
      throw new IllegalArgumentException("Only image files are accepted");
    }
    String objectKey = propertyId + "/" + UUID.randomUUID() + "-" + safeName(file.getOriginalFilename());
    try (InputStream input = file.getInputStream()) {
      minio.putObject(
          PutObjectArgs.builder()
              .bucket(bucket)
              .object(objectKey)
              .stream(input, file.getSize(), -1)
              .contentType(file.getContentType())
              .build());
    } catch (Exception e) {
      throw new IllegalStateException("Image upload failed", e);
    }
    PropertyImage image =
        images.save(
            new PropertyImage(
                propertyId, objectKey, images.findByPropertyIdOrderBySortOrder(propertyId).size()));
    return response(image);
  }

  @Transactional(readOnly = true)
  public List<ImageResponse> list(UUID propertyId) {
    return images.findByPropertyIdOrderBySortOrder(propertyId).stream().map(this::response).toList();
  }

  private ImageResponse response(PropertyImage image) {
    try {
      String url =
          minio.getPresignedObjectUrl(
              GetPresignedObjectUrlArgs.builder()
                  .method(Method.GET)
                  .bucket(bucket)
                  .object(image.getObjectKey())
                  .expiry(1, TimeUnit.HOURS)
                  .build());
      return new ImageResponse(image.getId(), url, image.getSortOrder());
    } catch (Exception e) {
      throw new IllegalStateException("Could not create image URL", e);
    }
  }

  private String safeName(String original) {
    if (original == null || original.isBlank()) return "image";
    return original.replaceAll("[^a-zA-Z0-9._-]", "_");
  }

  public record ImageResponse(UUID id, String url, int sortOrder) {}
}
