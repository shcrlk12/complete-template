package com.unison.scada.availability.api.availability;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AvailabilityTypeRepository extends JpaRepository<AvailabilityType, UUID> {

    @Query("SELECT t FROM AvailabilityType t WHERE t.isActive = :isActive")
    List<AvailabilityType> findByActive(@Param("isActive") boolean isActive);

    @Query("SELECT t FROM AvailabilityType t WHERE t.name = :name")
    Optional<AvailabilityType> findByName(@Param("name") String name);
}
