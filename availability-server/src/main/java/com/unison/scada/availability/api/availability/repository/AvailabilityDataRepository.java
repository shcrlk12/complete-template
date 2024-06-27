package com.unison.scada.availability.api.availability.repository;

import com.unison.scada.availability.api.availability.entity.AvailabilityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AvailabilityDataRepository extends JpaRepository<AvailabilityData, AvailabilityData.AvailabilityDataId> {
    @Query("SELECT ad " +
            "FROM AvailabilityData ad " +
            "LEFT JOIN ad.availabilityType at " +
            "WHERE ad.availabilityDataId.timestamp >= :startTime AND ad.availabilityDataId.timestamp < :endTime " +
            "AND ad.availabilityType IS NOT NULL")
    List<AvailabilityData> findAllDataByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query(value = "SELECT TOP 1 time FROM AVAILABILITY_DATA ad left outer join VARIABLE v on ad.VARIABLE_UUID = v.UUID " +
            "WHERE turbine_id = :turbineId " +
            "AND VARIABLE_UUID = :variableUUID " +
            "AND timestamp >= :time AND timestamp <= :endTime " +
            "ORDER BY timestamp " +
            "ASC", nativeQuery = true)
    Optional<Double> getTimeAfterCertainTimestamp(@Param("turbineId") int turbineId, @Param("variableUUID") UUID variableUUID, @Param("time") LocalDateTime time, @Param("endTime") LocalDateTime endTime);

    @Query(value = "SELECT TOP 1 time FROM AVAILABILITY_DATA ad left outer join VARIABLE v on ad.VARIABLE_UUID = v.UUID " +
            "WHERE turbine_id = :turbineId " +
            "AND VARIABLE_UUID = :variableUUID " +
            "AND timestamp <= :time AND timestamp >= :startTime " +
            "ORDER BY timestamp " +
            "DESC", nativeQuery = true)
    Optional<Double> getTimeBeforeCertainTimestamp(@Param("turbineId") int turbineId, @Param("variableUUID") UUID variableUUID, @Param("time") LocalDateTime time, @Param("startTime") LocalDateTime startTime);

    @Query("SELECT ad " +
            "FROM AvailabilityData ad " +
            "LEFT JOIN ad.availabilityType at " +
            "WHERE " +
            "ad.availabilityDataId.timestamp = :timestamp " +
            "AND ad.availabilityDataId.turbineId = :turbineId")
    List<AvailabilityData> findByIdWithoutUUID(@Param("timestamp") LocalDateTime timestamp, @Param("turbineId") int turbineId);

    @Query("SELECT ad " +
            "FROM AvailabilityData ad " +
            "LEFT JOIN ad.availabilityType at " +
            "LEFT JOIN ad.variable v  " +
            "WHERE ad.availabilityDataId.timestamp >= :startTime AND ad.availabilityDataId.timestamp < :endTime " +
            "AND ad.availabilityDataId.windFarmId = :windFarmId " +
            "ORDER BY ad.availabilityDataId.timestamp ASC")
    List<AvailabilityData> findByWindFarmIdAndTimeBetween(@Param("windFarmId") Integer windFarmId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);


    @Query("SELECT ad " +
            "FROM AvailabilityData ad " +
            "LEFT JOIN ad.availabilityType at " +
            "LEFT JOIN ad.variable v  " +
            "WHERE ad.availabilityDataId.timestamp >= :startTime AND ad.availabilityDataId.timestamp < :endTime " +
            "AND ad.availabilityDataId.windFarmId = :windFarmId " +
            "AND ad.availabilityDataId.turbineId = :turbineId " +
            "ORDER BY ad.availabilityDataId.timestamp ASC")
    List<AvailabilityData> findByWindFarmIdAndTurbineIdAndTimeBetween(@Param("windFarmId") Integer windFarmId, @Param("turbineId") Integer turbineId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query(value = "SELECT CONVERT(VARCHAR(10),ad.availabilityDataId.timestamp,23) as timestamp " +
            "FROM AvailabilityData ad " +
            "LEFT JOIN ad.availabilityType at " +
            "LEFT JOIN ad.variable v  " +
            "WHERE ad.availabilityDataId.timestamp BETWEEN :startTime AND :endTime " +
            "AND ad.availabilityDataId.windFarmId = :windFarmId " +
            "GROUP BY CONVERT(VARCHAR(10), ad.availabilityDataId.timestamp, 23) " +
            "ORDER BY ad.availabilityDataId.timestamp ASC "
            , nativeQuery = true)
    List<AvailabilityData> findByWindFarmIdAndTimeBetweenGroupDaily(@Param("windFarmId") Integer windFarmId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);


}
