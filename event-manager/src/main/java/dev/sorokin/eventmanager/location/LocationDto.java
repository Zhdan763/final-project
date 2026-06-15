package dev.sorokin.eventmanager.location;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

public record LocationDto(
        @Null
        Long id,
        @NotBlank(message = "Name cannot be empty")
        String name,
        @NotBlank(message = "Address cannot be empty")
        String address,
        @Min(value = 5, message = "Capacity must be greater than or equal to 5")
        Integer capacity,
        String description
) {

}
