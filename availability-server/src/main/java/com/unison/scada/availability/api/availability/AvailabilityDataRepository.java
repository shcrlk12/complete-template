package com.unison.scada.availability.api.availability;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AvailabilityDataRepository extends JpaRepository<AvailabilityData, AvailabilityData.AvailabilityDataId> {
}
