package dev.sorokin.eventmanager.location;

import dev.sorokin.eventmanager.error.GlobalExceptionHandler;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final LocationMapper locationMapper;
    private final Logger logger = LoggerFactory.getLogger(LocationController.class);

    public LocationController(LocationService locationService, LocationMapper locationMapper) {
        this.locationService = locationService;
        this.locationMapper = locationMapper;
    }

    @PostMapping
    public ResponseEntity<LocationDto> createLocation(
            @RequestBody @Valid LocationDto locationDto
    ) {
        logger.info("Get request for location create: locationDto={}", locationDto);
        Location location = locationService.createLocation(locationMapper.locationDtoToLocation(locationDto));
        return ResponseEntity
                .status(201)
                .body(locationMapper.locationToLocationDto(location));
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        logger.info("Get request for get all locations");
        var body = locationService.getAllLocations().stream().map(locationMapper::locationToLocationDto).toList();
        return ResponseEntity
                .status(200)
                .body(body);
    }


    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> getLocationById(
            @PathVariable Long locationId
    ) {
        logger.info("Get request for get location: locationId={}", locationId);
        var locationById = locationService.getLocationById(locationId);
        return ResponseEntity
                .status(200)
                .body(locationMapper.locationToLocationDto(locationById));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable Long locationId
    ) {
        logger.info("Get request for delete location: locationId={}", locationId);
        locationService.deleteLocation(locationId);
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateLocationById(
            @PathVariable Long locationId,
            @RequestBody @Valid LocationDto locationDto
    ) {
        logger.info("Get request for update location: locationId={}, updateLocationDto={}",
                locationId, locationDto);
        var updatedLocation = locationService.updateLocationById(
                locationId, locationMapper.locationDtoToLocation(locationDto)
        );
        return ResponseEntity
                .status(200)
                .body(locationMapper.locationToLocationDto(updatedLocation));
    }
}
