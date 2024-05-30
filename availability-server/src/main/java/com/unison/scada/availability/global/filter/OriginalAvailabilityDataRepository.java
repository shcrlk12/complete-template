package com.unison.scada.availability.global.filter;

import com.unison.scada.availability.global.OriginalAvailabilityData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OriginalAvailabilityDataRepository extends JpaRepository<OriginalAvailabilityData, OriginalAvailabilityData.OriginalAvailabilityDataId> {
}
