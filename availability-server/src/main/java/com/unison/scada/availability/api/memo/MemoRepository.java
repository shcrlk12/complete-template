package com.unison.scada.availability.api.memo;

import com.unison.scada.availability.api.windfarm.WindFarmOverview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Memo.MemoId> {
    @Query("SELECT m " +
            "FROM Memo m " +
            "WHERE " +
            "m.memoId.timestamp BETWEEN :startTime AND :endTime " +
            "ORDER BY m.memoId.timestamp ASC")
    List<Memo> findAllDataByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT m " +
            "FROM Memo m " +
            "WHERE m.memoId.timestamp BETWEEN :startTime AND :endTime " +
            "AND m.memoId.windFarmId = :windFarmId " +
            "AND m.memoId.turbineId = :turbineId " +
            "ORDER BY m.memoId.timestamp ASC")
    List<Memo> findByIdBetween(@Param("windFarmId") Integer windFarmId, @Param("turbineId") Integer turbineId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);


    List<Memo> findByMemoIdWindFarmIdAndMemoIdTimestampBetweenOrderByMemoIdTimestampAsc(Integer windFarmId, LocalDateTime startTime, LocalDateTime endTime);

}
