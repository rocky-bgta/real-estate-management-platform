package com.rocky.realestate.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {
  @Bean
  MinioClient minioClient(
      @Value("${app.storage.endpoint}") String endpoint,
      @Value("${app.storage.access-key}") String accessKey,
      @Value("${app.storage.secret-key}") String secretKey,
      @Value("${app.storage.bucket:property-images}") String bucket) {
    MinioClient client =
        MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
    try {
      if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
        client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
      }
    } catch (Exception e) {
      throw new IllegalStateException("Could not initialize object storage", e);
    }
    return client;
  }
}
