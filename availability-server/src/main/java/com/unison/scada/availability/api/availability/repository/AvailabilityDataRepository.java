package com.unison.scada.availability.api.availability.repository;

import com.unison.scada.availability.api.availability.entity.AvailabilityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AvailabilityDataRepository extends JpaRepository<AvailabilityData, AvailabilityData.AvailabilityDataId> {
    @Query("SELECT ad " +
            "FROM AvailabilityData ad " +
            "LEFT JOIN ad.availabilityType at " +
            "WHERE " +
            "ad.availabilityDataId.timestamp BETWEEN :startTime AND :endTime")
    List<AvailabilityData> findAllDataByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT ad " +
            "FROM AvailabilityData ad " +
            "LEFT JOIN ad.availabilityType at " +
            "WHERE " +
            "ad.availabilityDataId.timestamp = :timestamp " +
            "AND ad.availabilityDataId.turbineId = :turbineId")
    List<AvailabilityData> findByIdWithoutUUID(@Param("timestamp") LocalDateTime timestamp, @Param("turbineId") int turbineId);
}
