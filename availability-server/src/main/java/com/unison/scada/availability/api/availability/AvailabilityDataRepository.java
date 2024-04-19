package com.unison.scada.availability.api.availability;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface AvailabilityDataRepository extends JpaRepository<AvailabilityData, AvailabilityData.AvailabilityDataId> {
    @Query("SELECT ad " +
            "FROM AvailabilityData ad " +
            "LEFT JOIN ad.availabilityType at " +
            "WHERE " +
            "ad.availabilityDataId.timestamp BETWEEN :startTime AND :endTime")
    List<AvailabilityData> findAllDataByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
