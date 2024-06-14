package com.unison.scada.availability.api.memo;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="Memo")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Memo {

    @EmbeddedId
    private MemoId memoId;

    @Column(nullable = true)
    private String engineerName;

    @Column(nullable = true)
    private LocalDateTime workTime;

    @Column(nullable = true)
    private String material;

    @Column(nullable = true)
    private Integer quantity;

    @Column(nullable = true)
    private String workType;

    @Column(nullable = true)
    private String inspection;

    @Column(nullable = true)
    private String etc;

    @Column(nullable = false)
    @ColumnDefault("1")
    private boolean isActive;

    @Column(nullable = false)
    @ColumnDefault("0")
    private boolean isDelete;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @UpdateTimestamp
    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private String updatedBy;

    @Data
    @Embeddable
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemoId implements Serializable{

        @Column
        private LocalDateTime timestamp;

        @Column
        private Integer windFarmId;

        @Column
        private Integer turbineId;
    }
}
