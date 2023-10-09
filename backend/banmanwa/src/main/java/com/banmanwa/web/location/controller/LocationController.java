package com.banmanwa.web.location.controller;

import com.banmanwa.web.location.dto.Document;
import com.banmanwa.web.location.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/address")
    public ResponseEntity<List<Document>> findAddress(@RequestParam double x, @RequestParam double y) {
        List<Document> documents = locationService.findAddress(x, y);
        return ResponseEntity.ok(documents);
    }
}
