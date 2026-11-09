package com.rocky.realestate.admin;
import com.rocky.realestate.property.*; import org.springframework.web.bind.annotation.*; import java.util.UUID;
@RestController @RequestMapping("/api/v1/admin/properties")
public class AdminPropertyController { private final PropertyService service; public AdminPropertyController(PropertyService service){this.service=service;} @PostMapping("/{id}/approve") public PropertyResponse approve(@PathVariable UUID id){return service.approve(id);} @PostMapping("/{id}/reject") public PropertyResponse reject(@PathVariable UUID id){return service.reject(id);} }
