package com.rocky.realestate.common;
import com.rocky.realestate.property.PropertyNotFoundException; import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import java.time.OffsetDateTime; import java.util.Map;
@RestControllerAdvice public class ApiExceptionHandler {
 @ExceptionHandler(PropertyNotFoundException.class) ResponseEntity<Map<String,Object>> notFound(PropertyNotFoundException e){return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("timestamp",OffsetDateTime.now().toString(),"message",e.getMessage()));}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<Map<String,Object>> validation(MethodArgumentNotValidException e){return ResponseEntity.badRequest().body(Map.of("timestamp",OffsetDateTime.now().toString(),"message","Validation failed"));}
}
