package com.rocky.realestate.property;
import jakarta.validation.Valid; import org.springframework.data.domain.*; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.UUID;
@RestController @RequestMapping("/api/v1/properties")
public class PropertyController {
 private final PropertyService service; public PropertyController(PropertyService service){this.service=service;}
 @GetMapping public Page<PropertyResponse> list(@PageableDefault(size=20,sort="createdAt",direction=Sort.Direction.DESC) Pageable pageable){return service.published(pageable);}
 @PostMapping public ResponseEntity<PropertyResponse> create(@Valid @RequestBody PropertyRequest request){return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));}
}
