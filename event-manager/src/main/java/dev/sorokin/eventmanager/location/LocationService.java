package dev.sorokin.eventmanager.location;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public LocationService(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    public Location createLocation(Location location) {

        var locationSaved = locationRepository.save(locationMapper.locationToLocationEntity(location));
        return locationMapper.locationEntityToLocation(locationSaved);

    }

    public List<Location> getAllLocations() {
        var listLocationsEntity = locationRepository.findAll();
        return listLocationsEntity.stream().map(locationMapper::locationEntityToLocation).toList();
    }

    public Location getLocationById(Long locationId) {
        if (locationId < 0) {
            throw new IllegalArgumentException("Id cannot be negative");
        }
        var location = locationRepository.findById(locationId).orElseThrow(() -> new EntityNotFoundException(
                "No found location by id=%s".formatted(locationId)
        ));
        return locationMapper.locationEntityToLocation(location);

    }

    @Transactional
    public void deleteLocation(Long locationId) {
        if (!locationRepository.existsById(locationId)) {
            throw new EntityNotFoundException("No found location by id=%s".formatted(locationId));
        }
        locationRepository.deleteById(locationId);
    }

    @Transactional
    public Location updateLocationById(Long locationId, Location location) {
        var locationById = locationRepository.findById(locationId).orElseThrow(() -> new EntityNotFoundException(
                "No found location by id=%s".formatted(locationId)
        ));
        locationById.setName(location.name());
        locationById.setAddress(location.address());
        locationById.setCapacity(location.capacity());
        locationById.setDescription(location.description());
        var updatedLocation = locationRepository.save(locationById);
        return locationMapper.locationEntityToLocation(updatedLocation);
    }
}
