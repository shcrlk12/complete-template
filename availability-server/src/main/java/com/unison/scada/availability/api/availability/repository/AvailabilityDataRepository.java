package com.unison.scada.availability.api.availability.repository;

import com.unison.scada.availability.api.availability.entity.AvailabilityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AvailabilityDataRepository extends JpaRepository<AvailabilityData, AvailabilityData.AvailabilityDataId> {
    @Query("SELECT ad " +
            "FROM AvailabilityData ad " +
            "LEFT JOIN ad.availabilityType at " +
            "WHERE " +
            "ad.availabilityDataId.timestamp BETWEEN :startTime" +
            " AND :endTime" +
            " AND at.variableType = :type")
    List<AvailabilityData> findAllDataByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("type") int type);

    @Query(value = "SELECT TOP 1 time FROM AVAILABILITY_DATA ad left outer join AVAILABILITY_TYPE at on ad.AVAILABILITY_TYPE_UUID = at.UUID " +
            "WHERE turbine_id = :turbineId " +
            "AND AVAILABILITY_TYPE_UUID = :availabilityTypeUUID " +
            "AND timestamp > :time " +
            "AND at.variable_type = 2 " +
            "ORDER BY timestamp " +
            "ASC", nativeQuery = true)
    Long getTimeAfterCertainTimestamp(@Param("turbineId") int turbineId, @Param("availabilityTypeUUID") UUID availabilityTypeUUID, @Param("time") LocalDateTime time);

    @Query(value = "SELECT TOP 1 time FROM AVAILABILITY_DATA ad left outer join AVAILABILITY_TYPE at on ad.AVAILABILITY_TYPE_UUID = at.UUID " +
            "WHERE turbine_id = :turbineId " +
            "AND AVAILABILITY_TYPE_UUID = :availabilityTypeUUID " +
            "AND timestamp < :time " +
            "AND at.variable_type = 2 " +
            "ORDER BY timestamp " +
            "DESC", nativeQuery = true)
    Long getTimeBeforeCertainTimestamp(@Param("turbineId") int turbineId, @Param("availabilityTypeUUID") UUID availabilityTypeUUID, @Param("time") LocalDateTime time);

    @Query("SELECT ad " +
            "FROM AvailabilityData ad " +
            "LEFT JOIN ad.availabilityType at " +
            "WHERE " +
            "ad.availabilityDataId.timestamp = :timestamp " +
            "AND ad.availabilityDataId.turbineId = :turbineId")
    List<AvailabilityData> findByIdWithoutUUID(@Param("timestamp") LocalDateTime timestamp, @Param("turbineId") int turbineId);
}
