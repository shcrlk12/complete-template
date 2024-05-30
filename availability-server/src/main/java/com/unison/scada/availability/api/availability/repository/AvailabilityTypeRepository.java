package com.unison.scada.availability.api.availability.repository;

import com.unison.scada.availability.api.availability.entity.AvailabilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AvailabilityTypeRepository extends JpaRepository<AvailabilityType, UUID> {

    @Query("SELECT t FROM AvailabilityType t " +
            "WHERE t.isActive = :isActive " +
            "AND t.variableType = 1")
    List<AvailabilityType> findByActive(@Param("isActive") boolean isActive);

    List<AvailabilityType> findByVariableType( int variableType);

    @Query("SELECT t FROM AvailabilityType t WHERE t.name = :name")
    Optional<AvailabilityType> findByName(@Param("name") String name);
}
