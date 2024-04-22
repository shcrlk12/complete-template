package com.unison.scada.availability.api.memo;

import com.unison.scada.availability.api.availability.AvailabilityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Memo.MemoId> {
    @Query("SELECT m " +
            "FROM Memo m " +
            "WHERE " +
            "m.memoId.timestamp BETWEEN :startTime AND :endTime")
    List<Memo> findAllDataByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
